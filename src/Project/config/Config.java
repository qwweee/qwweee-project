/**
 * 
 */
package Project.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import Project.utils.StreamUtil;

/**
 * 讀取設定檔內資料
 * 
 * @author 怪叔叔
 *
 */
public final class Config {
	/**
	 * 資料庫連線驅動
	 */
	public static String DBDriver;
	/**
	 * 資料庫連線連結
	 */
	public static String DBURL;
	/**
	 * 資料庫連線使用者
	 */
	public static String DBUser;
	/**
	 * 資料庫連線密碼
	 */
	public static String DBPassword;
	/**
	 * DB pool 最大的連線數
	 */
	public static int DBMaxCon;
    /**
     * 處理結束的Thread的Queue大小
     */
    public static int ZOMBIE_QUEUE_SIZE;
    /**
     * 處理Netflow的Queue大小
     */
    public static int NETFLOW_QUEUE_SIZE;
    /**
     * 所有電腦SNMP的密碼預設值
     */
    public static String COMMUNITY;
    /**
     * SNMP tcpConnTable的oid
     */
    public static String TCPCONNECT;
    /**
     * SNMP hrSWRunTable的oid
     */
    public static String SWRUNTABLE;
    /**
     * SNMP sysUpTime的oid
     */
    public static String SYSUPTIME;
    /**
     * SnmpTrap的port
     */
    public static int TRAPPORT;
    /**
     * Netflow傳送至本機的port
     */
    public static int NETFLOWPORT;
    /**
     * Netflow接收資料的buffer大小
     */
    public static int NETFLOWBUFFER;
    /**
     * 開機檢測時間<p>
     * 單位: 分鐘(m) 5
     */
    public static int BOOT_DETECT_RANGE;
    /**
     * 開機後檢測時間<p>
     * 單位: 分鐘(m) 5
     */
    public static int OTHER_DETECT_RANGE;
    /**
     * 開機時每隔多久時間抓取SNMP資訊<p>
     * 單位: 秒鐘(s) 1
     */
    public static int PER_BOOT_DETECT_TIME;
    /**
     * 開機後每隔多久時間抓取SNMP資訊<p>
     * 單位: 秒鐘(s) 5
     */
    public static int PER_OTHER_DETECT_TIME;
    /**
     * dns反查的時間，開機後多久<p>
     * 單位:分鐘(m) 5
     */
    public static int DNS_ANALYSIS_TIME;
    /**
     * Smtp send mail port
     */
    public static int SMTPPORT;
    /**
     * Smtp send mail server address
     */
    public static String SMTPSERVER;
    /**
     * sender mail adderss
     */
    public static String SENDERMAIL;
    /**
     * manager mail address
     */
    public static String MANAGERMAIL;
    /**
     * apache的位址
     */
    public static String APACHE_PATH;
    /**
     * 存儲圖片的寬 (810)
     */
    public static int IMAGEWIDTH;
    /**
     * 儲存圖片的高 (500)
     */
    public static int IMAGEHEIGHT;
    /**
     * 放入Excel的寬/欄 (20)
     */
    public static int EXCELWIDTH;
    /**
     * 放入Excel的高/列 (24)
     */
    public static int EXCELHEIGHT;
    /**
     * 一單位中掃描次數標準 (10)
     */
    public static int SCANCOUNT;
    /**
     * 檢測FFT分析對稱相似率 (0.8)
     */
    public static double SAMERATE;
    /**
     * NetFlow檢測最少要有的資料數 (8)
     */
    public static int GROUPCOUNT;
    /**
     * SNMP重試次數 (1)
     */
    public static int RETRYTIMES;
    /**
     * 是否偵測到關機
     */
    public static boolean ALLTIMEDETECT;
    /**
     * 設定檔的位置
     */
    public static final String CONFIG_FILE = "./config/config.properties";
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
            /**
             * port setup
             */
            TRAPPORT = Integer.parseInt(settings.getProperty("TrapPort", "162"));
            NETFLOWPORT = Integer.parseInt(settings.getProperty("NetflowPort", "9991"));
            /**
             * time setup
             */
            BOOT_DETECT_RANGE = Integer.parseInt(settings.getProperty("Boot_detect_range", "10"));
            OTHER_DETECT_RANGE = Integer.parseInt(settings.getProperty("Other_detect_range", "10"));
            PER_BOOT_DETECT_TIME = Integer.parseInt(settings.getProperty("Per_boot_detect_time", "5"));
            PER_OTHER_DETECT_TIME = Integer.parseInt(settings.getProperty("Per_other_detect_time", "5"));
            DNS_ANALYSIS_TIME = Integer.parseInt(settings.getProperty("DNS_analysis_time", "15"));
            /**
             * db setup
             */
            DBDriver = settings.getProperty("Driver", "com.mysql.jdbc.Driver");
            DBURL = settings.getProperty("URL", "jdbc:mysql://localhost/project?useUnicode=true&characterEncoding=utf8");
            DBUser = settings.getProperty("Login", "root");
            DBPassword = settings.getProperty("Password", "");
            DBMaxCon = Integer.parseInt(settings.getProperty("MaxConnection", "100"));
            /**
             * queue size setup
             */
            ZOMBIE_QUEUE_SIZE = Integer.parseInt(settings.getProperty("Zombie_Queue_Size", "20"));
            NETFLOW_QUEUE_SIZE = Integer.parseInt(settings.getProperty("Netflow_Queue_Size", "100"));
            /**
             * others setup
             */
            NETFLOWBUFFER = Integer.parseInt(settings.getProperty("NetflowBuffer", "10240"));
            APACHE_PATH = settings.getProperty("ApachePath", "/var/www/html/detect/");
            RETRYTIMES = Integer.parseInt(settings.getProperty("RetryTimes", "1"));
            ALLTIMEDETECT = Boolean.parseBoolean(settings.getProperty("AllTimeDetect", "false"));
            /**
             * mail setup
             */
            SMTPPORT = Integer.parseInt(settings.getProperty("SmtpPort", "25"));
            SMTPSERVER = settings.getProperty("SmtpServer", "smtp.ncnu.edu.tw");
            SENDERMAIL = settings.getProperty("SenderMail", "cnclab@im.ncnu.edu.tw");
            MANAGERMAIL = settings.getProperty("ManagerMail", "s97213521@ncnu.edu.tw");
            /**
             * Image config
             */
            IMAGEWIDTH = Integer.parseInt(settings.getProperty("ImageWidth", "680"));
            IMAGEHEIGHT = Integer.parseInt(settings.getProperty("ImageHeight", "420"));
            /**
             * Excel image config
             */
            EXCELWIDTH = Integer.parseInt(settings.getProperty("ExcelWidth", "20"));
            EXCELHEIGHT = Integer.parseInt(settings.getProperty("ExcelHeight", "22"));
            /**
             * FFT config
             */
            SAMERATE = Double.parseDouble(settings.getProperty("SameRate", "0.8"));
            SCANCOUNT = Integer.parseInt(settings.getProperty("ScanCount", "10"));
            GROUPCOUNT = Integer.parseInt(settings.getProperty("GroupCount", "8"));
            //variable = settings.getProperty("", "");
            /**
             * default setup
             */
            TCPCONNECT = "tcpConnTable";
            SWRUNTABLE = "hrSWRunTable";
            SYSUPTIME = "system.sysUpTime.0";
            COMMUNITY = "public";
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
