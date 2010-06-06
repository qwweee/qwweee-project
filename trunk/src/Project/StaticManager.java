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
    
    public final static String[] BLACKLISTTITLE = {"黑名單", "白名單", "灰名單"};
    /**
     * 存ip對應DataStruct
     * 
     * @see Project.Structure.DataStruct
     */
    public static HashMap<String, DetectSet> IPList = new HashMap<String, DetectSet>();
    /**
     * 存ip對應DataStruct
     * 
     * @see Project.Structure.DataStruct
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
    public static final ArrayList<BlackListStruct> BlackList = new ArrayList<BlackListStruct>() {
        public boolean equals(Object o) {
            for (Object data : this.toArray()) {
                if (data.equals(o)){
                    return true;
                }
            }
            return false;
    }
    };
    public static final ArrayList<SWRunTableStruct> ProcessList = new ArrayList<SWRunTableStruct>();
    public static final DefaultListModel BlackListModel = new DefaultListModel();
    public static final DefaultListModel ProcessListModel = new DefaultListModel();
    public static final ListCellRenderer CellRanderer = new ListCellRenderer();
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
    public static ListModel readBlackList(int type) {
        BlackListStruct[] data = DBFunction.getInstance().GetBlackList();
        StaticManager.BlackList.clear();
        for (int i = 0 ; i < data.length ; i ++) {
            StaticManager.BlackList.add(data[i]);
        }
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
