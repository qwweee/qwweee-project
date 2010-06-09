package Project.utils.netflow;

/**
 * @author bbxp
 *
 */
public class Prefix extends Address {
	/**
	 * 
	 */
	public byte mask;

	/**
	 * 
	 */
	public long dmask;//����

	/**
	 * @param address
	 * @param mask
	 */
	public Prefix(long address, byte mask) {
		super(address);

		dmask = ~((1 << (32 - (mask & 0xff))) - 1);
		this.address = mask <= 0 || mask >= 32 ? address : address & dmask;
		this.mask = mask;
	}

	public String toString() {
		return IpSegmentManager.convertIP(address) + "/" + mask;
	}

	/**
	 * @param o
	 * @return
	 */
	public boolean equals(Prefix o) {
		return mask == o.mask && address == o.address;
	}

	/**
	 * @param addr
	 * @return
	 */
	public boolean consists(Address addr) {
		return (mask <= 0 || mask >= 32 ? addr.address : addr.address & dmask) == address;
	}

	/**
	 * @param value
	 * @return
	 */
	public boolean includes(Prefix value) {
		if (value.mask < mask)
			return false;

		if (equals(value))
			return true;

		return consists(value);
	}
}
