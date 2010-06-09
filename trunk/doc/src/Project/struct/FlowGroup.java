package Project.struct;

/**
 * @author bbxp
 *
 */
public class FlowGroup {
    public String ip;
    public int port;
    public int count;
    public void printAll() {
        System.out.println(String.format("%16s %5d %5d", ip, port, count));
    }
}