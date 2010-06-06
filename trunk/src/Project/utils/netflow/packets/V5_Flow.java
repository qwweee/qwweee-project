package Project.utils.netflow.packets;

import Project.utils.netflow.Address;
import Project.utils.netflow.DoneException;
import Project.utils.netflow.Prefix;
import Project.utils.netflow.Util;

/*
 *-------*-----------*----------------------------------------------------------*
 | Bytes | Contents  | Description                                              |
 *-------*-----------*----------------------------------------------------------*
 | 0-3   | srcaddr   | Source IP address                                        |
 *-------*-----------*----------------------------------------------------------*
 | 4-7   | dstaddr   | Destination IP address                                   |
 *-------*-----------*----------------------------------------------------------*
 | 8-11  | nexthop   | IP address of next hop router                            |
 *-------*-----------*----------------------------------------------------------*
 | 12-13 | input     | Interface index (ifindex) of input interface             |
 *-------*-----------*----------------------------------------------------------*
 | 14-15 | output    | Interface index (ifindex) of output interface            |
 *-------*-----------*----------------------------------------------------------*
 | 16-19 | dPkts     | Packets in the flow                                      |
 *-------*-----------*----------------------------------------------------------*
 | 20-23 | dOctets   | Total number of Layer 3 bytes in the packets of the flow |
 *-------*-----------*----------------------------------------------------------*
 | 24-27 | First     | SysUptime at start of flow                               |
 *-------*-----------*----------------------------------------------------------*
 | 28-31 | Last      | SysUptime at the time the last packet of the flow was    |
 |       |           | received                                                 |
 *-------*-----------*----------------------------------------------------------*
 | 32-33 | srcport   | TCP/UDP source port number or equivalent                 |
 *-------*-----------*----------------------------------------------------------*
 | 34-35 | dstport   | TCP/UDP destination port number or equivalent            |
 *-------*-----------*----------------------------------------------------------*
 | 36    | pad1      | Unused (zero) bytes                                      |
 *-------*-----------*----------------------------------------------------------*
 | 37    | tcp_flags | Cumulative OR of TCP flags                               |
 *-------*-----------*----------------------------------------------------------*
 | 38    | prot      | IP protocol type (for example, TCP = 6; UDP = 17)        |
 *-------*-----------*----------------------------------------------------------*
 | 39    | tos       | IP type of service (ToS)                                 |
 *-------*-----------*----------------------------------------------------------*
 | 40-41 | src_as    | Autonomous system number of the source, either origin or |
 |       |           | peer                                                     |
 *-------*-----------*----------------------------------------------------------*
 | 42-43 | dst_as    | Autonomous system number of the destination, either      |
 |       |           | origin or peer                                           |
 *-------*-----------*----------------------------------------------------------*
 | 44    | src_mask  | Source address prefix mask bits                          |
 *-------*-----------*----------------------------------------------------------*
 | 45    | dst_mask  | Destination address prefix mask bits                     |
 *-------*-----------*----------------------------------------------------------*
 | 46-47 | pad2      | Unused (zero) bytes                                      |
 *-------*-----------*----------------------------------------------------------*

 */

/**
 * @author bbxp
 * 每個netflow packet 都有多個 flow 資料，此為每個flow的資訊
 */
public class V5_Flow extends Flow {
    /**
     * source address
     */
    public String srcaddr = "";
    /**
     * destination address
     */
    public String dstaddr = "";
    /**
     * nethop
     */
    public String nexthop = "";
    
    private Prefix srcprefix, dstprefix;
    /**
     * input
     */
    public long input = -1;
    /**
     * output
     */
    public long output = -1;
    /**
     * packets
     */
    public long dPkts = 0;
    /**
     * octets
     */
    public long dOctets = 0;
    /**
     * first
     */
    public long First = 0;
    /**
     * last
     */
    public long Last = 0;
    /**
     * source port
     */
    public long srcport = -1;
    /**
     * destination port
     */
    public long dstport = -1;
    /**
     * tcp flags
     */
    public byte tcp_flags = 0;
    /**
     * protocol
     */
    public byte prot = -1;
    /**
     * TOS
     */
    public byte tos = 0;
    /**
     * source As
     */
    public long src_as = -1;
    /**
     * destination As
     */
    public long dst_as = -1;
    /**
     * source mask
     */
    public byte src_mask = 0;
    /**
     * destination mask
     */
    public byte dst_mask = 0;
    /**
     * Router ip
     */
    public String RouterIP = "";

