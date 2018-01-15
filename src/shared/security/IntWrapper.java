package shared.security;

/**
 * wrapper object for an int
 * 
 * @author Florian
 */
public class IntWrapper {
	private int value;

	/**
	 * initializes the object with 0
	 */
	public IntWrapper() {
		this(0);
	}

	/**
	 * initializes object with init
	 * 
	 * @param init
	 *            the value for init
	 */
	public IntWrapper(int init) {
		this.value = init;
	}

	/**
	 * adds a value to the object's value
	 * 
	 * @param val
	 *            the value to add
	 */
	public void add(int val) {
		this.value += val;
	}

	/**
	 * getter for the value
	 * 
	 * @return the object's value
	 */
	public int get() {
		return this.value;
	}

	/**
	 * set the value
	 * 
	 * @param value
	 *            the new value
	 */
	public void set(int value) {
		this.value = value;
	}

	/**
	 * converts the object's data to a string
	 */
	public String toString() {
		return this.value + "";
	}
}
