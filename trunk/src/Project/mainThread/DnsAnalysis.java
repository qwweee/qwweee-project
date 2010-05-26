package Project.mainThread;
import java.io.IOException;

import Project.utils.Queue;
import Project.utils.ReverseDNS;

/**
 * @author bbxp
 *
 */
public class DnsAnalysis extends Thread {
    
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
    }
    public void run() {
        while (true) {
            Object o = queue.deQueue();
            if (o instanceof String && ((String)o).equalsIgnoreCase("exit")) {
                break;
            }
            String ip = (String)o;
            try {
                String dns = ReverseDNS.reverseDns(ip);
                if (dns.equalsIgnoreCase(ip)) {
                    continue;
                }
                // TODO db 檢查dns table
                /*int haven = DBTest.getInstance().CheckDNSTable(ip, dns);
                if (haven == 0) { //無對應dns資料
                    if (!ip.equalsIgnoreCase(dns)) {
                        DBTest.getInstance().InsertDNSTable(ip, dns, 0);
                    }
                } else if (haven == 1) { //對應到相同dns資料

                } else if (haven > 1){
                    // TODO event 通知管理者
                } else {
                    // 錯誤
                    throw new Exception("新增DNSTable資料錯誤");
                }*/
                //System.out.println(dns+"\t\t"+ip);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(ip+" dns analysis thread end!");
    }
}