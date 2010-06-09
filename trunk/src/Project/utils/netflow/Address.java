package Project.utils.netflow;

/**
 * @author bbxp
 *
 */
public class Address {
	/**
	 * 
	 */
	public long address;

	/**
	 * @param address
	 */
	public Address(long address) {
		this.address = address;
	}
	/**
	 * 將Address轉成String
	 */
	public String toString() {
		return IpSegmentManager.convertIP(address);
	}
	/**
	 * @param o
	 * @return
	 */
	public boolean equals(Address o) {
		return address == o.address;
	}
}
