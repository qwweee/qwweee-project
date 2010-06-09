/**
 * 
 */
package Project.test;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.Number;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import Project.config.Config;
import Project.config.DBConfig;
import Project.db.DBFunction;
import Project.db.DatabaseFactory;
import Project.struct.FlowGroup;
import Project.utils.SQLUtil;

/**
 * @author bbxp
 *
 */
public class TestDB {
    /**
     * @param args
     * @throws SQLException 
     * @throws IOException 
     */
    public static void main(String[] args) throws SQLException, IOException {
        Config.Load();
        DBConfig.Load();
        DatabaseFactory.setDatabaseSettings(Config.DBDriver, Config.DBURL, Config.DBUser, Config.DBPassword, Config.DBMaxCon);
        DatabaseFactory.getInstance();
        String ip = "10.10.64.134";
        
        //dropALLTable();
        FlowGroup[] list = testFlow(ip);
        for (int i = 0 ; i < list.length ; i ++) {
            getData(ip, list[i].ip, list[i].port);
        }
        //getData("10.10.32.154","163.22.32.101" , 6667);
        //getData("10.10.32.154","10.10.32.33" , 445);
        //getData("10.10.64.100", "163.22.32.191", 80);
        //printData("10.10.32.154", "10.10.32.255", 137);
        //printData("10.10.32.154", "163.22.2.2", 53);
    }
    private static void NGCD() {
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
    public static FlowGroup[] testFlow(String ip) {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        ArrayList<FlowGroup> data = new ArrayList<FlowGroup>();
        String sql = "SELECT * FROM (SELECT flow.DstAddr, flow.DstPort, Count(flow.DstPort) as `count`, Sum(flow.dOctets) as `sum` FROM flow WHERE flow.SrcAddr =  '%s' AND flow.SrcPort <>  '161' AND flow.DstPort <>  '162'  GROUP BY flow.DstAddr, flow.DstPort) as `result` WHERE  `result`.`count` >= '2';";
        sql = "SELECT * FROM (SELECT `flow`.`SrcAddr`, `flow`.`DstAddr`, `flow`.`dPkts`, `flow`.`dOctets`, `flow`.`SrcPort`, `flow`.`DstPort`, Count(`flow`.`DstPort`) as count FROM `%s`.`flow` WHERE `flow`.`SrcAddr` = '%s' AND `flow`.`SrcPort` <> '161' AND `flow`.`DstPort` <> '162' GROUP BY `flow`.`DstAddr`, `flow`.`DstPort` ORDER BY `flow`.`DstAddr` ASC, `flow`.`DstPort` ASC) AS `result` WHERE `result`.`count` >= 8;";
        sql = String.format(sql, ip, ip);
        //System.out.println(sql);
        try {
            con = DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement(sql);
            rs = pstm.executeQuery();
            while (rs.next()) {
                FlowGroup set = new FlowGroup();
                set.ip = rs.getString("DstAddr");
                set.port = rs.getInt("DstPort");
                set.count = rs.getInt("count");
                data.add(set);
                //set.printAll();
            }
            SQLUtil.close(rs, pstm, con);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SQLUtil.close(rs, pstm, con);
        }
        //System.out.println(data.size());
        FlowGroup[] result = new FlowGroup[data.size()];
        result = data.toArray(result);
        return result;
    }
    public static DataStruct[] getData(String ip, String dstip, int port) {
        String query = "SELECT `flow`.`SrcAddr`, `flow`.`DstAddr`, `flow`.`dPkts`, `flow`.`dOctets`, `flow`.`SrcPort`, `flow`.`DstPort`, Count(`flow`.`DstPort`) as count FROM `%s`.`flow` WHERE `flow`.`SrcAddr` = '%s' GROUP BY `flow`.`DstAddr`, `flow`.`DstPort` ORDER BY `flow`.`DstAddr` ASC, `flow`.`DstPort` ASC;";
        //query = "SELECT * FROM (SELECT `flow`.`SrcAddr`, `flow`.`DstAddr`, `flow`.`dPkts`, `flow`.`dOctets`, `flow`.`SrcPort`, `flow`.`DstPort`, Count(`flow`.`DstPort`) as count FROM `%s`.`flow` WHERE `flow`.`SrcAddr` = '%s' GROUP BY `flow`.`DstAddr`, `flow`.`DstPort` ORDER BY `flow`.`DstAddr` ASC, `flow`.`DstPort` ASC) AS `result` WHERE `result`.`count` >= 2;";
        query = "SELECT `flow`.`dOctets`, `flow`.`Stamp`, `flow`.`aFirst`, `flow`.`dPkts` FROM `%s`.`flow` WHERE flow.DstPort = '%d' AND flow.DstAddr = '%s' ORDER BY flow.aFirst ASC"; 
        query = String.format(query, ip, port, dstip);
        //System.out.println(query);
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
        int gcd = 0;
        ArrayList<DataStruct> list = new ArrayList<DataStruct>();
        long size = 0, base = 0, perbase = 0;
        try {
            con = DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement(query);
            rs = pstm.executeQuery();
            while (rs.next()) {
                int pkts = rs.getInt(4);
                if (pkts >= 5 && port == 6667) {
                    continue;
                }
                int data = rs.getInt(1);
                long aFirst = rs.getLong(3);
                if (base == 0) {
                    base = aFirst;
                    perbase = aFirst;
                }
                size = (long)((aFirst - perbase) / 1000.0 +0.5);
                gcd = gcd(gcd,(int)size);
                DataStruct set = new DataStruct();
                set.dataSize = data;
                set.index = (int) ((aFirst - base)/ 1000.0 +0.5);
                list.add(set);
                //System.out.println(size +"\t"+set.index+"\t"+data);
                perbase = aFirst;
            }
            rs.close();
            pstm.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SQLUtil.close(rs, pstm, con);
        }
        //System.out.println(String.format("ip %15s port %5d", dstip, port));
        if (gcd == 0) {
            //System.out.println(String.format("ip %15s port %5d count %5d", dstip, port, list.size()));
            if (list.size() > 100) {
                
            }
            return null;
        }
        //System.out.println(list.get(list.size()-1).index);
        size = list.get(list.size()-1).index / (((gcd / 2)==0)?1:(gcd / 2));
        int count = 0;
        while (true) {
            if ( Math.pow(2, count)> size ) {
                break;
            }
            count++;
        }
        if ( gcd == 1 ) {
            count = (int) Math.round(Math.pow(2, count-1));
        } else {
            count = (int) Math.round(Math.pow(2, count));
            count = (count == size || count == (size-1) || count == (size+1))?count : (count/2);
            //count = (count == size || count == (size-1))?count : (count/2);
        }
        //System.out.println(String.format("gcd %5d count %5d end %5d", gcd, count, size));
        DataStruct[] result = new DataStruct[count];
        for (int i = 0 ; i < result.length ; i ++) {
            result[i] = new DataStruct();
        }
        int tmp = -1;
        for (int i = 0 ; i < list.size() ; i ++) {
            DataStruct data = list.get(i);
            data.index = data.index / (gcd==1?gcd:gcd/2);
            if (data.index >= count) {
                break;
            }
            if (tmp == data.index) {
                result[data.index].same++;
                result[data.index].dataSize = data.dataSize;
                //System.out.println(String.format("%5d %3d", result[data.index].index, result[data.index].same));
            } else {
                //System.out.println(String.format("%5d %3d", data.index, data.same));
                result[data.index] = data;
            }
            tmp = data.index;
        }
        tmp = 0;
        for (int i = 0 ; i < result.length ; i ++) {
            //System.out.println(String.format("%8d %2d", result[i].dataSize, result[i].same));
            if (result[i].same >= 4) {
                System.out.println(String.format("%15s %4d Scan", dstip, port));
                // TODO event 通知管理者 懷疑掃描網路
                return null;
            }
            if (result[i].dataSize != 0) {
                tmp++;
            }
            result[i].gcd = gcd;
        }
        if (tmp == result.length || tmp <= 3) {
            return null;
        }
        if (gcd != 1) {
            System.out.println(String.format("%15s %4d", dstip, port));
            System.out.println(String.format("gcd %5d count %5d end %5d", gcd, count, size));
            //writeExcel(ip, dstip, port, result);
        }
        //System.out.println(String.format("%15s %4d", dstip, port));
        //System.out.println(String.format("gcd %5d count %5d end %5d", gcd, count, size));
        return result;
    }
    private static int gcd(int m, int n) { 
        if(n != 0) return gcd(n, m % n); else return m; 
    }
    public static void writeExcel(String ip, String dstip, int port, DataStruct[] data) {
        File dir = new File("./test/"+ip);
        dir.mkdir();
        String filename =dir.getPath()+"/"+dstip+"_"+port+".xls"; 
        //System.out.println(filename);
        try {
            WritableWorkbook workbook = Workbook.createWorkbook(new File(filename));
            WritableSheet sheet = workbook.createSheet("First Sheet", 0);
            for (int i = 0 ; i < data.length ; i ++) {
                Number number = new Number(0,i+1,data[i].dataSize);
                sheet.addCell(number);
            }
            Number number = new Number(1,0,data.length);
            sheet.addCell(number);
            number = new Number(2,0,data[0].gcd);
            sheet.addCell(number);
            workbook.write();
            workbook.close(); 
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RowsExceededException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        } 
    }
    public static DataStruct[] printData(String ip, String dstip, int port) {
        String query = "SELECT `flow`.`SrcAddr`, `flow`.`DstAddr`, `flow`.`dPkts`, `flow`.`dOctets`, `flow`.`SrcPort`, `flow`.`DstPort`, Count(`flow`.`DstPort`) as count FROM `%s`.`flow` WHERE `flow`.`SrcAddr` = '%s' GROUP BY `flow`.`DstAddr`, `flow`.`DstPort` ORDER BY `flow`.`DstAddr` ASC, `flow`.`DstPort` ASC;";
        //query = "SELECT * FROM (SELECT `flow`.`SrcAddr`, `flow`.`DstAddr`, `flow`.`dPkts`, `flow`.`dOctets`, `flow`.`SrcPort`, `flow`.`DstPort`, Count(`flow`.`DstPort`) as count FROM `%s`.`flow` WHERE `flow`.`SrcAddr` = '%s' GROUP BY `flow`.`DstAddr`, `flow`.`DstPort` ORDER BY `flow`.`DstAddr` ASC, `flow`.`DstPort` ASC) AS `result` WHERE `result`.`count` >= 2;";
        query = "SELECT `flow`.`dOctets`, `flow`.`Stamp`, `flow`.`aFirst`, `flow`.`dPkts` FROM `%s`.`flow` WHERE flow.DstPort = '%d' AND flow.DstAddr = '%s' ORDER BY flow.aFirst ASC"; 
        query = String.format(query, ip, port, dstip);
        //System.out.println(query);
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
        int gcd = 0;
        ArrayList<DataStruct> list = new ArrayList<DataStruct>();
        long size = 0, base = 0, perbase = 0;
        try {
            con = DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement(query);
            rs = pstm.executeQuery();
            while (rs.next()) {
                int pkts = rs.getInt(4);
                if (pkts >= 5 && port == 6667) {
                    continue;
                }
                int data = rs.getInt(1);
                long aFirst = rs.getLong(3);
                if (base == 0) {
                    base = aFirst;
                    perbase = aFirst;
                }
                size = (long)((aFirst - perbase) / 1000.0 +0.5);
                gcd = gcd(gcd,(int)size);
                DataStruct set = new DataStruct();
                set.dataSize = data;
                set.index = (int) ((aFirst - base)/ 1000.0 +0.5);
                list.add(set);
                //System.out.println(size +"\t"+set.index+"\t"+data);
                perbase = aFirst;
            }
            rs.close();
            pstm.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SQLUtil.close(rs, pstm, con);
        }
        //System.out.println(String.format("ip %15s port %5d", dstip, port));
        if (gcd == 0) {
            //System.out.println(String.format("ip %15s port %5d count %5d", dstip, port, list.size()));
            if (list.size() > 100) {
                
            }
            return null;
        }
        //System.out.println(list.get(list.size()-1).index);
        size = list.get(list.size()-1).index / (((gcd / 2)==0)?1:(gcd / 2));
        int count = 0;
        while (true) {
            if ( Math.pow(2, count)> size ) {
                break;
            }
            count++;
        }
        if ( gcd == 1 ) {
            count = (int) Math.round(Math.pow(2, count-1));
        } else {
            count = (int) Math.round(Math.pow(2, count));
            count = (count == size || count == (size-1) || count == (size+1))?count : (count/2);
        }
        //System.out.println(String.format("gcd %5d count %5d end %5d", gcd, count, size));
        DataStruct[] result = new DataStruct[count];
        for (int i = 0 ; i < result.length ; i ++) {
            result[i] = new DataStruct();
        }
        int tmp = -1;
        for (int i = 0 ; i < list.size() ; i ++) {
            DataStruct data = list.get(i);
            data.index = data.index / (gcd==1?gcd:gcd/2);
            if (data.index >= count) {
                break;
            }
            if (tmp == data.index) {
                result[data.index].same++;
                result[data.index].dataSize = data.dataSize;
                //System.out.println(String.format("%5d %3d", result[data.index].index, result[data.index].same));
            } else {
                //System.out.println(String.format("%5d %3d", data.index, data.same));
                result[data.index] = data;
            }
            tmp = data.index;
        }
        tmp = 0;
        for (int i = 0 ; i < result.length ; i ++) {
            System.out.println(String.format("%8d %2d", result[i].dataSize, result[i].same));
            if (result[i].same >= 4) {
                //System.out.println(String.format("%15s %4d Scan", dstip, port));
            }
            if (result[i].dataSize != 0) {
                tmp++;
            }
            result[i].gcd = gcd;
        }
        if (tmp == result.length || tmp == 1) {
            return null;
        }
        if (gcd != 1) {
            System.out.println(String.format("gcd %5d count %5d end %5d", gcd, count, size));
            //writeExcel(dstip, port, result);
        }
        return result;
    }
}
