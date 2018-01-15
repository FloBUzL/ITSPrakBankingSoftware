package server.data;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import shared.constants.Misc;

/**
 * holds the auth errors
 * 
 * @author Florian
 */
public class AuthenticationErrors {
	private ConcurrentHashMap<String, Integer> userErrors;
	private ConcurrentHashMap<String, Integer> hostErrors;

	/**
	 * the constructor; sets up the timers
	 */
	public AuthenticationErrors() {
		this.userErrors = new ConcurrentHashMap<String, Integer>();
		this.hostErrors = new ConcurrentHashMap<String, Integer>();

		new Timer().schedule(new DecreaseHostAttempts(this), Misc.TIMER_DECREASE_ATTEMPTS_HOST,
				Misc.TIMER_DECREASE_ATTEMPTS_HOST);
		new Timer().schedule(new DecreaseUserAttempts(this), Misc.TIMER_DECREASE_ATTEMPTS_USER,
				Misc.TIMER_DECREASE_ATTEMPTS_USER);
	}

	/**
	 * decreases the counter for all hosts
	 */
	public void decreaseHostAttempts() {
		int amount = 0;
		for (String user : this.userErrors.keySet()) {
			amount = this.userErrors.get(user);
			if (amount > 0) {
				amount -= 1;
			}
			this.userErrors.put(user, amount);
		}
	}

	/**
	 * decreases the counter for all users
	 */
	public void decreaseUserAttempts() {
		int amount = 0;
		for (String host : this.hostErrors.keySet()) {
			amount = this.hostErrors.get(host);
			if (amount > 0) {
				amount -= 1;
			}
			this.hostErrors.put(host, amount);
		}
	}

	/**
	 * increases the error counter for a host
	 * 
	 * @param host
	 *            the client's host
	 */
	public void increase(String host) {
		this.increase(host, null);
	}

	/**
	 * increases the error counter for a host and user
	 * 
	 * @param host
	 *            the client's hosts
	 * @param user
	 *            the client's user name
	 */
	public void increase(String host, String user) {
		int amount = 0;
		if (this.hostErrors.containsKey(host)) {
			amount = this.hostErrors.get(host) + 1;
		}
		this.hostErrors.put(host, amount);

		if (user == null) {
			return;
		}
		if (this.userErrors.containsKey(user)) {
			amount = this.userErrors.get(user) + 1;
		}
		this.userErrors.put(user, amount);
	}

	/**
	 * checks if a host is blocked
	 * 
	 * @param host
	 *            the host to check
	 * @return true if the host is blocked
	 */
	public boolean isHostBlocked(String host) {
		return this.hostErrors.containsKey(host) && (this.hostErrors.get(host) > Misc.MAX_ATTEMPTS_HOST);
	}

	/**
	 * check if a user is blocked
	 * 
	 * @param user
	 *            the username to check
	 * @return true if the user is blocked
	 */
	public boolean isUserBlocked(String user) {
		return this.userErrors.containsKey(user) && (this.userErrors.get(user) > Misc.MAX_ATTEMPTS_USER);
	}
}

/**
 * timertask for decreasing host counters
 * 
 * @author Florian
 */
class DecreaseHostAttempts extends TimerTask {
	private AuthenticationErrors errors;

	public DecreaseHostAttempts(AuthenticationErrors errors) {
		this.errors = errors;
	}

	@Override
	public void run() {
		this.errors.decreaseHostAttempts();
	}
}

/**
 * timertask for decreasing user counters
 * 
 * @author Florian
 */
class DecreaseUserAttempts extends TimerTask {
	private AuthenticationErrors errors;

	public DecreaseUserAttempts(AuthenticationErrors errors) {
		this.errors = errors;
	}

	@Override
	public void run() {
		this.errors.decreaseUserAttempts();
	}
}
