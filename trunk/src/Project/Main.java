/**
 * 
 */
package Project;

import java.sql.SQLException;

import Project.config.Config;
import Project.db.DatabaseFactory;
import Project.mainThread.CloseZombie;
import Project.mainThread.NetFlowProcess;
import Project.mainThread.NetFlowReceiver;
import Project.mainThread.TaskSchedule;
import Project.mainThread.Trapd;
import Project.struct.DetectSet;

/**
 * @author bbxp
 *
 */
public class Main {

    /**
     * @param args
     * @throws SQLException 
     */
    public static void main(String[] args) throws SQLException {
        Config.Load();
        DatabaseFactory.setDatabaseSettings(Config.DBDriver, Config.DBURL, Config.DBUser, Config.DBPassword, Config.DBMaxCon);
        DatabaseFactory.getInstance();
        CloseZombie.getInstance();
        TaskSchedule.getInstance();
        NetFlowProcess.getInstance();
        NetFlowReceiver.getInstance();
        Trapd.getInstance();
        //test();
    }
    private static void test() {
        DetectSet data = new DetectSet("163.22.32.101");
        data.setLinkUp();
        DetectSet data2 = new DetectSet("10.10.32.97");
        data2.setLinkUp();
        DetectSet data3 = new DetectSet("10.10.64.81");
        data3.setLinkUp();
    }
}
