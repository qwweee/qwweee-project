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
import java.sql.Timestamp;
import java.util.ArrayList;

import org.jfree.ui.RefineryUtilities;

import jxl.Workbook;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.Number;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import Project.StaticManager;
import Project.config.Config;
import Project.config.DBConfig;
import Project.db.DBFunction;
import Project.db.DatabaseFactory;
import Project.email.SendMail;
import Project.struct.DataStruct;
import Project.struct.FlowGroup;
import Project.utils.ProcessFFT;
import Project.utils.SQLUtil;
import Project.utils.FFT.Complex;

/**
 * @author bbxp
 *
 */
public class TestDB {
    /**
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {
        Config.Load();
        DBConfig.Load();
        DatabaseFactory.setDatabaseSettings(Config.DBDriver, Config.DBURL, Config.DBUser, Config.DBPassword, Config.DBMaxCon);
        DatabaseFactory.getInstance();
        String ip = "10.10.64.134";
        
        //dropALLTable();
        FlowGroup[] list = getFlowGroups(ip);
        for (int i = 0 ; i < list.length ; i ++) {
            getFlowsData(ip, list[i].ip, list[i].port);
        }
        //write();
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
            gcd = ProcessFFT.gcd(gcd, data[i]);
        }
        System.out.println(gcd);
    }
    private static void connectDB() {
        Config.Load();
        DBConfig.Load();
        DatabaseFactory.setDatabaseSettings(Config.DBDriver, Config.DBURL, Config.DBUser, Config.DBPassword, Config.DBMaxCon);
        try {
            DatabaseFactory.getInstance();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static FlowGroup[] getFlowGroups(String ip, long start, long end) {
        return DBFunction.getInstance().getFlowGroups(ip, start, end);
    }
    public static FlowGroup[] getFlowGroups(String ip) {
        return DBFunction.getInstance().getFlowGroups(ip);
    }
    public static DataStruct[] getFlowsData(String ip, String dstip, int port) {
        return DBFunction.getInstance().getFlowsData(ip, dstip, port, true);
        /*String sql = "SELECT `flow`.`SrcAddr`, `flow`.`DstAddr`, `flow`.`dPkts`, `flow`.`dOctets`, `flow`.`SrcPort`, `flow`.`DstPort`, Count(`flow`.`DstPort`) as count FROM `%s`.`flow` WHERE `flow`.`SrcAddr` = '%s' GROUP BY `flow`.`DstAddr`, `flow`.`DstPort` ORDER BY `flow`.`DstAddr` ASC, `flow`.`DstPort` ASC;";
        //query = "SELECT * FROM (SELECT `flow`.`SrcAddr`, `flow`.`DstAddr`, `flow`.`dPkts`, `flow`.`dOctets`, `flow`.`SrcPort`, `flow`.`DstPort`, Count(`flow`.`DstPort`) as count FROM `%s`.`flow` WHERE `flow`.`SrcAddr` = '%s' GROUP BY `flow`.`DstAddr`, `flow`.`DstPort` ORDER BY `flow`.`DstAddr` ASC, `flow`.`DstPort` ASC) AS `result` WHERE `result`.`count` >= 2;";
        sql = "SELECT `flow`.`dOctets`, `flow`.`Stamp`, `flow`.`aFirst`, `flow`.`dPkts` FROM `%s`.`flow` WHERE flow.DstPort = '%d' AND flow.DstAddr = '%s' ORDER BY flow.aFirst ASC"; 
        sql = String.format(sql, ip, port, dstip);
        //System.out.println(query);
        connectDB();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        int gcd = 0;
        ArrayList<DataStruct> list = new ArrayList<DataStruct>();
        long size = 0, base = 0, perbase = 0;
        try {
            con = DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement(sql);
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
                gcd = ProcessFFT.gcd(gcd,(int)size);
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
        //count = (int) Math.round(Math.pow(2, count));
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
        boolean isScan = false;
        for (int i = 0 ; i < result.length ; i ++) {
            // TODO detect 判斷scan
            if (result[i].same >= Config.SCANCOUNT) {
                isScan = true;
            }
            if (result[i].dataSize != 0) {
                tmp++;
            }
            result[i].gcd = gcd;
        }
        if (isScan) {
            System.out.println(String.format("%15s %4d Scan", dstip, port));
            // TODO event 通知管理者 懷疑掃描網路或攻擊
            SendMail.getInstance().sendMail(String.format("%s\n%d\nScan", dstip, port), StaticManager.SCAN_DETECTED, StaticManager.OPTION_WARNING);
            writeExcel(ip,dstip,port,result,true,ProcessFFT.processFFT(result, dstip, port, true));
            return null;
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
        return result;*/
    }
    public static void write()throws Exception{
        WritableWorkbook wwb=Workbook.createWorkbook(new File("test/test.xls"));
        WritableSheet ws=wwb.createSheet("Test Sheet 1",0);
        File file=new File("test/test.png");
        WritableImage image=new WritableImage(5, 1, 6, 18,file);
        ws.addImage(image);
        wwb.write();
        wwb.close();
    } 
    public static DataStruct[] printFlowsData(String ip, String dstip, int port) {
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
                gcd = ProcessFFT.gcd(gcd,(int)size);
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
