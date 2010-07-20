/**
 * 
 */
package Project.mainThread;

import java.net.DatagramPacket;

import Project.LogStream;
import Project.StaticManager;
import Project.utils.Queue;
import Project.utils.netflow.Util;
import Project.utils.netflow.packets.V5_Packet;

/**
 * @author bbxp
 * 處理所收到的netflow packet
 */
public class NetFlowProcess extends Thread{
    /**
     * 處理netflow packet的queue
     */
    private Queue queue;
    /**
     * 建構元
     * @param queue netflow packet的queue
     */
    private static NetFlowProcess instance;
    public static NetFlowProcess getInstance() {
        return (instance == null)?(instance = new NetFlowProcess()):instance;
    }
    private NetFlowProcess() {
        this.setName("NetFlowProcess");
        this.queue = StaticManager.NETFLOW_QUEUE;
        this.setPriority(Thread.MAX_PRIORITY);
        this.start();
    }
    /**
     * 處理netflow packet的thread
     */
    public void run() {
        System.out.println("Process Netflow Packet Start ...............");
        LogStream.getInstance().sysPrint("Process Netflow Packet Start ...............");
        while(true) {
            DatagramPacket packet = (DatagramPacket)queue.deQueue();
            processPacket(packet);
        }
    }
    /**
     * 將封包進行分解，轉成可用資料
     * @param packet 收到的封包
     */
    private void processPacket(DatagramPacket p) {
        try {
            byte[] buf = p.getData();
            int len = p.getLength();
            String addr = p.getAddress().getHostAddress().trim();
            if (len < 2) {
                System.out.println("  * too short packet *");
            }
            short version = (short) Util.to_number(buf, 0, 2);
            if (version > 9 || version <= 0) {
                System.out.println("  * unsupported version *");
            }
            switch (version) {
            case 5:
                new V5_Packet(addr, buf, len);
                break;
            case 1:
            case 7:
            case 8:
            case 9:
            default:
                System.out.println("Error version");
            break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Process Netflow Packet Error!");
        }
    }
}
