package Project.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import Project.utils.StreamUtil;

/**
 * DB讀取設定檔物件
 * @author bbxp
 * TODO z done db 重新規劃存入資料庫格式 SWTable TCPTable (UDPTable)
 */
public final class DBConfig {
	/**
     * 設定檔的位置
     */
    public static final String CONFIG_FILE = "./config/database.properties";
	/**
     * 讀取設定檔內的設定，會有FileNotFoundException
     * 
     * @see java.io.FileNotFoundException
     * @see java.io.IOException
     *
     */
    public static void Load() {
        InputStream is = null;
        try {
            Properties settings = new Properties();
            is = new FileInputStream(new File(CONFIG_FILE));
            settings.load(is);
            StreamUtil.close(is);
            // TODO db SQL語法
            // PROJECT DB
            CREATEDB = settings.getProperty("CreateDB", "CREATE DATABASE `%s`;");
            SEARCHIPTABLE = settings.getProperty("SearchIPTable", "SELECT * FROM `project`.`iptable` WHERE ip = '%s';"); 
            INSERTIPTABLE = settings.getProperty("InsertIPTable", "INSERT INTO `iptable` (`id`,`ip`,`update`) VALUES (NULL,?,?);");
            UPDATEIPTABLE = settings.getProperty("UpdateIPTable", "UPDATE `iptable` SET `update`=? WHERE (`ip`=?);");
            SEARCHDNSTABLE = settings.getProperty("SearchDNSTable", "SELECT `ip`, `status`, `time` FROM `project`.`dnstable` WHERE dns = '%s';");
            INSERTDNSTABLE = settings.getProperty("InsertDNSTable", "INSERT INTO `dnstable` (`no`,`ip`,`dns`,`status`,`time`) VALUES (NULL,?,?,?,?);");
            UPDATEDNSTABLE = settings.getProperty("UpdateDNSTable", "UPDATE `dnstable` SET `status`=?,`time`=? WHERE `ip` = ?;");
            // HOST PROJECT
            CREATEFLOWTABLE = settings.getProperty("CreateFlowTable", "CREATE TABLE `%s`.`flow` (  `RouterIP` varchar(15) NOT NULL,  `SysUptime` bigint(20) DEFAULT NULL,  `Secs` bigint(20) DEFAULT NULL,  `Nsecs` bigint(20) DEFAULT NULL,  `Flow_Sequence` bigint(20) NOT NULL,  `Engine_Type` int(11) NOT NULL,  `Engine_ID` int(11) NOT NULL,  `SrcAddr` varchar(15) NOT NULL,  `DstAddr` varchar(15) NOT NULL,  `NextHop` varchar(15) NOT NULL,  `Input` int(11) NOT NULL,  `Output` int(11) NOT NULL,  `dPkts` bigint(20) DEFAULT NULL,  `dOctets` bigint(20) DEFAULT NULL,  `aFirst` bigint(20) DEFAULT NULL,  `aLast` bigint(20) DEFAULT NULL,  `SrcPort` int(11) NOT NULL,  `DstPort` int(11) NOT NULL,  `Tcp_Flags` int(11) NOT NULL,  `Prot` int(11) NOT NULL,  `TOS` int(11) NOT NULL,  `Src_As` int(11) NOT NULL,  `Dst_As` int(11) NOT NULL,  `Src_Mask` int(11) NOT NULL,  `Dst_Mask` int(11) NOT NULL,  `Stamp` timestamp NULL DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=utf8;");
            CREATESWTABLE = settings.getProperty("CreateSWTable", "CREATE TABLE `%s`.`swtable` (  `index` int(10) NOT NULL,  `name` varchar(100) NOT NULL,  `id` varchar(10) NOT NULL,  `path` text NOT NULL,  `parameters` text NOT NULL,  `type` varchar(20) NOT NULL,  `status` varchar(15) NOT NULL,  `start` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,  `end` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',  `map` text NOT NULL,  PRIMARY KEY (`index`,`start`)) ENGINE=InnoDB DEFAULT CHARSET=utf8;");
            CREATETCPTABLE = settings.getProperty("CreateTCPTable", "CREATE TABLE `%s`.`tcptable` (  `localaddr` varchar(15) NOT NULL,  `localport` int(5) NOT NULL,  `remaddr` varchar(15) NOT NULL,  `remport` int(5) NOT NULL,  `status` varchar(15) NOT NULL,  `start` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,  `end` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',  `map` text NOT NULL,  PRIMARY KEY (`localaddr`,`localport`,`remaddr`,`remport`,`start`)) ENGINE=InnoDB DEFAULT CHARSET=utf8;");
            INSERTFLOWTABLE = settings.getProperty("InsertFlowTable", "INSERT INTO `%s`.`flow` ( RouterIP, SysUptime, Secs, Nsecs, Flow_Sequence, Engine_Type, Engine_ID, SrcAddr, DstAddr, NextHop, Input, Output, dPkts, dOctets, aFirst, aLast, SrcPort, DstPort, Tcp_Flags, Prot, TOS, Src_As, Dst_As, Src_Mask, Dst_Mask ,Stamp) VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,?);");
            INSERTSWTABLE = settings.getProperty("InsertSWTable", "INSERT INTO `%s`.`swtable` (`index`,`name`,`id`,`path`,`parameters`,`type`,`status`,`start`,`end`,`count`) VALUES (?,?,?,?,?,?,?,?,?,?);");
            INSERTTCPTABLE = settings.getProperty("InsertTCPTable", "INSERT INTO `%s`.`tcptable` (`localaddr`,`localport`,`remaddr`,`remport`,`status`,`start`,`end`,`map`) VALUES ('?','?','?','?','?','?','?','?');");
            //CREATEDB = settings.getProperty("", "");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Config error");
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Config error");
            System.exit(1);
        } finally {
            StreamUtil.close(is);
        }
    }
    public static String CREATEDB;
    public static String CREATEFLOWTABLE;
    public static String CREATESWTABLE;
    public static String CREATETCPTABLE;
    
    public static String SEARCHIPTABLE;
    public static String SEARCHDNSTABLE;
    
    public static String INSERTIPTABLE;
    public static String INSERTDNSTABLE;
    public static String INSERTFLOWTABLE;
    public static String INSERTSWTABLE;
    public static String INSERTTCPTABLE;
    
    public static String UPDATEIPTABLE;
    public static String UPDATEDNSTABLE;
}
