/**
 * 
 */
package Project.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import Project.config.Config;
import Project.config.DBConfig;
import Project.db.DBFunction;
import Project.db.DatabaseFactory;
import Project.utils.SQLUtil;

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
        //dropALLTable();
        //testFlow("10.10.32.154");
        getData("10.10.32.154", 6667);
        int[] data = {96000,192000,288000,384000,480000,576000,672000,768000};
        int gcd = data[0];
        for (int i = 1 ; i < data.length ; i ++) {
            gcd = gcd(gcd, data[i]);
        }
        System.out.println(gcd);
    }
    private static void dropALLTable() {
        String[] ipList = DBFunction.getInstance().GetAllIPList();
        for (int i = 0 ; i < ipList.length ; i ++) {
            DBFunction.getInstance().dropDataBase(ipList[i]);
        }
        DBFunction.getInstance().clearDNSTable();
        DBFunction.getInstance().clearIPList();
    }
    private static void testFlow(String ip) {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        ArrayList<String> data = new ArrayList<String>();
        String sql = "SELECT * FROM (SELECT flow.dOctets, flow.SrcAddr, flow.DstAddr, flow.SrcPort, flow.DstPort, flow.Stamp, Count(flow.DstPort) as `count`, Sum(flow.dOctets) as `sum` FROM flow WHERE flow.SrcAddr =  '%s' AND flow.SrcPort <>  '161' AND flow.DstPort <>  '162'  GROUP BY flow.DstAddr, flow.DstPort) as `result` WHERE  `result`.`count` >= '2';";
        sql = String.format(sql, ip);
        try {
            con = DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement(sql);
            rs = pstm.executeQuery();
            while (rs.next()) {
                //data.add(rs.getString(1));
                System.out.println(rs.getString(1)+"\t"+rs.getString(2)+"\t"+rs.getString(3)+"\t"+rs.getString(4)+"\t"+rs.getString(5)+"\t"+rs.getString(6)+"\t"+rs.getString(7)+"\t"+rs.getString(8));
            }
            SQLUtil.close(rs, pstm, con);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SQLUtil.close(rs, pstm, con);
        }
        //String[] result = new String[data.size()];
        //result = data.toArray(result);
        return;
    }
    private static void getData(String ip, int port) {
        String query = "SELECT `flow`.`dOctets`, `flow`.`Stamp` FROM `10.10.32.154`.`flow` LIMIT 256;";
        query = "SELECT `flow`.`dOctets`, `flow`.`Stamp` FROM `%s`.`flow` WHERE `flow`.`DstPort` = '%d' ORDER BY flow.Stamp ASC;";
        query = String.format(query, ip, port);
        DatabaseFactory.setDatabaseSettings
        ("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/project?useUnicode=true&characterEncoding=utf8",
                "root", "ji394su3", 100);
        try {
            DatabaseFactory.getInstance();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Date base = null, others = null;
        long size = 0;
        int add = 0;
        int gcd = 0;
        int trange = 4000;
        try {
            con = DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement(query);
            rs = pstm.executeQuery();
            if (port == 6667) {
                rs.next();
                rs.next();
            }
            if (rs.next()) {
                int data = rs.getInt(1);
                base = rs.getTimestamp(2);
                size = (base.getTime()-base.getTime())/trange + add;
                gcd = (int) size;
                System.out.println(size);
            }
            while (rs.next()) {
                int data = rs.getInt(1);
                others = rs.getTimestamp(2);
                size = (others.getTime()-base.getTime())/trange + add;
                gcd = gcd(gcd,(int) size);
                System.out.println(size);
                
            }
            System.out.println("gcd = "+gcd);
            rs.close();
            pstm.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SQLUtil.close(rs, pstm, con);
        }
        int count = 0;
        while (true) {
            if ( Math.pow(2, count)> size ) {
                break;
            }
            count++;
        }
        size = count - 1;
        System.out.println("count = "+((int) Math.round(Math.pow(2, size))));
    }
    private static int gcd(int m, int n) { 
        if(n != 0) return gcd(n, m % n); else return m; 
    }
}
