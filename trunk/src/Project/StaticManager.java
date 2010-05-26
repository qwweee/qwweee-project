/**
 * 
 */
package Project;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;

import Project.config.Config;
import Project.struct.DetectSet;
import Project.utils.Queue;

/**
 * @author bbxp
 *
 */
public class StaticManager {
    /**
     * 存ip對應DataStruct
     * 
     * @see Project.Structure.DataStruct
     */
    public static HashMap<String, DetectSet> IPList = new HashMap<String, DetectSet>();
    /**
     * 存ip對應DataStruct
     * 
     * @see Project.Structure.DataStruct
     */
    public static HashMap<String, DetectSet> FlowList = new HashMap<String, DetectSet>();
    /**
     * 處理netflow packet的queue
     */
    public static Queue NETFLOW_QUEUE = new Queue(Config.NETFLOW_QUEUE_SIZE);
    /**
     * 所有Class都可以存取，用來將Zombie的Thread結束，Queue中存執行結束的Thread
     */
    public static Queue ZOMBIE_QUEUE = new Queue(Config.ZOMBIE_QUEUE_SIZE);
    public static void printDate(long time) {
        System.out.println(DateFormat.getTimeInstance().format(new Date(time)));
    }
    public static void threadInfo() {
        System.out.println(Thread.activeCount());
        Thread[] array = new Thread[Thread.activeCount()];
        Thread.enumerate(array);
        for (int i = 0 ; i < array.length ; i ++) {
            Thread t = array[i];
            Class<? extends Thread> c = array[i].getClass();
            System.out.println(String.format("%4s\t%s\t%s\t%s",t.isAlive(),t.getName(),c.getSimpleName(),t.getId()));
        }
    }
}
