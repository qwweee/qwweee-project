/**
 * 
 */
package Project.mainThread;

import Project.StaticManager;
import Project.utils.Queue;

/**
 * @author 怪叔叔
 *
 */
public class CloseZombie extends Thread {
    /**
     * 用來判斷這個Thread是否要結束
     */
    private boolean isStop;
    private Queue queue;
    /**
     * CloseZombie的建構元，初始化Queue，並執行此Thread
     * @param size Queue的大小
     * @see Project.Util.Queue
     */
    private static CloseZombie instance;
    public static CloseZombie getInstance() {
        return (instance == null)?(instance = new CloseZombie()):instance;
    }
    private CloseZombie(){
        this.setName("CloseZombie");
        queue = StaticManager.ZOMBIE_QUEUE;
        isStop = false;
        this.start();
    }
    /**
     * Thread開始執行的部份，抓取Queue中的Object(Thread)，將Thread結束
     */
    public void run(){
        while(!isStop){
            try{
                sleep(3000);
                Object o = queue.deQueue(); 
                System.out.println(o.getClass().getName());
                if (o instanceof Detection) {
                    Detection zombie = (Detection)o;
                    StaticManager.IPList.remove(zombie.getHost());
                    zombie.join();
                    System.out.println("Detection Thread is Join");
                } else if (o instanceof DnsAnalysis){
                    DnsAnalysis zombie = (DnsAnalysis)o;
                    StaticManager.FlowList.remove(zombie.ip);
                    zombie.join();
                    System.out.println("DnsAnalysis Thread is Join");
                } else {
                    Thread zombie = (Thread)o;
                    zombie.join();
                    System.out.println(zombie.getName() + " Thread is Join");
                }
            } catch(InterruptedException e) {
                e.printStackTrace();
                System.out.println("CloseZombie Interrupt Exception");
            }
        }
        System.out.println("CloseZombie結束");
    }
    /**
     * 設定是否將Thread結束
     * @param isStop 是否結束
     */
    public void setStop(boolean isStop){
        this.isStop = isStop;
    }
    /**
     * CloseZombie Thread是否已經結束
     * @return boolean 此Thread是否結束
     */
    public boolean isStop(){
        return this.isStop;
    }
}
