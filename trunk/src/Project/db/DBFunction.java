/**
 * 
 */
package Project.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import Project.StaticManager;
import Project.config.Config;
import Project.config.DBConfig;
import Project.email.SendMail;
import Project.struct.BlackListStruct;
import Project.struct.DataStruct;
import Project.struct.FlowGroup;
import Project.struct.GrayListStruct;
import Project.struct.SWRunTableStruct;
import Project.struct.TCPConnectStruct;
import Project.utils.ExcelUtil;
import Project.utils.ProcessFFT;
import Project.utils.SQLUtil;
import Project.utils.netflow.packets.V5_Flow;

/**
 * @author bbxp
 * TODO z done db 重新規劃存入資料庫格式 SWTable TCPTable (UDPTable)
 */
public class DBFunction {
    private static DBFunction instance;
    private DBFunction() {
    }
    /**
     * @return
     */
    public static DBFunction getInstance() {
        return (instance == null)?(instance = new DBFunction()):instance;
    }
    private boolean createDB(String ip) {
        String sql = String.format(DBConfig.CREATEDB, ip);
        return execSQL(sql);
    }
    private boolean createFlowTable(String ip) {
        String sql = String.format(DBConfig.CREATEFLOWTABLE, ip);
        return execSQL(sql);
    }
    private boolean createSWTable(String ip) {
        String sql = String.format(DBConfig.CREATESWTABLE, ip);
        return execSQL(sql);
    }
    private boolean createTCPTable(String ip) {
        String sql = String.format(DBConfig.CREATETCPTABLE, ip);
        return execSQL(sql);
    }
    private boolean clearTable(String dbname, String tablename) {
        String sql = String.format(DBConfig.CLEARTABLE, dbname,tablename);
        return execSQL(sql);
    }
    private boolean execSQL(String sql) {
        boolean noerror = true;
        Connection con = null;
        PreparedStatement pstm = null;
        //System.out.println(sql);
        try {
            con = DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement(sql);
            pstm.execute();
            SQLUtil.close(pstm, con);
        } catch (SQLException e) {
            e.printStackTrace();
            noerror = false;
        } finally {
            SQLUtil.close(pstm, con);
        }
        return noerror;
    }
    private boolean checkIPTable(String ip) {
        boolean havedata = false;
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        String sql = String.format(DBConfig.SEARCHIPTABLE, ip);
        //System.out.println(sql);
        try {
            con = DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement(sql);
            rs = pstm.executeQuery();
            if (rs.next()) {
                havedata = true;
            } else {
                havedata = false;
            }
            SQLUtil.close(rs, pstm, con);
        } catch (SQLException e) {
            e.printStackTrace();
            havedata = false;
        } finally {
            SQLUtil.close(rs, pstm, con);
        }
        return havedata;
    }
    private boolean insertIPTable(String ip) {
        boolean noerror = true;
        Connection con = null;
        PreparedStatement pstm = null;
        //System.out.println(DBConfig.INSERTIPTABLE);
        try {
            con = DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement(DBConfig.INSERTIPTABLE);
            pstm.setString(1, ip);
            pstm.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            pstm.execute();
            SQLUtil.close(pstm, con);
        } catch (SQLException e) {
            e.printStackTrace();
            noerror = false;
        } finally {
            SQLUtil.close(pstm, con);
        }
        return noerror;
    }
    public boolean initAllTable(String ip) {
        boolean noerror = true;
        // TODO z done db check IPList
        if (checkIPTable(ip)) { // 已經有建立DB過
            System.out.println("clear table");
            clearTable(ip, "flow");
            clearTable(ip, "swtable");
            clearTable(ip, "tcptable");
            return noerror;
        }
        // TODO z done db create DB and all table, insertIPTable
        System.out.println("create table");
        noerror &= createDB(ip);
        noerror &= insertIPTable(ip);
        noerror &= createFlowTable(ip);
        noerror &= createSWTable(ip);
        noerror &= createTCPTable(ip);
        return noerror;
    }
    public boolean insertDNSTable(String ip, String dns, int status) {
        boolean noerror = true;
        Connection con = null;
        PreparedStatement pstm = null;
        //System.out.println(DBConfig.INSERTDNSTABLE);
        try {
            con = DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement(DBConfig.INSERTDNSTABLE);
            pstm.setString(1, ip);
            pstm.setString(2, dns);
            pstm.setInt(3, status);
            pstm.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            pstm.execute();
            SQLUtil.close(pstm, con);
        } catch (SQLException e) {
            e.printStackTrace();
            noerror = false;
        } finally {
            SQLUtil.close(pstm, con);
        }
        return noerror;
    }
    public boolean insertFlowTable(String ip, V5_Flow flow, long sysUpTime, long sec, long nsec, long sequence, long enType, long enID) {
        boolean noerror = true;
        Connection con = null;
        PreparedStatement pstm = null;
        String sql = String.format(DBConfig.INSERTFLOWTABLE, ip);
        //System.out.println(sql);
        try {
            con = DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement(sql);
            pstm.setString(1, flow.RouterIP);
            pstm.setLong(2, sysUpTime);
            pstm.setLong(3, sec);
            pstm.setLong(4, nsec);
            pstm.setLong(5, sequence);
            pstm.setLong(6, enType);
            pstm.setLong(7, enID);
            pstm.setString(8, flow.srcaddr);
            pstm.setString(9, flow.dstaddr);
            pstm.setString(10, flow.nexthop);
            pstm.setLong(11, flow.input);
            pstm.setLong(12, flow.output);
            pstm.setLong(13, flow.dPkts);
            pstm.setLong(14, flow.dOctets);
            pstm.setLong(15, flow.First);
            pstm.setLong(16, flow.Last);
            pstm.setLong(17, flow.srcport);
            pstm.setLong(18, flow.dstport);
            pstm.setByte(19, flow.tcp_flags);
            pstm.setByte(20, flow.prot);
            pstm.setByte(21, flow.tos);
            pstm.setLong(22, flow.src_as);
            pstm.setLong(23, flow.dst_as);
            pstm.setByte(24, flow.src_mask);
            pstm.setByte(25, flow.dst_mask);
            pstm.setTimestamp(26, new Timestamp(System.currentTimeMillis()));
            pstm.execute();
            SQLUtil.close(pstm, con);
        } catch (SQLException e) {
            e.printStackTrace();
            noerror = false;
        } finally {
            SQLUtil.close(pstm, con);
        }
        return noerror;
    }
    public boolean insertSWTable(String ip, SWRunTableStruct data, boolean isboot) {
        boolean noerror = true;
        Connection con = null;
        PreparedStatement pstm = null;
        String sql = String.format(DBConfig.INSERTSWTABLE, ip);
        //System.out.println(sql);
        try {
            con = DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement(sql);
            pstm.setInt(1, data.Index);
            pstm.setString(2, data.Name);
            pstm.setString(3, data.ID);
            pstm.setString(4, data.Path);
            pstm.setString(5, data.Parametes);
            pstm.setString(6, data.Type);
            pstm.setString(7, data.Status);
            pstm.setTimestamp(8, new Timestamp(data.StartTime));
            pstm.setTimestamp(9, new Timestamp(data.EndTime));
            pstm.setString(10, new String(data.map));
            pstm.setInt(11, (isboot)?1:0);
            pstm.execute();
            SQLUtil.close(pstm, con);
        } catch (SQLException e) {
            e.printStackTrace();
            noerror = false;
        } finally {
            SQLUtil.close(pstm, con);
        }
        return noerror;
    }
    public boolean insertTCPTable(String ip, TCPConnectStruct data, boolean isboot) {
        boolean noerror = true;
        Connection con = null;
        PreparedStatement pstm = null;
        String sql = String.format(DBConfig.INSERTTCPTABLE, ip);
        //System.out.println(sql);
        try {
            con = DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement(sql);
            pstm.setString(1, data.LocalAddress);
            pstm.setInt(2, data.LocalPort);
            pstm.setString(3, data.RemoteAddress);
            pstm.setInt(4, data.RemotePort);
            pstm.setString(5, data.Status);
            pstm.setTimestamp(6, new Timestamp(data.StartTime));
            pstm.setTimestamp(7, new Timestamp(data.EndTime));
            pstm.setString(8, new String(data.map));
            pstm.setInt(9, (isboot)?1:0);
            pstm.execute();
            SQLUtil.close(pstm, con);
        } catch (SQLException e) {
            e.printStackTrace();
            noerror = false;
        } finally {
            SQLUtil.close(pstm, con);
        }
        return noerror;
    }
    public boolean updateIPTable(String ip) {
        boolean havechange = true;
        Connection con = null;
        PreparedStatement pstm = null;
        //System.out.println(DBConfig.UPDATEIPTABLE);
        try {
            con = DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement(DBConfig.UPDATEIPTABLE);
            pstm.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            pstm.setString(2, ip);
            pstm.execute();
            SQLUtil.close(pstm, con);
        } catch (SQLException e) {
            e.printStackTrace();
            havechange = false;
        } finally {
            SQLUtil.close(pstm, con);
        }
        return havechange;
    }
    public boolean updateDNSTable(String ip, String dns, int status) {
        boolean havechange = true;
        Connection con = null;
        PreparedStatement pstm = null;
        //System.out.println(DBConfig.UPDATEDNSTABLE);
        try {
            con = DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement(DBConfig.UPDATEDNSTABLE);
            pstm.setString(1, ip);
            pstm.setString(2, dns);
            pstm.setInt(3, status);
            pstm.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            pstm.execute();
            SQLUtil.close(pstm, con);
        } catch (SQLException e) {
            e.printStackTrace();
            havechange = false;
        } finally {
            SQLUtil.close(pstm, con);
        }
        return havechange;
    }
    public int checkDNSTable(String ip, String dns) {
        int result = 0;
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        String sql = String.format(DBConfig.SEARCHDNSTABLE, dns);
        //System.out.println(sql);
        try {
            con = DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement(sql);
            rs = pstm.executeQuery();
            String tip = "";
            int status = 0;
            @SuppressWarnings("unused")
            long time = 0;
            if (rs.next()) {
                tip = rs.getString(1);
                status = rs.getInt(2);
                time = rs.getTimestamp(3).getTime();
                if (tip.equalsIgnoreCase(ip)) { //找到對應dns之ip
                    result = 1;
                } else { //異常dns對應，dns對應到不同ip
                    result = 2;
                }
                if (status != 0) {
                    result = 3;
                }
                // TODO not sure 運用時間判斷
            }
            SQLUtil.close(rs, pstm, con);
        } catch (SQLException e) {
            e.printStackTrace();
            result = -1;
        } finally {
            SQLUtil.close(rs, pstm, con);
        }
        return result; //0為無對應資料 -1為錯誤 1為找到相同對應 2為對應異常 3為黑名單
    }
    public String[] GetAllIPList() {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        ArrayList<String> data = new ArrayList<String>();
        try {
            con = DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement(DBConfig.GETALLIPLIST);
            rs = pstm.executeQuery();
            while (rs.next()) {
                data.add(rs.getString(1));
            }
            SQLUtil.close(rs, pstm, con);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SQLUtil.close(rs, pstm, con);
        }
        String[] result = new String[data.size()];
        result = data.toArray(result);
        return result;
    }
    public SWRunTableStruct[] GetSWRunTable(String ip, int type) {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        String sql = String.format(DBConfig.GETSWRUNTABLE, ip);
        ArrayList<SWRunTableStruct> data = new ArrayList<SWRunTableStruct>();
        try {
            con = DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement(sql);
            pstm.setInt(1, type);
            rs = pstm.executeQuery();
            while (rs.next()) {
                SWRunTableStruct set = new SWRunTableStruct();
                set.Index = rs.getInt(1);
                set.Name = rs.getString(2);
                set.ID = rs.getString(3);
                set.Path = rs.getString(4);
                set.Parametes = rs.getString(5);
                set.Type = rs.getString(6);
                set.Status = rs.getString(7);
                set.StartTime = rs.getTimestamp(8).getTime();
                set.EndTime = rs.getTimestamp(9).getTime();
                set.map = rs.getString(10).getBytes();
                data.add(set);
            }
            SQLUtil.close(rs, pstm, con);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SQLUtil.close(rs, pstm, con);
        }
        SWRunTableStruct[] result = new SWRunTableStruct[data.size()];
        result = data.toArray(result);
        return result;
    }
    public BlackListStruct[] GetBlackList() {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        ArrayList<BlackListStruct> data = new ArrayList<BlackListStruct>();
        try {
            con = DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement(DBConfig.GETBLACKLIST);
            rs = pstm.executeQuery();
            while (rs.next()) {
                BlackListStruct set = new BlackListStruct();
                set.No = rs.getInt(1);
                set.Name = rs.getString(2);
                set.Path = rs.getString(3);
                set.Parametes = rs.getString(4);
                set.Type = rs.getString(5);
                set.Status = rs.getInt(6);
                data.add(set);
            }
            SQLUtil.close(rs, pstm, con);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SQLUtil.close(rs, pstm, con);
        }
        BlackListStruct[] result = new BlackListStruct[data.size()];
        result = data.toArray(result);
        return result;
    }
    public boolean insertBlackList(SWRunTableStruct data, int type) {
        boolean noerror = true;
        Connection con = null;
        PreparedStatement pstm = null;
        //System.out.println(DBConfig.INSERTDNSTABLE);
        try {
            con = DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement(DBConfig.INSERTBLACKLIST);
            pstm.setString(1, data.Name);
            pstm.setString(2, data.Path);
            pstm.setString(3, data.Parametes);
            pstm.setString(4, data.Type);
            pstm.setInt(5, type);
            pstm.execute();
            SQLUtil.close(pstm, con);
        } catch (SQLException e) {
            e.printStackTrace();
            noerror = false;
        } finally {
            SQLUtil.close(pstm, con);
        }
        return noerror;
    }
    public boolean insertBlackList(GrayListStruct data, int type) {
        boolean noerror = true;
        Connection con = null;
        PreparedStatement pstm = null;
        //System.out.println(DBConfig.INSERTDNSTABLE);
        try {
            con = DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement(DBConfig.INSERTBLACKLIST);
            pstm.setString(1, data.Name);
            pstm.setString(2, data.Path);
            pstm.setString(3, data.Parametes);
            pstm.setString(4, data.Type);
            pstm.setInt(5, type);
            pstm.execute();
            SQLUtil.close(pstm, con);
        } catch (SQLException e) {
            e.printStackTrace();
            noerror = false;
        } finally {
            SQLUtil.close(pstm, con);
        }
        return noerror;
    }
    public boolean clearBlackList() {
        return clearTable("project", "blacklist");
    }
    public boolean clearIPList() {
        return clearTable("project", "iptable");
    }
    public boolean clearDNSTable() {
        return clearTable("project", "dnstable");
    }
    public boolean dropDataBase(String dbname) {
        return execSQL(String.format("DROP DATABASE `%s`;", dbname));
    }
    public boolean updateBlackList(int type, int no) {
        //String sql = String.format(DBConfig.UPDATEBLACKLIST, type, no);
        return execSQL(String.format(DBConfig.UPDATEBLACKLIST, type, no));
    }
    public boolean removeBlackList(int no) {
        //String sql = String.format(DBConfig.REMOVEBLACKLIST, no);
        return execSQL(String.format(DBConfig.REMOVEBLACKLIST, no));
    }
    public void dropALLTable() {
        String[] ipList = DBFunction.getInstance().GetAllIPList();
        for (int i = 0 ; i < ipList.length ; i ++) {
            DBFunction.getInstance().dropDataBase(ipList[i]);
        }
        DBFunction.getInstance().clearDNSTable();
        DBFunction.getInstance().clearIPList();
    }
    public FlowGroup[] getFlowGroups(String ip) {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        ArrayList<FlowGroup> data = new ArrayList<FlowGroup>();
        String sql = "SELECT * FROM (SELECT flow.DstAddr, flow.DstPort, Count(flow.DstPort) as `count`, Sum(flow.dOctets) as `sum` FROM flow WHERE flow.SrcAddr =  '%s' AND flow.SrcPort <>  '161' AND flow.DstPort <>  '162'  GROUP BY flow.DstAddr, flow.DstPort) as `result` WHERE  `result`.`count` >= '2';";
        sql = "SELECT * FROM (SELECT `flow`.`SrcAddr`, `flow`.`DstAddr`, `flow`.`dPkts`, `flow`.`dOctets`, `flow`.`SrcPort`, `flow`.`DstPort`, Count(`flow`.`DstPort`) as count FROM `%s`.`flow` WHERE `flow`.`SrcAddr` = '%s' AND `flow`.`SrcPort` <> '161' AND `flow`.`DstPort` <> '162' GROUP BY `flow`.`DstAddr`, `flow`.`DstPort` ORDER BY `flow`.`DstAddr` ASC, `flow`.`DstPort` ASC) AS `result` WHERE `result`.`count` >= %d;";
        sql = DBConfig.NETFLOWGROUP;
        sql = String.format(sql, ip, ip, Config.GROUPCOUNT);
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
            }
            SQLUtil.close(rs, pstm, con);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SQLUtil.close(rs, pstm, con);
        }
        FlowGroup[] result = new FlowGroup[data.size()];
        result = data.toArray(result);
        return result;
    }
    public FlowGroup[] getFlowGroups(String ip, long start, long end) {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        ArrayList<FlowGroup> data = new ArrayList<FlowGroup>();
        String sql = "SELECT * FROM (SELECT flow.DstAddr, flow.DstPort, Count(flow.DstPort) as `count`, Sum(flow.dOctets) as `sum` FROM flow WHERE flow.SrcAddr =  '%s' AND flow.SrcPort <>  '161' AND flow.DstPort <>  '162'  GROUP BY flow.DstAddr, flow.DstPort) as `result` WHERE  `result`.`count` >= '2';";
        // `flow`.`Stamp` >= ? AND `flow`.`Stamp` <= ?
        sql = "SELECT * FROM (SELECT `flow`.`SrcAddr`, `flow`.`DstAddr`, `flow`.`dPkts`, `flow`.`dOctets`, `flow`.`SrcPort`, `flow`.`DstPort`, Count(`flow`.`DstPort`) as count FROM `%s`.`flow` WHERE `flow`.`SrcAddr` = '%s' AND `flow`.`SrcPort` <> '161' AND `flow`.`DstPort` <> '162' `flow`.`Stamp` >= ? AND `flow`.`Stamp` <= ? GROUP BY `flow`.`DstAddr`, `flow`.`DstPort` ORDER BY `flow`.`DstAddr` ASC, `flow`.`DstPort` ASC) AS `result` WHERE `result`.`count` >= %d;";
        sql = DBConfig.NETFLOWGRAYGROUP;
        sql = String.format(sql, ip, ip, Config.GROUPCOUNT);
        try {
            con = DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement(sql);
            pstm.setTimestamp(1, new Timestamp(start));
            pstm.setTimestamp(2, new Timestamp(end));
            rs = pstm.executeQuery();
            while (rs.next()) {
                FlowGroup set = new FlowGroup();
                set.ip = rs.getString("DstAddr");
                set.port = rs.getInt("DstPort");
                set.count = rs.getInt("count");
                data.add(set);
            }
            SQLUtil.close(rs, pstm, con);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SQLUtil.close(rs, pstm, con);
        }
        FlowGroup[] result = new FlowGroup[data.size()];
        result = data.toArray(result);
        return result;
    }
    public DataStruct[] getFlowsData(String ip, String dstip, int port, boolean isNotify) {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        String sql = "SELECT `flow`.`SrcAddr`, `flow`.`DstAddr`, `flow`.`dPkts`, `flow`.`dOctets`, `flow`.`SrcPort`, `flow`.`DstPort`, Count(`flow`.`DstPort`) as count FROM `%s`.`flow` WHERE `flow`.`SrcAddr` = '%s' GROUP BY `flow`.`DstAddr`, `flow`.`DstPort` ORDER BY `flow`.`DstAddr` ASC, `flow`.`DstPort` ASC;";
        //sql = "SELECT * FROM (SELECT `flow`.`SrcAddr`, `flow`.`DstAddr`, `flow`.`dPkts`, `flow`.`dOctets`, `flow`.`SrcPort`, `flow`.`DstPort`, Count(`flow`.`DstPort`) as count FROM `%s`.`flow` WHERE `flow`.`SrcAddr` = '%s' GROUP BY `flow`.`DstAddr`, `flow`.`DstPort` ORDER BY `flow`.`DstAddr` ASC, `flow`.`DstPort` ASC) AS `result` WHERE `result`.`count` >= 2;";
        sql = "SELECT `flow`.`dOctets`, `flow`.`Stamp`, `flow`.`aFirst`, `flow`.`dPkts` FROM `%s`.`flow` WHERE flow.DstPort = '%d' AND flow.DstAddr = '%s' ORDER BY flow.aFirst ASC";
        sql = DBConfig.NETFLOWDATASET;
        sql = String.format(sql, ip, port, dstip);
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
        if (gcd == 0) {
            if (list.size() > 100) {
                
            }
            return null;
        }
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
        //count = (int) Math.round(Math.pow(2, count));
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
            } else {
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
            if (isNotify) {
                // TODO event 通知管理者 懷疑掃描網路或攻擊
                SendMail.getInstance().sendMail(String.format("%s\n%d\nScan", dstip, port), StaticManager.SCAN_DETECTED, StaticManager.OPTION_WARNING);
                ExcelUtil.writeExcel(ip,dstip,port,result,true,ProcessFFT.processFFT(result, dstip, port, true));
            }
            return null;
        }
        if (tmp == result.length || tmp <= 3) {
            return null;
        }
        if (gcd != 1) {
            System.out.println(String.format("gcd %5d count %5d end %5d", gcd, count, size));
        }
        return result;
    }
}
