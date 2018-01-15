package shared.constants;

/**
 * some constants
 * 
 * @author Florian
 */
public class Misc {
	/**
	 * if debugging is active
	 */
	public static final boolean DEBUG = true;
	/**
	 * if the permanent registering of devices is allowed
	 */
	public static final boolean ALLOW_PERMANENT_DEVICES = true;
	/**
	 * if mails should be sent
	 */
	public static final boolean SEND_MAIL = false;

	/**
	 * maximum auth attempts, a host has
	 */
	public static final int MAX_ATTEMPTS_HOST = 5;
	/**
	 * maximum auth attempts, a user has
	 */
	public static final int MAX_ATTEMPTS_USER = 3;
	/**
	 * seconds to wait til decreasing the host attempts
	 */
	public static final int TIMER_DECREASE_ATTEMPTS_HOST = 1000 * 60 * 45;
	/**
	 * seconds to wait til decreasing the user attempts
	 */
	public static final int TIMER_DECREASE_ATTEMPTS_USER = 1000 * 60 * 15;
}