    private long src_addr = 0, dst_addr = 0, next_hop = 0;

    /**
     * 建構元
     * @param RouterIP 
     * @param buf
     * @param off
     * @throws DoneException
     */
    public V5_Flow(String RouterIP, byte[] buf, int off) throws DoneException {
        this.RouterIP = RouterIP;

        srcaddr = Util.str_addr(src_addr = Util.to_number(buf, off + 0, 4));
        dstaddr = Util.str_addr(dst_addr = Util.to_number(buf, off + 4, 4));
        nexthop = Util.str_addr(next_hop = Util.to_number(buf, off + 8, 4));

        input = Util.to_number(buf, off + 12, 2);
        output = Util.to_number(buf, off + 14, 2);
        dPkts = Util.to_number(buf, off + 16, 4);
        dOctets = Util.to_number(buf, off + 20, 4);
        First = Util.to_number(buf, off + 24, 4);
        Last = Util.to_number(buf, off + 28, 4);
        srcport = Util.to_number(buf, off + 32, 2);
        dstport = Util.to_number(buf, off + 34, 2);
        tcp_flags = buf[off + 37];
        prot = buf[off + 38];
        tos = buf[off + 39];
        src_as = Util.to_number(buf, off + 40, 2);
        dst_as = Util.to_number(buf, off + 42, 2);
        src_mask = buf[off + 44];
        dst_mask = buf[off + 45];

        srcprefix = new Prefix(src_addr, src_mask);
        dstprefix = new Prefix(dst_addr, dst_mask);
        if (dPkts + dOctets <= 0) {
            throw new DoneException("Packet and octets error");
        }
    }
    /**
     * 印出所有資訊
     */
    public void printAll() {
        System.out.println(srcaddr+"\t"+dstaddr+"\t"+nexthop);
        System.out.println(input+"\t"+output+"\t"+dPkts+"\t"+dOctets+"\t"+First+"\t"+Last);
        System.out.println(srcport+"\t"+dstport);
        System.out.println(tcp_flags+"\t"+prot+"\t"+tos);
        System.out.println(src_as+"\t"+dst_as+"\t");
        System.out.println(srcprefix+"\t"+dstprefix);
    }

    public Long getSrcPort() {
        return new Long(srcport);
    }

    public Long getDstPort() {
        return new Long(dstport);
    }

    public Long getProto() {
        return new Long(prot);
    }

    public Long getTOS() {
        return new Long(tos);
    }

    public Long getSrcAS() {
        return new Long(src_as);
    }

    public Long getDstAS() {
        return new Long(dst_as);
    }

    public Address getSrcAddr() {
        return new Address(src_addr);
    }

    public Address getDstAddr() {
        return new Address(dst_addr);
    }

    public Address getNextHop() {
        return new Address(next_hop);
    }

    public Long getInIf() {
        return new Long(input);
    }

    public Long getOutIf() {
        return new Long(output);
    }

    public Prefix getSrcPrefix() {
        return srcprefix;
    }

    public Prefix getDstPrefix() {
        return dstprefix;
    }

    /**
     * (non-Javadoc)
     * @return
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(V5_Flow obj) {
        if ((this.dOctets == obj.dOctets) && (this.dPkts == obj.dPkts)
            && (this.dst_addr == obj.dst_addr)
            && (this.dst_as == obj.dst_as)
            && (this.dst_mask == obj.dst_mask)
            && (this.dstport == obj.dstport) && (this.First == obj.First)
            && (this.input == obj.input) && (this.Last == obj.Last)
            && (this.next_hop == obj.next_hop)
            && (this.output == obj.output) && (this.prot == obj.prot)
            && (this.src_addr == obj.src_addr)
            && (this.src_as == obj.src_as)
            && (this.src_mask == obj.src_mask)
            && (this.srcport == obj.srcport)
            && (this.tcp_flags == obj.tcp_flags) && (this.tos == obj.tos)) {
            return true;
        } else {
            return false;
        }
    }
}
