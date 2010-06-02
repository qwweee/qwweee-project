/**
 * 
 */
package Project.test;

import java.sql.SQLException;

import Project.config.Config;
import Project.config.DBConfig;
import Project.db.DBFunction;
import Project.db.DatabaseFactory;

/**
 * @author bbxp
 *
 */
public class TestDB {

    /**
     * @param args
     * @throws SQLException 
     */
    public static void main(String[] args) throws SQLException {
        Config.Load();
        DBConfig.Load();
        DatabaseFactory.setDatabaseSettings(Config.DBDriver, Config.DBURL, Config.DBUser, Config.DBPassword, Config.DBMaxCon);
        DatabaseFactory.getInstance();
        dropALLTable();
    }
    private static void dropALLTable() {
        String[] ipList = DBFunction.getInstance().GetAllIPList();
        for (int i = 0 ; i < ipList.length ; i ++) {
            DBFunction.getInstance().dropDataBase(ipList[i]);
        }
        DBFunction.getInstance().clearDNSTable();
        DBFunction.getInstance().clearIPList();
    }
}
