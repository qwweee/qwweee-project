/**
 * 
 */
package Project.mainThread;

import Project.LogStream;
import Project.StaticManager;
import Project.config.Config;
import Project.db.DBFunction;
import Project.email.SendMail;
import Project.struct.BlackListStruct;
import Project.struct.DataStruct;
import Project.struct.DetectSet;
import Project.struct.FlowGroup;
import Project.struct.GrayListStruct;
import Project.utils.ExcelUtil;
import Project.utils.ProcessFFT;
import Project.utils.TimeUtil;
import Project.utils.FFT.Complex;

/**
 * @author 怪叔叔
 *
 */
public class Detection extends Thread {
    /**
     * 存Trap所需資訊，及開始時間
     */
    private DetectSet host;
    public static boolean isScan = false;
    public boolean alreadyDetect;
    /**
     * 
     * @param data 傳入Trap所需要的資訊，以便抓SNMP資料
     * @see Project.struct.Structure.DataStruct
     */
    public Detection(DetectSet data){
        this.setName(data.ip + " Detection");
        host = data;
        host.starttime = System.currentTimeMillis();
        this.setPriority(Thread.MAX_PRIORITY);
        alreadyDetect = false;
    }
    /**
     * 從開機時間開始算Config.DETECT_TIME_OPEN，每隔Config.THREAD_TIME_OPEN時間抓取SNMP資訊
     * 
     * @see Project.Config.Config#DETECT_TIME
     * @see Project.Config.Config#THREAD_TIME
     */
    public void run(){
        System.out.println(host.ip+" Daemon Detecting!!");
        StaticManager.printDate(System.currentTimeMillis());
        StaticManager.sysPrintDate(true);
        // TODO z done 判斷是否關機
        while (!host.isShutdown()) {
            try {
                sleep(Config.PER_OTHER_DETECT_TIME*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            host.tasksnmpget.go();
            //System.out.println("systime get");
        }
        // TODO z done 中斷dns反查
        host.setTaskStopTime();
        host.endtime = System.currentTimeMillis();
        // TODO analysis 關機灰名單檢測
        detectGrayList();
        // TODO analysis 關機後 進行netflow分析
        detectNetflow();
        // TODO z done join detection thread
        StaticManager.ZOMBIE_QUEUE.enQueue(this);
    }
    /**
     * 取得host的IP
     * @return String ip
     */
    public String getHost() {
        return this.host.ip;
    }
    private void detectGrayList() {
        if (host.grayListCount != 0) {
            for (BlackListStruct tmp : host.comparerList) {
                if (tmp instanceof GrayListStruct) {
                    GrayListStruct gray = (GrayListStruct)tmp;
                    if (detectNetflow(gray.startTime, gray.endTime)) {
                        // TODO detect 加入黑名單
                        insertBlackList(gray, StaticManager.BLACKLIST);
                        // TODO event 通知管理者 將灰名單加入黑名單
                        SendMail.getInstance().sendMail(
                                String.format("加入黑名單之處理程序為：\n程序名稱：%s\n路徑：%s\n參數：%s\n型態：%s\n" +
                                		"執行時間從：%s 至 %s\n", gray.Name, gray.Path, gray.Parametes, gray.Type, TimeUtil.timeToString(gray.startTime), TimeUtil.timeToString(gray.endTime)), 
                                StaticManager.BLACKLIST_ADD, StaticManager.OPTION_INFO);
                        LogStream.getInstance().eventPrint(String.format("%.3f min", (gray.endTime - gray.startTime)/1000.0/60.0));
                        StaticManager.sysPrintDate(false);
                        LogStream.getInstance().eventPrint("\n");
                    } else {
                        // TODO detect 加入白名單
                        insertBlackList(gray, StaticManager.WHITELIST);
                        // TODO event 通知管理者 將灰名單加入白名單
                        SendMail.getInstance().sendMail(
                                String.format("加入白名單之處理程序為：\n程序名稱：%s\n路徑：%s\n參數：%s\n型態：%s\n" +
                                		"執行時間從：%s 至 %s\n", gray.Name, gray.Path, gray.Parametes, gray.Type, TimeUtil.timeToString(gray.startTime), TimeUtil.timeToString(gray.endTime)), 
                                StaticManager.WHITELIST_ADD, StaticManager.OPTION_INFO);
                        LogStream.getInstance().eventPrint(String.format("%.3f min", (gray.endTime - gray.startTime)/1000.0/60.0));
                        StaticManager.sysPrintDate(false);
                        LogStream.getInstance().eventPrint("\n");
                    }
                }
            }
        }
    }
    private void detectNetflow() {
        FlowGroup[] list = DBFunction.getInstance().getFlowGroups(host.ip);
        for (int i = 0 ; i < list.length ; i ++) {
            DataStruct[] data = DBFunction.getInstance().getFlowsData(host.ip, list[i].ip, list[i].port, true);
            if (data == null || data.length <= Config.GROUPCOUNT) {
                continue;
            }
            Complex[] fft = null;
            if ((fft = ProcessFFT.processFFT(data,list[i].ip,list[i].port)) != null) {
                ExcelUtil.writeExcel(host.ip, list[i].ip, list[i].port, data, false, fft);
            } else {
                fft = ProcessFFT.processFFT(data);
                ExcelUtil.writeExcel(host.ip, list[i].ip, list[i].port, data, false, fft, "");
            }
        }
    }
    private boolean detectNetflow(long start, long end) {
        //System.out.println(new Timestamp(start)+"\t"+new Timestamp(end));
        FlowGroup[] list = DBFunction.getInstance().getFlowGroups(host.ip, start, end);
        for (int i = 0 ; i < list.length ; i ++) {
            isScan = false;
            DataStruct[] data = DBFunction.getInstance().getFlowsData(host.ip, list[i].ip, list[i].port, false);
            if (data == null || data.length <= Config.GROUPCOUNT) {
                continue;
            }
            Complex[] fft = null;
            if ((fft = ProcessFFT.processFFT(data,list[i].ip,list[i].port)) != null) {
                ExcelUtil.writeExcel(host.ip, list[i].ip, list[i].port, data, false, fft, false, start, end);
                return true;
            } else {
                fft = ProcessFFT.processFFT(data);
                ExcelUtil.writeExcel(host.ip, list[i].ip, list[i].port, data, false, fft, false, start, end);
            }
            if (isScan) {
                return true;
            }
        }
        return false;
    }
    private boolean insertBlackList(GrayListStruct data, int type) {
        if (StaticManager.BlackList.equals(data)) {
            return false;
        } else {
            return DBFunction.getInstance().insertBlackList(data, type);
        }
    }
}
