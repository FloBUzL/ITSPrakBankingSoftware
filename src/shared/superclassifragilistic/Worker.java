package shared.superclassifragilistic;

/**
 * abstract worker class
 * 
 * @author Florian Berberich
 */
public abstract class Worker {
	protected boolean succeeded = false;

	/**
	 * returns if the worker succeeded
	 * 
	 * @return
	 */
	public boolean isSucceeded() {
		return this.succeeded;
	}

	/**
	 * runs the server
	 * 
	 * @return the object itself
	 * @throws Exception
	 */
	abstract public Worker run() throws Exception;

	/**
	 * should setup all needed objects
	 * 
	 * @return the object itself
	 */
	abstract public Worker setup();
}
