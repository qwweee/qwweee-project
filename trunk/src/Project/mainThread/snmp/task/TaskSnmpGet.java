package Project.mainThread.snmp.task;

import Project.mainThread.TaskSchedule;
import Project.mainThread.snmp.SnmpGet;
import Project.struct.DetectSet;
import Project.struct.TaskStruct;

/**
 * @author bbxp
 *
 */
public class TaskSnmpGet implements Runnable {
    private SnmpGet snmp;
    private boolean isStop;
    private DetectSet host;
    private String mib;
    /**
     * @param snmp
     * @param data
     * @param mib
     */
    public TaskSnmpGet(SnmpGet snmp, DetectSet data, String mib) {
        this.snmp = snmp;
        this.host = data;
        this.mib = mib;
        this.isStop = false;
    }
    @Override
    public void run() {
        while (!isStop) {
            synchronized(this) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String tmp = snmp.getValue(mib);
                if (tmp == null) {
                    System.out.println("SNMPGET already power off! break Detection Thread!");
                    break;
                }
            }
        }
        host.setShutdown();
        TaskStruct task = new TaskStruct(host, "stopSnmpGet", System.currentTimeMillis(), host.starttime);
        TaskSchedule.getInstance().insertTask(task);
        snmp = null;
        host = null;
        mib = null;
    }
    /**
     * 叫醒Thread
     */
    public synchronized void go() {
        notifyAll();
    }
    /**
     * 設定結束收集資料
     */
    public synchronized void setStop() {
        this.isStop = true;
        notifyAll();
    }
}
