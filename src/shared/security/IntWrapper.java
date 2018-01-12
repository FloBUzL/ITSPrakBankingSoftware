package shared.security;

public class IntWrapper {
	private int value;

	public IntWrapper() {
		this(0);
	}

	public IntWrapper(int init) {
		this.value = init;
	}

	public void set(int value) {
		this.value = value;
	}

	public int get() {
		return this.value;
	}

	public void add(int val) {
		this.value += val;
	}

	public String toString() {
		return this.value + "";
	}
}
