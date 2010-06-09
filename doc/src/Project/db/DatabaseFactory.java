package Project.db;

import java.sql.Connection;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;
/**
 * DataBase connection 產生物件
 * @author bbxp
 *
 */
public class DatabaseFactory {
	/**
	 * DB原始資料
	 */
    private ComboPooledDataSource DBsource;
    /**
     * DB工廠
     */
    private static DatabaseFactory DBinstance;
    /**
     * DB所使用的driver
     */
    private static String DBdriver;
    /**
     * DB所指向的url
     */
    private static String DBurl;
    /**
     * DB使用者
     */
    private static String DBuser;
    /**
     * DB密碼
     */
    private static String DBpassword;
    /**
     * DB最大的連線數(pool大小)
     */
    private static int DBmaxCon;
    /**
     * 建構元，初始化所有物件以及設定其預設值
     * @throws SQLException
     */
    private DatabaseFactory() throws SQLException {
        try {
            DBsource = new ComboPooledDataSource();
            DBsource.setAutoCommitOnClose(true);
            DBsource.setInitialPoolSize(10);
            DBsource.setMinPoolSize(10);
            // if(DBMaxCon < 100 || DBMaxCon > 1000) //
            // DBMaxCon = 100;
            DBsource.setMaxPoolSize(DBmaxCon);
            DBsource.setAcquireRetryAttempts(0); // try to obtain connections
            // indefinitely (0 = never quit)
            DBsource.setAcquireRetryDelay(500); // 500 miliseconds wait before
            // try to acquire connection
            // again
            DBsource.setCheckoutTimeout(500); // 0 = wait indefinitely for new
            // connection
            // if pool is exhausted
            DBsource.setAcquireIncrement(5); // if pool is exhausted, get 5
            // more
            // connections at a time
            // cause there is a "long" delay on acquire connection
            // so taking more than one connection at once will make connection
            // pooling
            // more effective.
            // this "connection_test_table" is automatically created if not
            // already there
            DBsource.setAutomaticTestTable("connection_test_table");
            DBsource.setTestConnectionOnCheckin(false);
            // testing OnCheckin used with IdleConnectionTestPeriod is faster
            // than testing on checkout
            DBsource.setIdleConnectionTestPeriod(60); // test idle connection
            // every 60 sec
            DBsource.setMaxIdleTime(100); // 0 = idle connections never expire
            // *THANKS* to connection testing configured above
            // but I prefer to disconnect all connections not used
            // for more than 1 hour
            // enables statement caching, there is a "semi-bug" in c3p0 0.9.0
            // but in 0.9.0.2 and later it's fixed
            DBsource.setMaxStatementsPerConnection(100);
            DBsource.setBreakAfterAcquireFailure(false); // never fail if any
            // way
            // possible
            // setting this to true will make
            // c3p0 "crash" and refuse to work
            // till restart thus making acquire
            // errors "FATAL" ... we don't want that
            // it should be possible to recover
            DBsource.setDriverClass(DBdriver);
            DBsource.setJdbcUrl(DBurl);
            DBsource.setUser(DBuser);
            DBsource.setPassword(DBpassword);
            /* Test the connection */
            DBsource.getConnection().close();
        } catch (SQLException x) {
            System.out.println("Database Connection FAILED");
            // rethrow the exception
            throw x;
        } catch (Exception e) {
            System.out.println("Database Connection FAILED");
            throw new SQLException("could not init DB connection:" + e);
        }
    }
    /**
     * 設定要連DB的基本資料
     * @param driver 驅動
     * @param url 連線address
     * @param user 登入之使用者
     * @param password 登入之密碼
     * @param maxCon connection上限數
     */
    public static void setDatabaseSettings(String driver, String url,
            String user, String password, int maxCon) {
        DBdriver = driver;
        DBurl = url;
        DBuser = user;
        DBpassword = password;
        DBmaxCon = maxCon;
    }
    /**
     * 結束所有DB連線，釋放所有資源
     *
     */
    public void shutdown() {
        try {
            DBsource.close();
        } catch (Exception e) {
        }
        try {
            DBsource = null;
        } catch (Exception e) {
        }
    }
    /**
     * 取得設定好的DataBaseFactroy，外部呼叫初始化
     * @return DatabaseFactory 取得
     * @throws SQLException
     */
    public static DatabaseFactory getInstance() throws SQLException {
        if (DBinstance == null) {
            DBinstance = new DatabaseFactory();
        }
        return DBinstance;
    }
    /**
     * 取得可執行Query的connection
     * @return Connection
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
        Connection con = null;
        while (con == null) {
            try {
                con = DBsource.getConnection();
                con.setAutoCommit(false);
            } catch (SQLException e) {
            	throw new SQLException("could not get DB connection:" + e);
            }
        }
        return con;
    }
}
