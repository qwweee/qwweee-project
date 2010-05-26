package Project.mainThread.snmp.task;

import java.io.IOException;

import Project.StaticManager;
import Project.config.Config;
import Project.html.HTML;
import Project.struct.DetectSet;
import Project.struct.TCPConnectStruct;

import com.adventnet.snmp.beans.SnmpTable;
import com.adventnet.snmp.beans.SnmpTableEvent;
import com.adventnet.snmp.beans.SnmpTableListener;

public class TaskTCPListener implements SnmpTableListener{
    private DetectSet host;
    private boolean isBoot;
    private int mapcount;
    private int mapsize;
    private int count;
    public TaskTCPListener(DetectSet host) {
        this.host = host;
        this.isBoot = true;
        this.mapcount = 0;
        this.mapsize = 0;
        this.count = 0;
    }
    private synchronized void processData(SnmpTable table) {
        for (int i=0;i<table.getRowCount();i++) {
            table.setDataType(SnmpTable.STRING_DATA);
            String key = table.getValueAt(i,1).toString()+table.getValueAt(i,2).toString()+
                         table.getValueAt(i,3).toString()+table.getValueAt(i,4).toString();
            if (host.tcp.containsKey(key)) {
                TCPConnectStruct tcp = host.tcp.get(key);
                tcp.map[mapcount] = 1;
                //sw.print();
            } else {
                TCPConnectStruct value = new TCPConnectStruct();
                value.setBoot(isBoot);
                mapsize = value.map.length;
                value.StartTime = System.currentTimeMillis();
                value.Status = table.getValueAt(i,0).toString();
                value.LocalAddress = table.getValueAt(i,1).toString();
                value.LocalPort = Integer.parseInt(table.getValueAt(i,2).toString());
                value.RemoteAddress = table.getValueAt(i,3).toString();
                value.RemotePort = Integer.parseInt(table.getValueAt(i,4).toString());
                value.map[mapcount] = 1;
                host.tcp.put(key, value);
                //value.print();
            }
        }
        count++;
        mapcount++;
        if (mapcount == mapsize) {
            writeFile();
            writeDB();
        }
        // TODO z done 開機後檢測時間結束
        if (count == Config.BOOT_DETECT_RANGE*60/Config.PER_BOOT_DETECT_TIME && isBoot){
            this.isBoot = false;
            System.out.println(host.ip+"\tTCP開機時間檢測完畢\t" + mapcount + "/" + mapsize);
            StaticManager.printDate(System.currentTimeMillis());
            table.setPollInterval(Config.PER_OTHER_DETECT_TIME);
            count = 0;
            mapsize = 0;
        }
        // TODO z done 其他檢測時間結束 關閉Thread (snmp)
        if (count == Config.OTHER_DETECT_RANGE*60/Config.PER_OTHER_DETECT_TIME && !isBoot) {
            System.out.println(host.ip+"\tTCP其他時間檢測完畢\t" + mapcount + "/" + mapsize);
            StaticManager.printDate(System.currentTimeMillis());
            count = 0;
            stopListener(table);
        }
    }
    private void stopListener(SnmpTable table) {
        table.stopPollingTable();
        table.removeSnmpTableListener(this);
        writeFile();
        writeDB();
        System.gc();
    }
    private void writeFile() {
        if (host.tcp.size() == 0) {
            return;
        }
        try {
            HTML.ToFileTCP(host, host.ip, isBoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mapcount = 0;
        mapsize = 0;
        host.clearTCPHash();
    }
 // TODO db 寫入db內的tcp table
    private void writeDB() {
        if (host.tcp.size() == 0) {
            return;
        }
    }
    @Override
    public void tableChanged(SnmpTableEvent event) {
        SnmpTable table = (SnmpTable)event.getSource();
        if (event.isEndOfTable()) { // 最後抓到資料的時候
            processData(table);
        }
        if( event.isEndOfTable() || event.getType() == 2){
            if (table.getRowCount() == 0) { // no rows and we're being notified 關機時
                System.err.println(table.getErrorString());
                host.setShutdown();
                stopListener(table);
            }
            return;
        }
    }
}