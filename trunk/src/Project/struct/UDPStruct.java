/**
 * 
 */
package Project.struct;
/**
 * UDP Table欄位
 * 
 * @author 怪叔叔
 *
 */
public class UDPStruct {
    /**
     * UdpLocalAddress 為hash的索引鍵之一
     */
    public String LocalAddress;
    /**
     * UdpLocalPort 為hash的索引鍵之一
     */
    public int LocalPort;
    /**
     * 紀錄開始是第幾次
     */
    public int StartCount;
    /**
     * 紀錄執行次數
     */
    public int Count;
    /**
     * 紀錄開始時間
     */
    public long StartTime;
    /**
     * 要輸出的欄位標頭
     */
    public static String[] Title = {"本地端Address", "本地端Port", "開始次數", "執行次數", "開始時間"};
    /**
     * 印出這筆資料的所有的資訊
     *
     */
    public void print() {
        System.out.println(LocalAddress+"\t"+LocalPort+"\t"+StartCount+"\t"+Count+"\t"+Header.timeformat.format(StartTime));
    }
}
