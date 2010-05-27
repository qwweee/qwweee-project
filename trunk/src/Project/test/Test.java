/**
 * 
 */
package Project.test;

import java.sql.Connection;
import java.sql.SQLException;

import Project.config.Config;
import Project.config.DBConfig;
import Project.db.DatabaseFactory;
import Project.struct.DetectSet;

/**
 * @author bbxp
 *
 */
public class Test {

    /**
     * @param args
     * @throws SQLException 
     */
    public static void main(String[] args) throws SQLException {
        Config.Load();
        DBConfig.Load();
        DatabaseFactory.setDatabaseSettings(Config.DBDriver, Config.DBURL, Config.DBUser, Config.DBPassword, Config.DBMaxCon);
        DatabaseFactory.getInstance();
        Connection con = DatabaseFactory.getInstance().getConnection();
    }

}
