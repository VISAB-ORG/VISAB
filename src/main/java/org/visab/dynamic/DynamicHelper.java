package org.visab.dynamic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.exception.DynamicException;
import org.visab.util.StringFormat;

public final class DynamicHelper {

    private static Logger logger = LogManager.getLogger(DynamicHelper.class);

    /**
     * Tries to get a Class<?> object for a given class name.
     * 
     * @param className The fully classified class name
     * @return The Class<?> object if successful, null else
     */
    public static Class<?> tryGetClass(String className) {
        Class<?> _class = null;

        if (className != null && !className.isBlank()) {
            try {
                _class = Class.forName(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();

                // If there was a class name given, but it couldent be resolved throw exception
                var message = StringFormat.niceString("Failed to find class for name {0}.", className);
                logger.fatal(message);
                throw new DynamicException(message);
            }
        }

        return _class;
    }
}
