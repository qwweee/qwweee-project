package Project.utils.netflow;

/**
 * @author bbxp
 *
 */
public abstract class Util {
	/**
	 * @param i
	 * @return
	 */
	public static byte IntToByte(int i) {
		return (byte) i;
	}

	/**
	 * @param abyte0
	 * @param offset
	 * @return
	 */
	public static int BytesToInt(byte abyte0[], int offset) {
		return (int)to_number(abyte0,offset,2);
	}

	/**
	 * @param i
	 * @return
	 */
	public static byte[] IntToBytes(int i) {
		byte abyte0[] = new byte[2];
		abyte0[1] = (byte) (0xff & i);
		abyte0[0] = (byte) ((0xff00 & i) >> 8);
		return abyte0;
	}

	/**
	 * @param i
	 * @return
	 */
	public static byte[] IntToBytes4(int i) {
		byte abyte0[] = new byte[4];
		abyte0[3] = (byte) (0xff & i);
		abyte0[2] = (byte) ((0xff00 & i) >> 8);
		abyte0[1] = (byte) ((0xff0000 & i) >> 16);
		abyte0[0] = (byte) ((0xff000000 & i) >> 24);
		return abyte0;
	}

	/**
	 * @param l
	 * @return
	 */
	public static byte[] LongToBytes8(long l) {
		byte abyte0[] = new byte[8];
		abyte0[7] = (byte) (int) (255L & l);
		abyte0[6] = (byte) (int) ((65280L & l) >> 8);
		abyte0[5] = (byte) (int) ((0xff0000L & l) >> 16);
		abyte0[4] = (byte) (int) ((0xff000000L & l) >> 24);
		abyte0[3] = (byte) (int) ((0xff00000000L & l) >> 32);
		abyte0[2] = (byte) (int) ((0xff0000000000L & l) >> 40);
		abyte0[1] = (byte) (int) ((0xff000000000000L & l) >> 48);
		abyte0[0] = (byte) (int) ((0xff00000000000000L & l) >> 56);
		return abyte0;
	}

	/**
	 * @param abyte0
	 * @param offset
	 * @return
	 */
	public static long Bytes8ToLong(byte abyte0[], int offset) {
		return (255L & (long) abyte0[offset]) << 56
				| (255L & (long) abyte0[offset + 1]) << 48
				| (255L & (long) abyte0[offset + 2]) << 40
				| (255L & (long) abyte0[offset + 3]) << 32
				| (255L & (long) abyte0[offset + 4]) << 24
				| (255L & (long) abyte0[offset + 5]) << 16
				| (255L & (long) abyte0[offset + 6]) << 8
				| (255L & (long) abyte0[offset + 7]);
	}

	/**
	 * @param l
	 * @param abyte0
	 */
	public static void LongToBytes4(long l, byte abyte0[]) {
		abyte0[3] = (byte) (int) (255L & l);
		abyte0[2] = (byte) (int) ((65280L & l) >> 8);
		abyte0[1] = (byte) (int) ((0xff0000L & l) >> 16);
		abyte0[0] = (byte) (int) ((0xffffffffff000000L & l) >> 24);
	}

	/**
	 * @param i
	 * @param abyte0
	 */
	public static void IntToBytes(int i, byte abyte0[]) {
		abyte0[1] = (byte) (0xff & i);
		abyte0[0] = (byte) ((0xff00 & i) >> 8);
	}

	/**
	 * @param i
	 * @param abyte0
	 */
	public static void IntToBytes4(int i, byte abyte0[]) {
		abyte0[3] = (byte) (0xff & i);
		abyte0[2] = (byte) ((0xff00 & i) >> 8);
		abyte0[1] = (byte) ((0xff0000 & i) >> 16);
		abyte0[0] = (byte) (int) ((0xffffffffff000000L & (long) i) >> 24);
	}

