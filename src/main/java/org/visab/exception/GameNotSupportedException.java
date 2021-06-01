package org.visab.exception;

/**
 * Exception that is thrown when wants VISAB to process game information for an
 * unknown game.
 *
 * @author moritz
 *
 */
public class GameNotSupportedException extends RuntimeException {

    public GameNotSupportedException() {
    }

    public GameNotSupportedException(String message) {
        super(message);
    }
}
