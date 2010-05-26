/**
 * 
 */
package Project.mainThread;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import Project.struct.TaskStruct;

/**
 * @author bbxp
 *
 */
public class TaskSchedule extends Thread {
    private ArrayList<TaskStruct> taskList;
    private boolean isStop;
    private static TaskSchedule instance;
    public static TaskSchedule getInstance() {
        return (instance == null)?(instance = new TaskSchedule()): instance;
    }

    /**
     * 
     */
    private TaskSchedule() {
        this.setName("TaskSchedule");
        taskList = new ArrayList<TaskStruct>();
        isStop = false;
        this.start();
    }
    /**
     * @param task
     */
    public void insertTask(TaskStruct task) {
        taskList.add(task);
    }
    /**
     * 
     */
    public void setStop() {
        isStop = true;
    }
    public void run() {
        while (!isStop) {
            try {
                sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long current = System.currentTimeMillis();
            for(int i = 0 ; i < taskList.size() ; i ++) {
                TaskStruct task = taskList.get(i);
                if (task.time <= current) {
                    try {
                        processTask(task);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                    taskList.remove(task);
                }
            }
        }
    }
    private void processTask(TaskStruct task) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        task.obj.getClass().getMethod(task.methodname).invoke(task.obj);
        //StaticManager.printDate(task.time);
    }
}
