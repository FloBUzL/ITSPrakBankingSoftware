package shared.exception;

public class GeneralException extends Exception {
    private static final long serialVersionUID = -1726519269072204026L;

    public GeneralException() {

    }

    public GeneralException(String msg) {
	super(msg);
    }
}
