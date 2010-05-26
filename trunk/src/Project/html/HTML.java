/**
 * 
 */
package Project.html;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Project.config.Config;
import Project.struct.DetectSet;
import Project.struct.SWRunTableStruct;
import Project.struct.TCPConnectStruct;
import Project.utils.StreamUtil;

/**
 * @author 怪叔叔
 * 
 * 將結構存成HTML檔案
 */
public final class HTML {
    private static final String TYPE_BOOT = "B";
    private static final String TYPE_OTHER = "O";
    private static final String TAIL_SW = "S";
    private static final String TAIL_TCP = "T";
    /**
     * 將TCPConnectStruct轉成HTML<p>
     * "狀態", "本地端Address", "本地端Port", "目的端Address", "目的端Port", "開始次數", "執行次數", "開始時間"
     * @return String 將傳進入的TCPConnectStruct轉成HTML的String
     * @see Project.Structure.TCPConnectStruct
     */
    private static String TCPToHTML(List<TCPConnectStruct> data){
        StringBuffer tmp = new StringBuffer();
        tmp.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                   "<html xmlns='http://www.w3.org/1999/xhtml'>\n" +
                   "<head>\n" +
                   "<meta http-equiv='Content-Type' content='text/html; charset='utf8' />\n" +
                   "<title>TCPConnectionTable</title>\n" +
                   "</head>\n" +
                   "<body>\n" +
                   "<table align='center' border='5'><tr bgColor='darkblue'>\n" +
                   "<th><font color=white></font></th>\n");
        for (int i = 0 ; i < TCPConnectStruct.Title.length ; i ++) {
            tmp.append("<th><font color=white>"+TCPConnectStruct.Title[i]+"</font></th>\n");
        }
        tmp.append("</tr>\n");
        for (int i = 0 ; i < data.size() ; i ++) {
            tmp.append("<tr  bgColor='lightyellow'><td bgColor='lightgreen'>\n");
            TCPConnectStruct tmpData = data.get(i);
            tmp.append("<td>" + tmpData.Status + "</td>\n");
            tmp.append("<td>" + tmpData.LocalAddress + "</td>\n");
            tmp.append("<td>" + tmpData.LocalPort + "</td>\n");
            tmp.append("<td>" + tmpData.RemoteAddress + "</td>\n");
            tmp.append("<td>" + tmpData.RemotePort + "</td>\n");
            tmp.append("<td>" + tmpData.StartTime + "</td>\n");
            tmp.append("<td>" + byte2String(tmpData.map) + "</td>\n");
            tmp.append("</tr>\n");
        }
        tmp.append("</table>\n");
        tmp.append("</body>\n");
        tmp.append("</html>\n");
        return tmp.toString();
    }
    /**
     * 將SWRunTableStruct轉成HTML<p>
     * "索引", "名稱", "ID", "路徑", "參數", "型態", "狀態", "開始次數", "執行次數", "開始時間"
     * @return String 將傳進入的SWRunTableStruct轉成HTML的String
     * @see Project.Structure.SWRunTableStruct
     */
    private static String SWRunToHTML(List<SWRunTableStruct> data){
        StringBuffer tmp = new StringBuffer();
        tmp.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                   "<html xmlns='http://www.w3.org/1999/xhtml'>\n" +
                   "<head>\n" +
                   "<meta http-equiv='Content-Type' content='text/html; charset='utf8' />\n" +
                   "<title>SWRunTable</title>\n" +
                   "</head>\n" +
                   "<body>\n" +
                   "<table align='center' border='5'><tr bgColor='darkblue'>\n" +
                   "<th><font color=white></font></th>\n");
        for (int i = 0 ; i < SWRunTableStruct.Title.length ; i ++) {
            tmp.append("<th><font color=white>"+SWRunTableStruct.Title[i]+"</font></th>\n");
        }
        tmp.append("</tr>\n");
        for (int i = 0 ; i < data.size() ; i ++) {
            tmp.append("<tr  bgColor='lightyellow'><td bgColor='lightgreen'>\n");
            SWRunTableStruct tmpData = data.get(i);
            tmp.append("<td>" + tmpData.Index + "</td>\n");
            tmp.append("<td>" + tmpData.Name + "</td>\n");
            tmp.append("<td>" + tmpData.ID + "</td>\n");
            tmp.append("<td>" + tmpData.Path + "</td>\n");
            tmp.append("<td>" + tmpData.Parameters + "</td>\n");
            tmp.append("<td>" + tmpData.Type + "</td>\n");
            tmp.append("<td>" + tmpData.Status + "</td>\n");
            tmp.append("<td>" + tmpData.StartTime + "</td>\n");
            tmp.append("<td>" + byte2String(tmpData.map) + "</td>\n");
            tmp.append("</tr>\n");
        }
        tmp.append("</table>\n");
        tmp.append("</body>\n");
        tmp.append("</html>\n");
        return tmp.toString();
    }
    /**
     * 將傳入的html存入到apache資料夾內的html檔
     * @param host DataStruct 記錄資料
     * @param ip String ip位址
     * @throws IOException 
     * @throws Exception 寫檔錯誤
     * @see Project.Structure.DataStruct
     */
    public static void ToFileTCP(DetectSet host, String ip, boolean isBoot) throws IOException{
        ArrayList<TCPConnectStruct> tmp = new ArrayList<TCPConnectStruct>();
        FileWriter fw;
        tmp.addAll(host.tcp.values());
        String tcp = "";
        if (isBoot) {
            tcp = FileName(ip, HTML.TYPE_BOOT, HTML.TAIL_TCP, host.ftcpcount);
        } else {
            tcp = FileName(ip, HTML.TYPE_OTHER, HTML.TAIL_TCP, host.ftcpcount);
        }
        fw = new FileWriter(tcp);
        fw.write(HTML.TCPToHTML(tmp));
        fw.flush();
        fw.close();
        StreamUtil.close(fw);
        tmp.removeAll(tmp);
        host.ftcpcount++;
    }
    /**
     * 將傳入的html存入到apache資料夾內的html檔
     * @param data DataStruct 記錄資料
     * @param type String 哪種型態的格式
     * @param ip String ip位址
     * @throws IOException 
     * @throws Exception 寫檔錯誤
     * @see Project.Structure.DataStruct
     */
    public static void ToFileSW(DetectSet host, String ip, boolean isBoot) throws IOException {
        ArrayList<SWRunTableStruct> tmp = new ArrayList<SWRunTableStruct>();
        FileWriter fw;
        tmp.addAll(host.sw.values());
        String sw = "";
        if (isBoot) {
            sw = FileName(ip, HTML.TYPE_BOOT, HTML.TAIL_SW, host.fswcount);
        } else {
            sw = FileName(ip, HTML.TYPE_OTHER, HTML.TAIL_SW, host.fswcount);
        }
        fw = new FileWriter(sw);
        fw.write(HTML.SWRunToHTML(tmp));
        fw.flush();
        StreamUtil.close(fw);
        tmp.removeAll(tmp);
        host.fswcount++;

    }
    private static String FileName(String ip, String type, String tail, int count) {
        return (Config.APACHE_PATH + type + ip + "_" + count + "_" + tail + ".html");
    }
    private static String byte2String(byte[] map) {
        StringBuffer tmp = new StringBuffer();
        tmp.append("<table align='center' border='5'><tr bgColor='darkblue'>\n");
        tmp.append("<tr  bgColor='lightyellow'>\n");
        for (int i = 0 ; i < map.length ; i ++) {
            if (map[i] != 0) {
                tmp.append("<td bgColor='red'>");
                tmp.append("　");
                
            } else {
                tmp.append("<td bgColor='black'>");
                tmp.append("　");
            }
            tmp.append("</td>\n");
        }
        tmp.append("</tr>\n");
        tmp.append("</table>\n");
        return tmp.toString();
    }
}
