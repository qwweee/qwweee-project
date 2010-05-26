/**
 * 
 */
package Project.struct;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author 怪叔叔
 *
 */
public final class Header {
    /**
     * SNMP hrSWRunTable資訊的欄位
     */
    public final static String[] hrSWRunTable = {"hrSWRunIndex","hrSWRunName","hrSWRunID","hrSWRunPath","hrSWRunParameters","hrSWRunType","hrSWRunStatus"};
    /**
     * SNMP tcpConnTable資訊的欄位
     */
    public final static String[] ConnectTable = {"tcpConnState","tcpConnLocalAddress","tcpConnLocalPort","tcpConnRemAddress","tcpConnRemPort"};
    /**
     * 存日期的資料格式
     */
    public final static SimpleDateFormat timeformat = new SimpleDateFormat("yyyy/MM/dd a h:mm:ss (E)", Locale.TAIWAN);
}
