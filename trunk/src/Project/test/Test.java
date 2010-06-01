/**
 * 
 */
package Project.test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Project.config.Config;
import Project.config.DBConfig;
import Project.db.DBFunction;
import Project.db.DatabaseFactory;
import Project.struct.DetectSet;
import Project.utils.SQLUtil;

/**
 * @author bbxp
 *
 */
public class Test {

    /**
     * @param args
     * @throws SQLException 
     * @throws InterruptedException 
     * @throws IOException 
     */
    public static void main(String[] args) throws SQLException, IOException, InterruptedException {
        //Config.Load();
        //DBConfig.Load();
        //DatabaseFactory.setDatabaseSettings(Config.DBDriver, Config.DBURL, Config.DBUser, Config.DBPassword, Config.DBMaxCon);
        //DatabaseFactory.getInstance();
        //DetectSet set = new DetectSet("10.10.32.97");
        //set.setLinkUp();
        //selectDB();
        InetAddress address = InetAddress.getByName("10.10.32.154");
        while(!address.isReachable(5000)) {
            System.out.println("not ping ");
            Thread.sleep(1000);
        }
    }
    /**
     * @throws SQLException
     */
    public static void insertDB() throws SQLException {
        Connection con = DatabaseFactory.getInstance().getConnection();
        String sql = "INSERT INTO `test` (`id`,`text`) VALUES (NULL,'%s')";
        byte[] value = {1,1,1,1,1,1,1,1,1,1,0,0};
        sql = String.format(sql, new String(value));
        System.out.println(sql);
        PreparedStatement pstm = con.prepareStatement(sql);
        pstm.execute();
        SQLUtil.close(pstm, con);
    }
    /**
     * @throws SQLException
     */
    public static void selectDB() throws SQLException {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        String sql = "SELECT test.`text` FROM test;";
        con = DatabaseFactory.getInstance().getConnection();
        pstm = con.prepareStatement(sql);
        rs = pstm.executeQuery();
        String text = "";
        while (rs.next()) {
            text = rs.getString(1);
            byte[] value = text.getBytes();
            for (int i = 0 ; i < value.length ; i++) {
                System.out.print(value[i]+"\t");
            }
            System.out.println();
        }
        SQLUtil.close(rs, pstm, con);
    }

}
