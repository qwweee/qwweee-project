/**
 * 
 */
package Project.mainThread;

import Project.StaticManager;
import Project.config.Config;
import Project.struct.DetectSet;

import com.adventnet.snmp.beans.SnmpTrapReceiver;
import com.adventnet.snmp.beans.TrapEvent;
import com.adventnet.snmp.beans.TrapListener;
/**
 * 執行SnmpTrap的Receiver<p>
 * 進行記錄開機後DETECT_TIME時間，每隔THREAD_TIME抓取該電腦的SNMP資訊
 * 
 * @author 怪叔叔
 */
public class Trapd {
    /**
     * SnmpTrapReceiver
     */
    private SnmpTrapReceiver receiver;
    /**
     * TrapListener
     */
    private TrapListener listener;
    /**
     * 存ip對應DataStruct
     * 
     * @see Project.struct.Structure.DataStruct
     */
    private static Trapd instance;
    public static Trapd getInstance() {
        return (instance == null)?(instance = new Trapd(Config.COMMUNITY)):instance;
    }
    /**
     * 這是建構元<p>
     * port 預設為 162<p>
     * 進行初始化 init()
     * 
     * @param community 輸入的密碼
     * 
     */
    private Trapd(String community){
        init();
        start();
    }
    /**
     * 這是建構元<p>
     * 沒有參數<p>
     * community 預設為 public<p>
     * port 預設為 162<p>
     * 進行初始化 init()
     *
     */
    public Trapd(){
        init();
        start();
    }
    /**
     * 初始化 將trap為coldstart的記錄 starttim, ip, port<p>
     * 並執行Thread跑config檔內的設定時間
     *
     * @see Project.struct.Structure.DataStruct
     */
    private void init(){
        receiver = new SnmpTrapReceiver();
        receiver.setCommunity(Config.COMMUNITY);
        receiver.setPort(Config.TRAPPORT);
        listener = new TrapListener() {
            public void receivedTrap(TrapEvent trap) {
                if (trap.getTrapType() == 0){
                    // TODO z done task 開機trap 後執行內容
                    if (!StaticManager.IPList.containsKey(trap.getRemoteHost())) {
                        DetectSet data = new DetectSet(trap.getRemoteHost());
                        StaticManager.IPList.put(trap.getRemoteHost(), data);
                        StaticManager.FlowList.put(trap.getRemoteHost(), data);
                        System.out.println("add "+data.ip+" to IPList!");
                        //data.setLinkUp();
                    } else {
                        System.out.println(trap.getRemoteHost()+" already in list!");
                    }
                } else if (trap.getTrapType() == 3) {
                    // TODO z done task 網路開啟trap 後開始偵測
                    StaticManager.IPList.get(trap.getRemoteHost()).setLinkUp();
                }
            }
        };
    }
    /**
     * 執行Snmp Trap的Receiver
     *
     */
    private void start(){
        System.out.println("trapd is running!");
        receiver.addTrapListener(listener);
    }
    public void stop() {
        receiver.removeTrapListener(listener);
        System.out.println("trapd is ending!");
    }
}
