/**
 * 
 */
package Project.mainThread.snmp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import Project.config.Config;

import com.adventnet.snmp.beans.DataException;
import com.adventnet.snmp.beans.SnmpTable;
import com.adventnet.snmp.beans.SnmpTableEvent;
import com.adventnet.snmp.beans.SnmpTableListener;
import com.adventnet.snmp.mibs.MibException;

/**
 * SNMP抓取Table資料
 * 
 * @author 怪叔叔
 *
 */
public class SnmpGetTable {
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
     * SNMP mib host的資料格式
     */
    private static String HOSTMIB = "./mib/HR-MIB";
    /**
     * SNMP table抓取資料的物件
     */
    private SnmpTable table;
    private String oid;
    private SnmpTableListener listener;
    /**
     * 
     * @param community SNMP通訊密碼
     * @param host SNMP主機IP
     */
    public SnmpGetTable(String host, String community, String oid) {
        this.host = host;
        this.community = community;
        this.oid = oid;
        this.listener = new SnmpTableListener() {
            @Override
            public void tableChanged(SnmpTableEvent event) {
            }
        };
        init();
    }
    public SnmpGetTable(String host, String community, String oid, SnmpTableListener listener) {
        this.host = host;
        this.community = community;
        this.oid = oid;
        this.listener = listener;
        init();
    }
    /**
     * 初始化所有物件，將mib資料load到SnmpTable中
     *
     */
    private void init() {
        table = new SnmpTable();
        //table.setLoadFromCompiledMibs(true);
        table.setTargetHost(host);
        table.setCommunity(community);
        try {
            table.loadMibs(SnmpGetTable.RFC1213MIB);
            table.loadMibs(SnmpGetTable.HOSTMIB);
            table.setTableOID(oid);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (MibException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (DataException e) {
            e.printStackTrace();
        }
        table.setPollInterval(Config.PER_BOOT_DETECT_TIME);
    }
    public void addListener() {
        try {
            InetAddress address = InetAddress.getByName(host);
            while (true) {
                if (address.isReachable(Config.PER_BOOT_DETECT_TIME)) {
                    break;
                }
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        table.addSnmpTableListener(listener);
    }
    public void rmListener() {
        table.removeSnmpTableListener(listener);
    }
}