	/**
	 * @param abyte0
	 * @param offset
	 * @return
	 */
	public static int Bytes4ToInt(byte abyte0[], int offset) {
		return (0xff & abyte0[offset]) << 24
				| (0xff & abyte0[offset + 1]) << 16
				| (0xff & abyte0[offset + 2]) << 8 | 0xff & abyte0[offset + 3];
	}

	/**
	 * @param abyte0
	 * @param offset
	 * @return
	 */
	public static long Bytes4ToLong(byte abyte0[], int offset) {
		return (255L & (long) abyte0[offset + 0]) << 24
				| (255L & (long) abyte0[offset + 1]) << 16
				| (255L & (long) abyte0[offset + 2]) << 8 | 255L
				& (long) abyte0[offset + 3];
	}

	/**
	 * @param p
	 * @param off
	 * @param len
	 * @return
	 */
	static public final long to_number(byte[] p, int off, int len) {
		long ret = 0;
		int done = off + len;
		for (int i = off; i < done; i++)
			ret = ((ret << 8) & 0xffffffff) + (p[i] & 0xff);

		return ret;
	}
        /**
         * @param args
         */
        public static void main(String[] args){
            //
            byte[] t = new byte[]{3,-1,54,-2,34,6,7,5,0,7,4};
//            System.out.println(BytesToInt(t,2));
            System.out.println(to_number(t,2,2));
        }
	static private final String value(long num, String msg) {
		if (num == 0)
			return "";

		return (num == 1 ? "1 " + msg : num + " " + msg + "s") + ", ";
	}

	/**
	 * @param time
	 * @return
	 */
	static public final String uptime(long time) {
		if (time == 0)
			return "0 seconds";

		if (time < 0)
			return time + "(Negative?!)";

		long sec = time % 60;
		long min = (time / 60) % 60;
		long hour = (time / 60 / 60) % 24;
		long day = time / 60 / 60 / 24;

		String ret = value(day, "day") + value(hour, "hour")
				+ value(min, "minute") + value(sec, "second");
		return ret.substring(0, ret.length() - 2);
	}

	static private final char digits[] = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9' };

	static private final String value1(long l) {
		return "" + digits[(int) (l / 10) % 10] + digits[(int) l % 10];
	}

	/**
	 * @param time
	 * @return
	 */
	static public final String uptime_short(long time) {
		if (time == 0)
			return "00:00";

		if (time < 0)
			return time + "(Negative?!)";

		long sec = time % 60;
		long min = (time / 60) % 60;
		long hour = (time / 60 / 60) % 24;
		long day = time / 60 / 60 / 24;

		return value1(day) + '-' + value1(hour) + ':' + value1(min) + ':'
				+ value1(sec);
	}

	/**
	 * ת������IP��ַ��string
	 *
	 * @param addr
	 * @return
	 */
	static public final String str_addr(long addr) {
		// return new String(((addr >> 24) & 0xff) + "." + ((addr >> 16) & 0xff)
		// + "." + ((addr >> 8) & 0xff) + "." + (addr & 0xff));
	    return IpSegmentManager.getSegNameByIP(addr);
//	    return IpSegmentManager.convertIP(addr);
	}

	/**
	 * @param i
	 * @return
	 */
	static public final String toInterval(long i) {
		if (i < 60)
			return i + "S";

		if (i < 3600)
			return (i / 60) + "M";

		return (i / 3600) + "H";
	}

	/**
	 * @param ip
	 * @return
	 */
	public static final long convertIPS2Long(String ip) {
		char[] c = ip.toCharArray();
		byte[] b = { 0, 0, 0, 0 };
		for (int i = 0, j = 0; i < c.length;) {
			int d = (byte) (c[i] - '0');
			switch (c[i++]) {
			case '.':
				++j;
				break;
			default:
				if ((d < 0) || (d > 9))
					return 0;
				if ((b[j] & 0xff) * 10 + d > 255)
					return 0;
				b[j] = (byte) (b[j] * 10 + d);
			}
		}
		return 0x00000000ffffffffl & (b[0] << 24 | (b[1] & 0xff) << 16 | (b[2] & 0xff) << 8
				| (b[3] & 0xff));
	}
}
