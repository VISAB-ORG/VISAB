package org.visab.exception;

/**
 * Exception that is thrown when Dynamic classes fail at dynamically creating an
 * instance of a java object for a given class name. Generally this exception
 * indicates that the given classMapping.json has faulty class names.
 */
public class DynamicException extends RuntimeException {

    public DynamicException() {
    }

    public DynamicException(String message) {
        super(message);
    }

}
