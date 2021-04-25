package org.visab.exception;

/**
 * Exception that is thrown when wants VISAB to process game information for an
 * unknown game.
 *
 * @author moritz
 *
 */
public class GameNotSupportedException extends Exception {

    private static final long serialVersionUID = 5354989043165402188L;

    public GameNotSupportedException() {
    }

    public GameNotSupportedException(String message) {
	super(message);
    }
}
