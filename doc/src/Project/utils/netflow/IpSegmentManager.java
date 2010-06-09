package Project.utils.netflow;

/**
 * @author bbxp
 *
 */
public class IpSegmentManager {
    private static int segmentsLen = 0;
    private static int MAXSEG = 1000;
    private static long[] segments = new long[MAXSEG];
    private static String[] segNames = new String[MAXSEG];
    /**
     * @param addr
     * @return
     */
    public static String convertIP(long addr) {
        return convertIP((int) (addr & 0xffffffff));
    }
    
    /**
     * @param addr
     * @return
     */
    public static String getSegNameByIP(long IP) {
        return getSegNameByIP((int) (IP & 0xffffffff));
    }
    
    private static String convertIP(int addr) {
        StringBuffer buf = new StringBuffer();
        buf.append(((addr >>> 24) & 0xff)).append('.').append(
                ((addr >>> 16) & 0xff)).append('.').append(
                ((addr >>> 8) & 0xff)).append('.').append(addr & 0xff);
        return buf.toString();
    }

    private static String getSegNameByIP(int addr) {
        int idx = getIPIdx(addr);
        if (idx == -1) {
            return convertIP(addr);
        } else {
            return segNames[idx];
        }
    }
    
    private static int getIPIdx(int IP) {
        for (int idx = 0; idx < segmentsLen; idx++) {
            if ((segments[idx] & IP) == segments[idx]) {
                if ((segments[idx] | IP) == IP) {
                    return idx;
                }
            }
        }
        return -1;
    }
}
