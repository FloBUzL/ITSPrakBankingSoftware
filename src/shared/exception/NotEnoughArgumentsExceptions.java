package shared.exception;

public class NotEnoughArgumentsExceptions extends Exception {
    private static final long serialVersionUID = -1341456273219324385L;

    public NotEnoughArgumentsExceptions() {

    }

    public NotEnoughArgumentsExceptions(String message) {
	super(message);
    }
}
