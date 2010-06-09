package Project.utils.netflow;

/**
 * @author bbxp
 *
 */
public class DoneException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2257630368096030109L;

	String message;

	/**
	 * @param message
	 */
	public DoneException(String message) {
		this.message = message;
	}

	public String toString() {
		return message;
	}
}
