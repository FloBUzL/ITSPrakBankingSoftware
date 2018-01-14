package server.data;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import shared.constants.Misc;

public class AuthenticationErrors {
    private ConcurrentHashMap<String,Integer> userErrors;
    private ConcurrentHashMap<String,Integer> hostErrors;
    private Logger logger;

    public AuthenticationErrors() {
        this.userErrors = new ConcurrentHashMap<String, Integer>();
        this.hostErrors = new ConcurrentHashMap<String, Integer>();
        this.logger = Logger.getAnonymousLogger();

        new Timer().schedule(new DecreaseHostAttempts(this), Misc.TIMER_DECREASE_ATTEMPTS_HOST, Misc.TIMER_DECREASE_ATTEMPTS_HOST);
        new Timer().schedule(new DecreaseUserAttempts(this), Misc.TIMER_DECREASE_ATTEMPTS_USER, Misc.TIMER_DECREASE_ATTEMPTS_USER);
    }

    public void increase(String host) {
        this.increase(host, null);
    }

    public void increase(String host,String user) {
        int amount = 0;
        if(this.hostErrors.containsKey(host)) {
            amount = this.hostErrors.get(host)+1;
        }
        this.hostErrors.put(host, amount);
        this.logger.info("set authentication errors for host " + host + " to " + amount);

        if(user == null) {
            return;
        }
        if(this.userErrors.containsKey(user)) {
            amount = this.userErrors.get(user)+1;
        }
        this.userErrors.put(user, amount);
        this.logger.info("set authentication errors for user " + user + " to " + amount);
    }

    public boolean isHostBlocked(String host) {
        return this.hostErrors.containsKey(host) && (this.hostErrors.get(host) > Misc.MAX_ATTEMPTS_HOST);
    }

    public boolean isUserBlocked(String user) {
        return this.userErrors.containsKey(user) && (this.userErrors.get(user) > Misc.MAX_ATTEMPTS_USER);
    }

    public void decreaseUserAttempts() {
        int amount = 0;
        for(String host : this.hostErrors.keySet()) {
            amount = this.hostErrors.get(host);
            if(amount > 0) {
                amount -= 1;
            }
            this.hostErrors.put(host, amount);
            this.logger.info("set authentication errors for host " + host + " to " + amount);
        }
    }

    public void decreaseHostAttempts() {
        int amount = 0;
        for(String user : this.userErrors.keySet()) {
            amount = this.userErrors.get(user);
            if(amount > 0) {
                amount -= 1;
            }
            this.userErrors.put(user, amount);
            this.logger.info("set authentication errors for user " + user + " to " + amount);
        }
    }
}

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
