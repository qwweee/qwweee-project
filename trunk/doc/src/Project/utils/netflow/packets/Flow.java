package Project.utils.netflow.packets;

import Project.utils.netflow.Address;
import Project.utils.netflow.Prefix;

/**
 * @author bbxp
 * 抽象物件
 */
abstract public class Flow {
	/**
	 * @return
	 */
	public Long getSrcPort() {
		return null;
	}

	/**
	 * @return
	 */
	public Long getDstPort() {
		return null;
	}

	/**
	 * @return
	 */
	public Long getProto() {
		return null;
	}
    
    /**
     * @return
     */
	public Long getTOS() {
		return null;
	}

    /**
     * @return
     */
	public Long getSrcAS() {
		return null;
	}

    /**
     * @return
     */
	public Long getDstAS() {
		return null;
	}

    /**
     * @return
     */
	public Address getSrcAddr() {
		return null;
	}

    /**
     * @return
     */
	public Address getDstAddr() {
		return null;
	}

    /**
     * @return
     */
	public Address getNextHop() {
		return null;
	}

    /**
     * @return
     */
	public Long getInIf() {
		return null;
	}

    /**
     * @return
     */
	public Long getOutIf() {
		return null;
	}

    /**
     * @return
     */
	public Prefix getSrcPrefix() {
		return null;
	}

    /**
     * @return
     */
	public Prefix getDstPrefix() {
		return null;
	}
}
