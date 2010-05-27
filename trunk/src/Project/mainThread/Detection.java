/**
 * 
 */
package Project.mainThread;

import Project.StaticManager;
import Project.config.Config;
import Project.struct.DetectSet;

/**
 * @author 怪叔叔
 *
 */
public class Detection extends Thread {
    /**
     * 存Trap所需資訊，及開始時間
     */
    private DetectSet host;
    /**
     * 
     * @param data 傳入Trap所需要的資訊，以便抓SNMP資料
     * @see Project.Structure.DataStruct
     */
    public Detection(DetectSet data){
        this.setName("Detection");
        host = data;
        host.starttime = System.currentTimeMillis();
        this.setPriority(Thread.MAX_PRIORITY);
        this.start();
    }
    /**
     * 從開機時間開始算Config.DETECT_TIME_OPEN，每隔Config.THREAD_TIME_OPEN時間抓取SNMP資訊
     * 
     * @see Project.Config.Config#DETECT_TIME
     * @see Project.Config.Config#THREAD_TIME
     */
    public void run(){
        System.out.println(host.ip+" Daemon Detecting!!");
        StaticManager.printDate(System.currentTimeMillis());
        // TODO z done 判斷是否關機
        while (!host.isShutdown()) {
            try {
                sleep(Config.PER_OTHER_DETECT_TIME*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            host.tasksnmpget.go();
        }
        // TODO z done 中斷dns反查
        host.setTaskStopTime();
        host.endtime = System.currentTimeMillis();
        // TODO analysis 關機檢測後所要處理分析工作
        // TODO analysis 關機後 進行netflow分析
        // TODO z done join detection thread
        StaticManager.ZOMBIE_QUEUE.enQueue(this);
    }
    /**
     * 取得host的IP
     * @return String ip
     */
    public String getHost() {
        return this.host.ip;
    }
}
