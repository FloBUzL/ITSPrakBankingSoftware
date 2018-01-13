package shared.superclassifragilistic;

import java.util.logging.Logger;

import shared.constants.Misc;

public abstract class Worker {
    protected boolean succeeded = false;
    private Logger logger = Logger.getAnonymousLogger();

    abstract public Worker setup();

    abstract public Worker run();

    public boolean isSucceeded() {
	return this.succeeded;
    }

    protected void debug(String message) {
	if(Misc.DEBUG) {
	    this.logger.info(message);
	}
    }
}
