package Project.struct;

/**
 * @author bbxp
 *
 */
public class TaskStruct {
    /**
     * 
     */
    public Object obj;
    /**
     * 
     */
    public String methodname;
    /**
     * 
     */
    public long time;
    /**
     * 
     */
    public long stime;
    /**
     * @param o
     * @param methodname
     * @param time
     */
    public TaskStruct(Object o, String methodname, long time, long stime) {
        this.obj = o;
        this.methodname = methodname;
        this.time = time;
        this.stime = stime;
    }
}
