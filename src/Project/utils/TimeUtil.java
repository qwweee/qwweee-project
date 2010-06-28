/**
 * 
 */
package Project.utils;

import java.text.DateFormat;
import java.util.Date;

/**
 * @author bbxp
 *
 */
public class TimeUtil {
    /**
     * @param time
     * @return
     */
    public static String timeToString(long time) {
        return DateFormat.getInstance().format(new Date(time));
    }
}
