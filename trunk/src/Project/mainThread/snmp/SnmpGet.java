package Project.mainThread.snmp;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.adventnet.snmp.beans.SnmpTarget;
import com.adventnet.snmp.mibs.MibException;

/**
 * @author bbxp
 *
 */
public class SnmpGet {
    /**
     * SNMP通訊密碼
     */
    private String community;
    /**
     * SNMP主機IP
     */
    private String host;
    /**
     * SNMP mib rfc1213的資料格式
     */
    private static String RFC1213MIB = "./mib/RFC1213-MIB";
    /**
     * SNMP GET 要抓的物件
     */
    private SnmpTarget target;
    /**
     * @param host
     * @param community
     */
    public SnmpGet(String host, String community) {
        this.community = community;
        this.host = host;
        init();
    }
    private void init() {
        target = new SnmpTarget();
        target.setTargetHost(host);
        target.setCommunity(community);
        try {
            target.loadMibs(SnmpGet.RFC1213MIB);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (MibException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    /**
     * @param oid
     * @return
     */
    public String getValue(String oid) {
        target.setObjectID(oid);
        String result = target.snmpGet();
        return result;
    }
}
