/**
 * 
 */
package Project.test;

import Project.config.Config;
import Project.struct.DetectSet;

/**
 * @author bbxp
 *
 */
public class Test {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Config.Load();
        String host = "163.22.32.101";
        DetectSet set = new DetectSet(host);
        set.setLinkUp();
    }

}
