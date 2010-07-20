package Project.struct;

import java.util.ArrayList;
import java.util.HashMap;

import Project.LogStream;
import Project.StaticManager;
import Project.config.Config;
import Project.db.DBFunction;
import Project.email.SendMail;
import Project.mainThread.Detection;
import Project.mainThread.DnsAnalysis;
import Project.mainThread.TaskSchedule;
import Project.mainThread.snmp.SnmpGet;
import Project.mainThread.snmp.SnmpGetTable;
import Project.mainThread.snmp.task.TaskSWRunListener;
import Project.mainThread.snmp.task.TaskSnmpGet;
import Project.mainThread.snmp.task.TaskTCPListener;
import Project.utils.Queue;

/**
 * @author bbxp
 *
 */
public class DetectSet {
    /**
     * 存入ip
     */
    public String ip;
    /**
     * 存入SNMP登入密碼
     */
    private String community;
    /**
     * 存入開始檢測時間
     */
    public long starttime;
    /**
     * 存結束檢測時間
     */
    public long endtime;
    /**
     * 存抓取SNMP Table資料的class TCPConnectionTable
     * 
     * @see Project.SnmpGetTable
     */
    public SnmpGetTable tcpsnmptable;
    /**
     * 存抓取SNMP Table資料的class SWRunTable
     * 
     * @see Project.SnmpGetTable
     */
    public SnmpGetTable swsnmptable;
    /**
     * 存抓取SNMP單一資料的class
     * 
     * @see Project.SnmpGet
     */
    public SnmpGet snmp;
    /**
     * netflow 所得資料送入dns反查
     */
    public Queue flowQueue;
    /**
     * SNMP hrSWRunTable內Table的所有統計資料
     * @see Project.Structure.SWRunTableStruct
     */
    public HashMap<String, SWRunTableStruct> sw;
    /**
     * SNMP TCPConnectTable內Table的所有統計資料
     * @see Project.Structure.TCPConnectStruct
     */
    public HashMap<String, TCPConnectStruct> tcp;
    /**
     * 運用netflow資料dns反查
     */
    private DnsAnalysis analysis;
    /**
     * 記錄Thread何時結束，及結束的function
     */
    private TaskStruct task;
    /**
     * 是否網卡已經up
     */
    private boolean isLinkUp;
    /**
     * sw存檔次數
     */
    public int fswcount;
    /**
     * tcp存檔次數
     */
    public int ftcpcount;
    /**
     * 是否已經關機
     */
    private boolean isShutdown;
    /**
     * SNMP sysuptime Thread
     */
    private Thread sysThread;
    /**
     * taskSNMPGet
     */
    public TaskSnmpGet tasksnmpget;
    private TaskSWRunListener swlistener;
    private TaskTCPListener tcplistener;
    public ArrayList<BlackListStruct> comparerList;
    public int grayListCount;
    /**
     * @param ip
     */
    public DetectSet(String ip) {
        this.ip = ip;
        this.community = Config.COMMUNITY;
        this.flowQueue = new Queue(Config.NETFLOW_QUEUE_SIZE);
        this.isLinkUp = false;
        this.isShutdown = false;
        this.snmp = new SnmpGet(ip, community);
        this.sw = new HashMap<String, SWRunTableStruct>();
        this.tcp = new HashMap<String, TCPConnectStruct>();
        this.analysis = new DnsAnalysis(flowQueue, ip);
        this.tasksnmpget = new TaskSnmpGet(snmp, this, Config.SYSUPTIME);
        this.sysThread = new Thread(tasksnmpget);
        this.sysThread.setName(ip + " SNMPGet");
        this.sysThread.setPriority(Thread.MAX_PRIORITY);
        this.fswcount = 0;
        this.ftcpcount = 0;
        this.swlistener = new TaskSWRunListener(this);
        this.tcplistener = new TaskTCPListener(this);
        this.tcpsnmptable = new SnmpGetTable(ip, community, Config.TCPCONNECT, tcplistener);
        this.swsnmptable = new SnmpGetTable(ip, community, Config.SWRUNTABLE, swlistener);
        this.comparerList = new ArrayList<BlackListStruct>() {
            /**
             * 
             */
            private static final long serialVersionUID = 7811372418804483943L;
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
        this.grayListCount = 0;
        this.starttime = System.currentTimeMillis();
        // TODO z done db 檢查ip table並建立建立DB，失敗System.exit(1)
        if (!DBFunction.getInstance().initAllTable(ip)) {
            System.err.println("檢查ip table及建立DB錯誤!");
            System.exit(1);
        }
    }
    /**
     * 結束Analysis Thread
     */
    public void stopAnalysis() {
        flowQueue.enQueue("exit");
        // TODO z done join analysis thread
        StaticManager.ZOMBIE_QUEUE.enQueue(analysis);
    }
    /**
     * 結束SnmpGet Thread
     */
    public void stopSnmpGet() {
        // TODO z done join and stop SnmpGet Thread
        StaticManager.ZOMBIE_QUEUE.enQueue(sysThread);
    }
    /**
     * 設定dns反查結束偵測時間
     */
    public void setTaskStopTime(){
        task.time = System.currentTimeMillis();
    }
    /**
     * 
     */
    public synchronized void setLinkUp() {
        if (!isLinkUp) {
            // TODO z done task 開始偵測
            sysThread.start();
            new Detection(this).start();
            
            swsnmptable.addListener();
            tcpsnmptable.addListener();
            
            analysis.start();
            task = new TaskStruct(this, "stopAnalysis", System.currentTimeMillis()+(Config.DNS_ANALYSIS_TIME*60*1000), System.currentTimeMillis());
            TaskSchedule.getInstance().insertTask(task);
            
            isLinkUp = true;
        }
    }
    public void clearTCPHash() {
        this.tcp.clear();
    }
    public void clearSWHash() {
        this.sw.clear();
    }
    public boolean isShutdown() {
        return this.isShutdown;
    }
    public synchronized void setShutdown() {
        if (!isShutdown()) {
            System.out.println(ip+" 已經關機");
            LogStream.getInstance().sysPrint(ip+" 已經關機");
            StaticManager.sysPrintDate(true);
            StaticManager.printDate(System.currentTimeMillis());
            this.isShutdown = true;
            tcplistener.stopListener(tcpsnmptable.table);
            swlistener.stopListener(swsnmptable.table);
            StaticManager.IPList.remove(ip);
        }
    }
    public void checkGrayList() {
        //System.out.println("比對黑白名單 "+sw.size());
        StaticManager.updateBlackList();
        for (SWRunTableStruct data : sw.values()) {
            if (StaticManager.BlackList.equals(data)) { // 黑白灰名單內有資料
                BlackListStruct tmp = StaticManager.BlackList.get(StaticManager.BlackList.indexOf(data));
                checkComparerList(tmp);
            } else { //加入灰名單
                checkComparerList(new GrayListStruct(data.Name, data.Index, data.Parametes, data.Path, data.Type, StaticManager.GRAYLIST, data.StartTime));
            }
        }
    }
    private void checkComparerList(BlackListStruct tmp) {
        if (comparerList.equals(tmp)) { // 已經有在比對名單內
            //System.out.println("在比對名單裡面");
            BlackListStruct comparer = comparerList.get(comparerList.indexOf(tmp));
            switch (tmp.Status) {
            case StaticManager.BLACKLIST:
            case StaticManager.WHITELIST:
                break;
            case StaticManager.GRAYLIST:
                GrayListStruct gray = (GrayListStruct) comparer;
                gray.endTime = System.currentTimeMillis();
                break;
            default:
                System.err.println("檢測黑白名單格式錯誤!");
            }
        } else { // 如果沒資料
            //System.out.println("沒有在比對名單裡面");
            switch (tmp.Status) {
            case StaticManager.BLACKLIST:
                // TODO event 通知管理者 有黑名單之處理程序
                SendMail.getInstance().sendMail(
                        String.format("處理程序為：\n程序名稱：%s\n路徑：%s\n參數：%s\n型態：%s\n", 
                                tmp.Name, tmp.Path, tmp.Parametes, tmp.Type), 
                        StaticManager.BLACK_DETECTED, StaticManager.OPTION_SEVERE);
            case StaticManager.WHITELIST:
                comparerList.add(tmp);
                break;
            case StaticManager.GRAYLIST:
                //System.out.println("加入灰名單");
                comparerList.add(new GrayListStruct(tmp));
                grayListCount++;
                break;
            default:
                System.err.println("檢測黑白名單格式錯誤!");
            }
        }
    }
}
