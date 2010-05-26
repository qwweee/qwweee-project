/**
 * 
 */
package Project.mainThread;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import Project.StaticManager;
import Project.config.Config;
import Project.utils.Queue;

/**
 * @author bbxp
 *
 */
public class NetFlowReceiver extends Thread{
    /**
     * listen udp port
     */
    private int port;
    /**
     * process netflow packet queue
     */
    private Queue queue;
    private static NetFlowReceiver instance;
    public static NetFlowReceiver getInstance() {
        return (instance == null)?(instance = new NetFlowReceiver()):instance;
    }
    /**
     * 建構元，初始化
     */
    private NetFlowReceiver() {
        this.setName("NetFlowReceiver");
        this.port = Config.NETFLOWPORT;
        this.queue = StaticManager.NETFLOW_QUEUE;
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
