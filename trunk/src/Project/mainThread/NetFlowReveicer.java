/**
 * 
 */
package Project.mainThread;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import Project.config.Config;
import Project.utils.Queue;

/**
 * @author bbxp
 *
 */
public class NetFlowReveicer extends Thread{
    /**
     * listen udp port
     */
    private int port;
    /**
     * process netflow packet queue
     */
    private Queue queue;
    /**
     * 處理netflow封包的thread
     */
    private NetFlowProcess netflow;
    /**
     * 建構元，初始化
     */
    public NetFlowReveicer() {
        this.port = Config.NETFLOWPORT;
        this.queue = new Queue(Config.NETFLOW_QUEUE_SIZE);
        netflow = new NetFlowProcess(queue);
        netflow.start();
    }
    /**
     * 執行thread 跑udp socket server接收netflow送來的訊息
     */
    public void run() {
        try {
            DatagramSocket socket=new DatagramSocket(port);
            System.out.println("Listen on Netflow Port " + port + " Start ........");
            while(true) {
                byte[] buffer = new byte[Config.NETFLOWBUFFER];
                DatagramPacket packet = new DatagramPacket(buffer,buffer.length);
                socket.receive(packet);
                queue.enQueue(packet);
            }
        } catch (SocketException e) {
            e.printStackTrace();
            System.out.println("NetflowReceiver socket error!");
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("NetflowReceiver catch packet error!");
            System.exit(1);
        }
    }

}
