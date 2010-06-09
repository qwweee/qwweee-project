package Project.utils;

/**
 * @author bbxp
 *
 */
public class SystemUtil {
    private static Runtime runTime = Runtime.getRuntime();

    /**
     * <font color=#00800>傳回記憶體使用量</font>
     * 
     * @return 記憶體使用量
     */
    public static long getUsedMemoryMB() {
        return (runTime.totalMemory() - runTime.freeMemory()) / 1024L / 1024L;
    }

    /**
     * <font color=#00800>傳回記憶體使用量上限</font>
     * 
     * @return 記憶體最大可用量
     */
    public static long getTotalMemoryMB() {
        return runTime.totalMemory() / 1024L / 1024L;
    }

    /**
     * <font color=#00800>傳回記憶體尚未使用量</font>
     * 
     * @return 記憶體尚未使用量
     */
    public static long getFreeMemoryMB() {
        return runTime.freeMemory() / 1024L / 1024L;
    }
}