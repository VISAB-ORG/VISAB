package org.visab.dynamic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.util.NiceString;

/**
 * Class containing helper methods used by the dynamic classes.
 */
public final class DynamicHelper {

    private static final Logger logger = LogManager.getLogger(DynamicHelper.class);

    /**
     * Tries to get a Class<?> object for a given class name.
     * 
     * @param className The fully classified class name
     * @return The Class<?> object if successful, null else
     */
    public static final Class<?> tryGetClass(String className) {
        Class<?> _class = null;

        if (className != null && !className.isBlank()) {
            try {
                _class = Class.forName(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                logger.error(NiceString.make("Failed to find class for name {0}.", className));
            }
        }

        return _class;
    }
}
