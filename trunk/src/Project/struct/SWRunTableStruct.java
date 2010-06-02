/**
 * 
 */
package Project.struct;

import Project.config.Config;

/**
 * SWRunTable欄位
 * 
 * @author 怪叔叔
 *
 */
public class SWRunTableStruct {
    /**
     * hrSWRunIndex 為hash的索引鍵
     */
    public int Index;
    /**
     * hrSWRunName
     */
    public String Name;
    /**
     * hrSWRunID
     */
    public String ID;
    /**
     * hrSWRunPath
     */
    public String Path;
    /**
     * hrSWRunParameters
     */
    public String Parametes;
    /**
     * hrSWRunType
     */
    public String Type;
    /**
     * hrSWRunStatus
     */
    public String Status;
    /**
     * 紀錄在何時有此處理程序 (60 sec / per time)
     */
    public byte[] map;
    /**
     * 紀錄開始時間
     */
    public long StartTime;
    /**
     * 紀錄結束時間
     */
    public long EndTime;
    /**
     * 要輸出的欄位標頭
     */
    public static String[] Title = {"索引", "名稱", "ID", "路徑", "參數", "型態", "狀態", "開始時間"};
    /**
     * 印出這筆資料的所有的資訊
     *
     */
    public void print() {
        System.out.println(Index+"\t"+Name+"\t"+ID+"\t"+Path+"\t"+Parametes+"\t"+Type+"\t"+Status+"\t"+Header.timeformat.format(StartTime));
        for (int i = 0 ; i < map.length ; i ++) {
            System.out.print(map[i]+"\t");
        }
        System.out.println();
    }
    /**
     * 初始化記錄處理程序時間陣列大小
     * @param isboot
     */
    public void setBoot(boolean isboot) {
        if (isboot) {
            map = new byte[(int)(60/Config.PER_BOOT_DETECT_TIME)];
        } else {
            map = new byte[(int)(60/Config.PER_OTHER_DETECT_TIME)];
        }
        for (int i = 0 ; i < map.length ; i ++) {
            map[i] = -1;
        }
    }
}
