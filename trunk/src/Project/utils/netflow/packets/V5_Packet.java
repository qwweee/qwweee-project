package Project.utils.netflow.packets;

import Project.StaticManager;
import Project.db.DBFunction;
import Project.utils.netflow.DoneException;
import Project.utils.netflow.Util;

/*

 *-------*---------------*------------------------------------------------------*
 | Bytes | Contents      | Description                                          |
 *-------*---------------*------------------------------------------------------*
 | 0-1   | version       | NetFlow export format version number                 |
 *-------*---------------*------------------------------------------------------*
 | 2-3   | count         | Number of flows exported in this packet (1-30)       |
 *-------*---------------*------------------------------------------------------*
 | 4-7   | SysUptime     | Current time in milliseconds since the export device |
 |       |               | booted                                               |
 *-------*---------------*------------------------------------------------------*
 | 8-11  | unix_secs     | Current count of seconds since 0000 UTC 1970         |
 *-------*---------------*------------------------------------------------------*
 | 12-15 | unix_nsecs    | Residual nanoseconds since 0000 UTC 1970             |
 *-------*---------------*------------------------------------------------------*
 | 16-19 | flow_sequence | Sequence counter of total flows seen                 |
 *-------*---------------*------------------------------------------------------*
 | 20    | engine_type   | Type of flow-switching engine                        |
 *-------*---------------*-------------------------------------------Source ID--*
 | 21    | engine_id     | Slot number of the flow-switching engine             |
 *-------*---------------*------------------------------------------------------*
 | 22-23 | reserved      | Unused (zero) bytes                                  |
 *-------*---------------*------------------------------------------------------*

 */

/**
 * @author bbxp
 * 將送入的封包進行解析
 */
public class V5_Packet {
    private long count;

    @SuppressWarnings("unused")
    private String RouterIP;
    private long SysUptime, unix_secs, unix_nsecs, flow_sequence;
    private long engine_type, engine_id;

    /**
     * netflow packet header size (V5)
     */
    public static final int V5_Header_Size = 24;

    /**
     * netflow packet every one flow size (V5)
     */
    public static final int V5_Flow_Size = 48;

    /**
     *
     * @param RouterIP
     * @param buf
     * @param len
     * @throws DoneException
     */
    public V5_Packet(String RouterIP, byte[] buf, int len) throws DoneException {
        if (len < V5_Header_Size)
            throw new DoneException("    * incomplete header *");

        this.RouterIP = RouterIP;
        count = Util.to_number(buf, 2, 2);

        if (count <= 0 || len != V5_Header_Size + count * V5_Flow_Size)
            throw new DoneException("    * corrupted packet " + len + "/"
                    + count + "/" + (V5_Header_Size + count * V5_Flow_Size)
                    + " *");

        SysUptime = Util.to_number(buf, 4, 4);
        unix_secs = Util.to_number(buf, 8, 4);
        unix_nsecs = Util.to_number(buf, 12, 4);
        flow_sequence = Util.to_number(buf, 16, 4);
        engine_type = buf[20];
        engine_id = buf[21];
        
        //flows = new Vector((int) count);
        //System.out.println(count);
        for (int i = 0, p = V5_Header_Size; i < count; i++, p += V5_Flow_Size) {
            V5_Flow f = null;
            try {
                f = new V5_Flow(RouterIP, buf, p);
                // TODO z done db 將flow資料存入資料庫
                if (StaticManager.IPList.containsKey(f.dstaddr)) {
                    DBFunction.getInstance().insertFlowTable(f.dstaddr, f, SysUptime, unix_secs, unix_nsecs, flow_sequence, engine_type, engine_id);
                    if (StaticManager.FlowList.containsKey(f.dstaddr)) {
                        StaticManager.FlowList.get(f.dstaddr).flowQueue.enQueue(f.srcaddr);
                    }
                    //f.printAll();
                }
                if (StaticManager.IPList.containsKey(f.srcaddr)) {
                    DBFunction.getInstance().insertFlowTable(f.srcaddr, f, SysUptime, unix_secs, unix_nsecs, flow_sequence, engine_type, engine_id);
                    if (StaticManager.FlowList.containsKey(f.srcaddr)) {
                        StaticManager.FlowList.get(f.srcaddr).flowQueue.enQueue(f.dstaddr);
                    }
                    //f.printAll();
                }
                //f.printAll();
                /*if (f.srcaddr != null && f.dstaddr != null) {
                    flows.add(f);
                } else {
                }*/
            } catch (DoneException e) {
                e.printStackTrace();
            }
        }
    }

    protected static String add_raw_sql = null;
}
