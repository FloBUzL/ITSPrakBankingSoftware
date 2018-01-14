package shared.exception;

public class NoSuchTaskException extends Exception {
    private static final long serialVersionUID = -7656740827413441076L;

    public NoSuchTaskException(String message) {
	super(message);
    }
}
