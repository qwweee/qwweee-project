package Project.mainThread.snmp.task;

import java.io.IOException;
import java.util.ArrayList;

import Project.StaticManager;
import Project.config.Config;
import Project.db.DBFunction;
import Project.html.HTML;
import Project.struct.DetectSet;
import Project.struct.SWRunTableStruct;

import com.adventnet.snmp.beans.SnmpTable;
import com.adventnet.snmp.beans.SnmpTableEvent;
import com.adventnet.snmp.beans.SnmpTableListener;

public class TaskSWRunListener implements SnmpTableListener{
    private DetectSet host;
    private boolean isBoot;
    private int mapcount;
    private int mapsize;
    private int count;
    public TaskSWRunListener(DetectSet host) {
        this.host = host;
        this.isBoot = true;
        this.count = 0;
        this.mapcount = 0;
        this.mapsize = 0;
    }
    private synchronized void processData(SnmpTable table) {
        for (int i=0;i<table.getRowCount();i++) {
            table.setDataType(SnmpTable.STRING_DATA);
            String key = table.getValueAt(i,0).toString();
            if (host.sw.containsKey(key)) {
                SWRunTableStruct sw = host.sw.get(key);
                sw.map[mapcount] = 1;
                //sw.print();
            } else {
                SWRunTableStruct value = new SWRunTableStruct();
                value.setBoot(isBoot);
                mapsize = value.map.length;
                value.StartTime = System.currentTimeMillis();
                value.Index = Integer.parseInt(table.getValueAt(i,0).toString());
                value.Name = table.getValueAt(i,1).toString();
                value.ID = table.getValueAt(i,2).toString();
                value.Path = table.getValueAt(i,3).toString();
                value.Parameters = table.getValueAt(i,4).toString();
                value.Type = table.getValueAt(i,5).toString();
                value.Status = table.getValueAt(i,6).toString();
                value.map[mapcount] = 1;
                host.sw.put(key, value);
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
            System.out.println(host.ip+"\tSW開機時間檢測完畢\t" + mapcount + "/" + mapsize);
            StaticManager.printDate(System.currentTimeMillis());
            table.setPollInterval(Config.PER_OTHER_DETECT_TIME);
            count = 0;
        }
        // TODO z done 其他檢測時間結束 關閉Thread (snmp)
        if (count == Config.OTHER_DETECT_RANGE*60/Config.PER_OTHER_DETECT_TIME && !isBoot) {
            System.out.println(host.ip+"\tSW其他時間檢測完畢\t" + mapcount + "/" + mapsize);
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
        if (host.sw.size() == 0) {
            return;
        }
        try {
            HTML.ToFileSW(host, host.ip, isBoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mapsize = 0;
        mapcount = 0;
        host.clearSWHash();
    }
    // TODO z done db 寫入db內的swrun table
    private void writeDB() {
        if (host.sw.size() == 0) {
            return;
        }
        ArrayList<SWRunTableStruct> data = new ArrayList<SWRunTableStruct>();
        data.addAll(host.sw.values());
        for (int i = 0 ; i < data.size() ; i ++) {
            data.get(i).EndTime = System.currentTimeMillis();
            if (!DBFunction.getInstance().insertSWTable(host.ip, data.get(i))) {
                System.err.println("新增SWTable資料錯誤!");
            }
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