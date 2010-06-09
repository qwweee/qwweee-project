package Project.mainThread.snmp.task;

import Project.config.Config;
import Project.mainThread.snmp.SnmpGetTable;
import Project.struct.DetectSet;
import Project.struct.SWRunTableStruct;
import Project.struct.TaskStruct;

/**
 * @author bbxp
 *
 */
public class TaskSnmpSWRun implements Runnable {
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
    public TaskSnmpSWRun(SnmpGetTable snmp, DetectSet data) {
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
            String[][] tmp = snmp.GetTable(Config.SWRUNTABLE);
            if (tmp == null) {
                isStop = true;
                System.out.println("already power off! break Detection Thread!");
                break;
            }
            for (int i = 0 ; i < tmp.length ; i ++) {
                String key = tmp[i][0];
                if (host.sw.containsKey(key)) {
                    SWRunTableStruct sw = host.sw.get(key);
                    sw.StartTime = System.currentTimeMillis();
                    sw.map[count] = 1;
                    //sw.print();
                } else {
                    SWRunTableStruct value = new SWRunTableStruct();
                    value.StartTime = System.currentTimeMillis();
                    value.Index = Integer.parseInt(tmp[i][0]);
                    value.Name = tmp[i][1];
                    value.ID = tmp[i][2];
                    value.Path = tmp[i][3];
                    value.Parameters = tmp[i][4];
                    value.Type = tmp[i][5];
                    value.Status = tmp[i][6];
                    value.map[count] = 1;
                    host.sw.put(key, value);
                    //value.print();
                }
                // TODO 寫入DB
                /*if (DBTest.getInstance().CheckIPTable(host.getIP())) {
                    if (!DBTest.getInstance().InsertSWTable(host.getIP(), host.getSWRunMap().get(key))) {
                        System.out.println("新增SWTable資料錯誤(已存在資料表)");
                        throw new Exception("新增SWTable資料錯誤");
                    }
                } else {
                    if (!DBTest.getInstance().CreateDB(host.getIP())) {
                        System.out.println("建立資料庫失敗");
                        System.exit(1);
                    } else {
                        System.out.println("建立所需要的所有資料表");
                        DBTest.getInstance().initDB(host.getIP());
                        if (!DBTest.getInstance().InsertSWTable(host.getIP(), host.getSWRunMap().get(key))) {
                            System.out.println("新增SWTable資料錯誤(無存在資料表)");
                            throw new Exception("新增SWTable資料錯誤");
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
