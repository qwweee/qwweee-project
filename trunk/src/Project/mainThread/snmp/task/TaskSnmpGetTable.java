package Project.mainThread.snmp.task;

import Project.mainThread.snmp.SnmpGetTable;
import Project.struct.DetectSet;
import Project.struct.TaskStruct;

/**
 * @author bbxp
 *
 */
public class TaskSnmpGetTable implements Runnable {
    private SnmpGetTable snmp;
    private boolean isStop;
    private DetectSet host;
    private String mib;
    /**
     * @param snmp
     * @param data
     * @param mib
     */
    public TaskSnmpGetTable(SnmpGetTable snmp, DetectSet data, String mib) {
        this.snmp = snmp;
        this.host = data;
        this.mib = mib;
        this.isStop = false;
    }
    @Override
    public synchronized void run() {
        while (!isStop) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String[][] tmp = snmp.GetTable(mib);
            if (tmp == null) {
                isStop = true;
                System.out.println("already power off! break Detection Thread!");
                break;
            }
        }
        snmp = null;
        host = null;
        mib = null;
        @SuppressWarnings("unused")
        TaskStruct task = new TaskStruct(host, "stopSnmpGetTable", System.currentTimeMillis());
        // 加入到task排程內
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
