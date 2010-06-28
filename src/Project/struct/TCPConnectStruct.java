/**
 * 
 */
package Project.struct;

import Project.config.Config;

/**
 * TcpConnectionTable欄位
 * 
 * @author 怪叔叔
 *
 */
public class TCPConnectStruct {
    /**
     * TcpConnStatus
     */
    public String Status;
    /**
     * TcpConnLocalAddress 為hash的索引鍵之一
     */
    public String LocalAddress;
    /**
     * TcpConnRemoteAddress 為hash的索引鍵之一
     */
    public String RemoteAddress;
    /**
     * TcpConnLocalPort 為hash的索引鍵之一
     */
    public int LocalPort;
    /**
     * TcpConnRemotePort 為hash的索引鍵之一
     */
    public int RemotePort;
    /**
     * 紀錄在何時有此connection (60 sec / per time)
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
     * 印出這筆資料的所有的資訊
     *
     */
    public void print() {
        System.out.println(Status+"\t"+LocalAddress+"\t"+LocalPort+"\t"+RemoteAddress+"\t"+RemotePort+"\t"+Header.timeformat.format(StartTime));
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
    }
}
