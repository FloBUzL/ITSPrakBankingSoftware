package shared.superclassifragilistic;

import java.util.logging.Logger;

import shared.constants.Misc;

public abstract class ConnectionData {
    private Logger logger;

    public ConnectionData() {
	this.logger = Logger.getAnonymousLogger();
    }

    public void debug(String log) {
	if(Misc.DEBUG) {
	    this.logger.info(log);
	}
    }

    public void log(String log) {
	this.logger.info(log);
    }
}
