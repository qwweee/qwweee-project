/**
 * 
 */
package Project;

import java.awt.Component;
import java.awt.Font;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.ListModel;

import Project.config.Config;
import Project.db.DBFunction;
import Project.gui.List.ListCellRenderer;
import Project.gui.List.ListItem;
import Project.struct.BlackListStruct;
import Project.struct.DetectSet;
import Project.struct.SWRunTableStruct;
import Project.utils.Queue;

/**
 * @author bbxp
 *
 */
public class StaticManager {
    public final static int BLACKLIST = 0;
    public final static int WHITELIST = 1;
    public final static int GRAYLIST = 2;
    public final static int REMOVEITEM = 3;
    public final static int REMOVEALLBLACKLIST = -1;
    
    public final static int NOTBOOTLIST = 0;
    public final static int BOOTLIST = 1;
    public final static int IPLISTDIALOG = -1;
    
    public final static String OPTION_SEVERE = "嚴重";
    public final static String OPTION_WARNING = "警告";
    public final static String OPTION_NORMAL = "正常";
    public final static String OPTION_INFO = "訊息";
    
    public final static String FLOW_DETECTED = "偵測Flow分析";
    public final static String SCAN_DETECTED = "偵測掃描活動";
    public final static String BLACK_DETECTED = "偵測處理程序黑名單";
    public final static String DNS_DETECTED = "偵測DNS反查";
    public final static String ATTACK_DETECTED = "偵測攻擊活動";
    public final static String BLACKLIST_ADD = "加入黑名單";
    public final static String GRAYLIST_ADD = "加入灰名單";
    public final static String WHITELIST_ADD = "加入白名單";
    
    
    
    
    public final static String[] BLACKLISTTITLE = {"黑名單", "白名單", "灰名單"};
    /**
     * 存ip對應DataStruct
     * 
     * @see Project.struct.Structure.DataStruct
     */
    public static HashMap<String, DetectSet> IPList = new HashMap<String, DetectSet>();
    /**
     * 存ip對應DataStruct
     * 
     * @see Project.struct.Structure.DataStruct
     */
    public static HashMap<String, DetectSet> FlowList = new HashMap<String, DetectSet>();
    /**
     * 處理netflow packet的queue
     */
    public static Queue NETFLOW_QUEUE = new Queue(Config.NETFLOW_QUEUE_SIZE);
    /**
     * 所有Class都可以存取，用來將Zombie的Thread結束，Queue中存執行結束的Thread
     */
    public static Queue ZOMBIE_QUEUE = new Queue(Config.ZOMBIE_QUEUE_SIZE);
    public static void printDate(long time) {
        System.out.println(DateFormat.getTimeInstance().format(new Date(time)));
    }
    public static void threadInfo() {
        System.out.println(Thread.activeCount());
        Thread[] array = new Thread[Thread.activeCount()];
        Thread.enumerate(array);
        for (int i = 0 ; i < array.length ; i ++) {
            Thread t = array[i];
            Class<? extends Thread> c = array[i].getClass();
            System.out.println(String.format("%4s\t%s\t%s\t%s",t.isAlive(),t.getName(),c.getSimpleName(),t.getId()));
        }
    }
    public static final Font default_font = new Font("標楷體", Font.BOLD, 18);
    public static final Font menu_font = new Font("標楷體", Font.PLAIN, 18);
    /**
     * 要輸出的欄位標頭
     */
    public static final String[] TCPTitle = {"狀態", "本地端Address", "本地端Port", "目的端Address", "目的端Port", "開始時間", "map"};
    /**
     * 要輸出的欄位標頭
     */
    public static final String[] SWTitle = {"索引", "名稱", "ID", "路徑", "參數", "型態", "狀態", "開始時間", "map"};
    public static final ArrayList<BlackListStruct> BlackList = new ArrayList<BlackListStruct>() {
        public boolean equals(Object o) {
            for (Object data : this.toArray()) {
                if (data.equals(o)){
                    return true;
                }
            }
            return false;
        }
        public int indexOf(Object o) {
            for (int i = 0 ; i < this.toArray().length ; i ++) {
                if (this.toArray()[i].equals(o)) {
                    return i;
                }
            }
            return -1;
        }
    };
    public static final ArrayList<SWRunTableStruct> ProcessList = new ArrayList<SWRunTableStruct>();
    public static final DefaultListModel BlackListModel = new DefaultListModel();
    public static final DefaultListModel ProcessListModel = new DefaultListModel();
    public static final ListCellRenderer CellRanderer = new ListCellRenderer();
    public static final String MAILENCODE = "BIG5";
    public static String BlackListString(ListItem data) {
        BlackListStruct tmp = (BlackListStruct)data.getData();
        return String.format("%5d  %20s", tmp.No, tmp.Name);
    }
    public static String ProcessListString(ListItem data) {
        SWRunTableStruct tmp = (SWRunTableStruct)data.getData();
        return String.format("%5d  %20s  %s", tmp.Index, tmp.Name, DateFormat.getInstance().format(new Date(tmp.StartTime)));
    }
    public static ListModel readProcessList(String ip, int type) {
        SWRunTableStruct[] data = DBFunction.getInstance().GetSWRunTable(ip, type);
        StaticManager.ProcessList.clear();
        for (int i = 0 ; i < data.length ; i ++) {
            StaticManager.ProcessList.add(data[i]);
        }
        return StaticManager.getListModel(StaticManager.ProcessListModel, StaticManager.ProcessList, -1);
    }
    public static void updateBlackList() {
        BlackListStruct[] data = DBFunction.getInstance().GetBlackList();
        StaticManager.BlackList.clear();
        for (int i = 0 ; i < data.length ; i ++) {
            StaticManager.BlackList.add(data[i]);
        }
    }
    public static ListModel readBlackList(int type) {
        updateBlackList();
        return getListModel(StaticManager.BlackListModel, StaticManager.BlackList, type);
    }
    private static ListModel getListModel(DefaultListModel model, ArrayList<?> list,int type) {
        model.clear();
        for(int i = 0 ; i < list.size() ; i ++)
        {
            if (type == -1) {
                model.addElement(createItem(list.get(i)));
            } else {
                if (type == ((BlackListStruct)list.get(i)).Status) {
                    model.addElement(createItem(list.get(i)));
                }
            }
        }
        return model;
    }
    private static ListItem createItem(final Object data) {
        return new ListItem() {
            @Override
            public Object getData() {
                return data;
            }
        };
    }
    public static void showWarningMsg(Component frame, String msg, String title) {
        JOptionPane.showMessageDialog(frame, msg, title, JOptionPane.WARNING_MESSAGE);
    }
}
