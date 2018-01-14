package shared.superclassifragilistic;

import java.util.logging.Logger;

import shared.constants.Misc;

/**
 * abstract class for connection data
 * @author Florian
 */
public abstract class ConnectionData {
    private Logger logger;

    /**
     * initializes the logger
     */
    public ConnectionData() {
	this.logger = Logger.getAnonymousLogger();
    }

    /**
     * logs the string if debugging is active
     * @param log the string to log
     */
    public void debug(String log) {
	if(Misc.DEBUG) {
	    this.logger.info(log);
	}
    }

    /**
     * logs the string
     * @param log the string to log
     */
    public void log(String log) {
	this.logger.info(log);
    }
}
