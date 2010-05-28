package Project.mainThread;
import java.io.IOException;

import Project.db.DBFunction;
import Project.utils.Queue;
import Project.utils.ReverseDNS;

/**
 * @author bbxp
 *
 */
public class DnsAnalysis extends Thread {
    /**
     * dns分析 正常
     */
    public final static int NORMAL = 0;
    /**
     * dns分析 異常 對應不同ip
     */
    public final static int WARRING = 1;
    /**
     * dns分析 黑名單
     */
    public final static int BLACKLIST = 2;
    /**
     * 
     */
    public String ip;
    private Queue queue;
    /**
     * @param flowqueue
     */
    public DnsAnalysis (Queue flowqueue, String ip) {
        this.setName("DnsAnalysis");
        this.queue = flowqueue;
        this.ip = ip;
        this.setPriority(Thread.MAX_PRIORITY);
    }
    public void run() {
        while (true) {
            Object o = queue.deQueue();
            if (o instanceof String && ((String)o).equalsIgnoreCase("exit")) {
                break;
            }
            String ip = (String)o;
            String dns = "";
            try {
                dns = ReverseDNS.reverseDns(ip);
                if (dns.equalsIgnoreCase(ip)) {
                    continue;
                }
                // TODO z done db 檢查dns table
                int result = DBFunction.getInstance().checkDNSTable(ip, dns);
                switch (result) {
                case 0: //無對應dns資料
                    if (!ip.equalsIgnoreCase(dns)) { //ip和dns不同時 新增
                        DBFunction.getInstance().insertDNSTable(ip, dns, DnsAnalysis.NORMAL);
                    }
                    break;
                case 1: //對應到相同dns資料
                    DBFunction.getInstance().updateDNSTable(ip, dns, DnsAnalysis.NORMAL);
                    break;
                case 2: // TODO event 警告通知管理者 檢測到有對應異常
                    DBFunction.getInstance().updateDNSTable(ip, dns, DnsAnalysis.WARRING);
                    break;
                case 3: // TODO event 通知管理者 黑名單
                    DBFunction.getInstance().updateDNSTable(ip, dns, DnsAnalysis.BLACKLIST);
                    break;
                default: //錯誤
                    throw new Exception("DnsAnalysis 錯誤!");
                }
                //System.out.println(dns+"\t\t"+ip);
            } catch (IOException e) {
                //e.printStackTrace();
                System.err.println(ip+"\t"+dns+" error!");
            } catch (Exception e) {
                //e.printStackTrace();
                System.err.println(ip+"\t"+dns+" error!");
            }
        }
        System.out.println(ip+" dns analysis thread end!");
    }
}