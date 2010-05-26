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
 * TODO db 重新規劃存入資料庫格式 SWTable TCPTable (UDPTable)
 */
public final class DBConfig {
	/**
	 * 建立新的資料庫
	 */
	public static String CREATEDB;
	/**
	 * 搜尋在IPTABLE內是否有資料
	 */
	public static String SEARCHIPTABLE;
	/**
	 * 搜尋在DNSTABLE內是否有相對應的IP和DNS
	 */
	public static String SEARCHDNSTABLE;
	/**
	 * 更新IPTABLE內IP的更新時間
	 */
	public static String UPDATEIPTABLE;
	/**
	 * 更新DNSTABLE內status和時間
	 */
	public static String UPDATEDNSTABLE;
	/**
	 * 插入一筆IP資料到IPTABLE資料表內
	 */
	public static String INSERTIPTABLE;
	/**
	 * 建立software table的資料表
	 */
	public static String CREATESWTABLE;
	/**
	 * 建立TCP connection table的資料表
	 */
	public static String CREATETCPTABLE;
	/**
	 * 建立TCP connection table的資料表
	 */
	public static String CREATEUDPTABLE;
    /**
     * 建立NetFlow table的資料表
     */
    public static String CREATEFLOWTABLE;
	/**
	 * 插入一筆TCP connection資料到tcp table內
	 */
	public static String INSERTTCPTABLE;
	/**
     * 插入一筆UDP資料到udp table內
     */
    public static String INSERTUDPTABLE;
    /**
     * 插入一筆software資料到software table內
     */
    public static String INSERTSWTABLE;
    /**
     * 插入一筆Netflow資料到flow table內
     */
    public static String INSERTFLOWTABLE;
    /**
     * 插入一筆DNS IP對應資料到dns table內
     */
    public static String INSERTDNSTABLE;
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
            is.close();
            CREATEDB = settings.getProperty("CreateDB", "CREATE DATABASE ? ENCODING utf8;");
            SEARCHIPTABLE = settings.getProperty("SearchIPTable", "SELECT * FROM `project`.`iptable` WHERE ip = ?;");
            SEARCHDNSTABLE = settings.getProperty("SearchDNSTable", "SELECT `ip`, `status`, `time` FROM `project`.`dnstable` WHERE dns = ?;");
            UPDATEIPTABLE = settings.getProperty("UpdateIPTable", "UPDATE `iptable` SET `update`=? WHERE (`ip`=?) ;");
            UPDATEDNSTABLE = settings.getProperty("UpdateDNSTable", "UPDATE `dnstable` SET `status`=?,`time`=? WHERE ip = ?;");
            INSERTIPTABLE = settings.getProperty("InsertIPTable", "INSERT INTO `iptable` (`id`,`ip`,`update`) VALUES (NULL,?,?);");
            CREATESWTABLE = settings.getProperty("CreateSWTable", "`swtable` (  `index` int(10) NOT NULL,  `name` varchar(100) NOT NULL,  `id` varchar(10) NOT NULL,  `path` text NOT NULL,  `parameters` text NOT NULL,  `type` varchar(20) NOT NULL,  `status` varchar(15) NOT NULL,  `start` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,  `end` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',  `count` int(2) NOT NULL,  PRIMARY KEY (`index`,`start`)) ENGINE=InnoDB DEFAULT CHARSET=utf8;");
            CREATETCPTABLE = settings.getProperty("CreateTCPTable", "`tcptable` (  `localaddr` varchar(15) NOT NULL,  `localport` int(5) NOT NULL,  `remaddr` varchar(15) NOT NULL,  `remport` int(5) NOT NULL,  `status` varchar(15) NOT NULL,  `start` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,  `end` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',  `count` int(2) NOT NULL,  PRIMARY KEY (`localaddr`,`localport`,`remaddr`,`remport`,`start`)) ENGINE=InnoDB DEFAULT CHARSET=utf8;");
            CREATEUDPTABLE = settings.getProperty("CreateUDPTable", "`udptable` (  `localaddr` varchar(15) NOT NULL,  `localport` int(5) NOT NULL,  `start` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,  `end` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',  `count` int(2) NOT NULL,  PRIMARY KEY (`localaddr`,`localport`,`start`)) ENGINE=InnoDB DEFAULT CHARSET=utf8;");
            CREATEFLOWTABLE = settings.getProperty("CreateFlowTable", "`flow` ( RouterIP VARCHAR(15) NOT NULL, SysUptime BIGINT,  Secs BIGINT, Nsecs BIGINT, Flow_Sequence BIGINT NOT NULL, Engine_Type INT NOT NULL, Engine_ID INT NOT NULL, SrcAddr VARCHAR(15) NOT NULL, DstAddr VARCHAR(15) NOT NULL, NextHop VARCHAR(15) NOT NULL, Input INT NOT NULL, Output INT NOT NULL, dPkts BIGINT, dOctets BIGINT, aFirst BIGINT, aLast BIGINT, SrcPort INT NOT NULL, DstPort INT NOT NULL, Tcp_Flags INT NOT NULL, Prot INT NOT NULL, TOS INT NOT NULL, Src_As INT NOT NULL, Dst_As INT NOT NULL, Src_Mask INT NOT NULL, Dst_Mask INT NOT NULL, Stamp timestamp NULL );");
            INSERTTCPTABLE = settings.getProperty("InsertTCPTable", "`tcptable` (`localaddr`,`localport`,`remaddr`,`remport`,`status`,`start`,`end`,`count`) VALUES (?,?,?,?,?,?,?,?);");
            INSERTUDPTABLE = settings.getProperty("InsertUDPTable", "`udptable` (`localaddr`,`localport`,`start`,`end`,`count`) VALUES (?,?,?,?,?);");
            INSERTSWTABLE = settings.getProperty("InsertSWTable", "`swtable` (`index`,`name`,`id`,`path`,`parameters`,`type`,`status`,`start`,`end`,`count`) VALUES (?,?,?,?,?,?,?,?,?,?);");
            INSERTFLOWTABLE = settings.getProperty("InsertFlowTable", "`flow` ( RouterIP, SysUptime, Secs, Nsecs, Flow_Sequence, Engine_Type, Engine_ID, SrcAddr, DstAddr, NextHop, Input, Output, dPkts, dOctets, aFirst, aLast, SrcPort, DstPort, Tcp_Flags, Prot, TOS, Src_As, Dst_As, Src_Mask, Dst_Mask ,Stamp) VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,?);");
            INSERTDNSTABLE = settings.getProperty("InsertDNSTable", "INSERT INTO `dnstable` (`no`,`ip`,`dns`,`status`,`time`) VALUES (NULL,?,?,?,?);");
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
}
