package Project.mainThread.snmp.task;

import Project.config.Config;
import Project.mainThread.snmp.SnmpGetTable;
import Project.struct.DetectSet;
import Project.struct.TCPConnectStruct;
import Project.struct.TaskStruct;

/**
 * @author bbxp
 *
 */
public class TaskSnmpTCP implements Runnable {
    private SnmpGetTable snmp;
    private boolean isStop;
    private DetectSet host;
    /**
     * 計算跑了幾次的程式
     */
    public int count;
    /**
     * @param snmp
     * @param data
     */
    public TaskSnmpTCP(SnmpGetTable snmp, DetectSet data) {
        this.snmp = snmp;
        this.host = data;
        this.isStop = false;
        this.count = 0;
    }
    @Override
    public synchronized void run() {
        while (!isStop) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String[][] tmp = snmp.GetTable(Config.TCPCONNECT);
            if (tmp == null) {
                isStop = true;
                System.out.println("already power off! break Detection Thread!");
                break;
            }
            for (int i = 0 ; i < tmp.length ; i ++) {
                String key = tmp[i][1]+tmp[i][2]+tmp[i][3]+tmp[i][4];
                if (host.tcp.containsKey(key)) {
                    TCPConnectStruct tcp = host.tcp.get(key);
                    tcp.map[count++] = 1;
                    //tcp.print();
                } else {
                    TCPConnectStruct value = new TCPConnectStruct();
                    value.StartTime = System.currentTimeMillis();
                    value.Status = tmp[i][0];
                    value.LocalAddress = tmp[i][1];
                    value.LocalPort = Integer.parseInt(tmp[i][2]);
                    value.RemoteAddress = tmp[i][3];
                    value.RemotePort = Integer.parseInt(tmp[i][4]);
                    value.map[count] = 1;
                    host.tcp.put(key, value);
                    //value.print();
                }
                // TODO 寫入DB
                /*if (DBTest.getInstance().CheckIPTable(host.getIP())) {
                    if (!DBTest.getInstance().InsertTCPTable(host.getIP(), host.getTCPMap().get(key))) {
                        System.out.println("新增TCPTable資料錯誤(已存在資料表)");
                        throw new Exception("新增TCPTable資料錯誤");
                    }
                } else {
                    if (!DBTest.getInstance().CreateDB(host.getIP())) {
                        System.out.println("建立資料庫失敗");
                        System.exit(1);
                    } else {
                        System.out.println("建立所需要的所有資料表");
                        DBTest.getInstance().initDB(host.getIP());
                        if (!DBTest.getInstance().InsertTCPTable(host.getIP(), host.getTCPMap().get(key))) {
                            System.out.println("新增TCPTable資料錯誤(無存在資料表)");
                            throw new Exception("新增TCPTable資料錯誤");
                        }
                    }
                }*/
            }
            count++;
        }
        snmp = null;
        host = null;
        TaskStruct task = new TaskStruct(host, "stopSnmpGetTable", System.currentTimeMillis());
        // TODO 加入到task排程內
    }
    /**
     * 叫醒Thread
     */
    public synchronized void go() {
        notifyAll();
    }
    /**
     * 是否已經關機
     * @return 是否關機
     */
    public boolean isShutdown() {
        return isStop;
    }
    /**
     * 設定結束收集資料
     */
    public void setStop() {
        this.isStop = true;
    }
}
