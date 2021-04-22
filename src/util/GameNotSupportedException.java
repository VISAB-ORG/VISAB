package util;

public class GameNotSupportedException extends Exception {

    private static final long serialVersionUID = 5354989043165402188L;

    public GameNotSupportedException() {
    }

    public GameNotSupportedException(String message) {
	super(message);
    }
}
