package org.visab.dynamic;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.processing.ISessionListener;
import org.visab.processing.starter.DefaultSessionListener;
import org.visab.util.StringFormat;
import org.visab.workspace.Workspace;

/**
 * The DyanmicInstatiator using which objects can be instantiates from a fully
 * classified class name. Is used for creating SessionListener instances.
 */
public final class DyanmicInstatiator {

    private static Logger logger = LogManager.getLogger(DyanmicInstatiator.class);

    /**
     * Instantiates a SessionListener based on the dynamic mappings configuration.
     * 
     * @param game      The game to instantiate a listener of
     * @param sessionId The sessionId
     * @return A SessionListener object if successful, throws exception if not
     *         succesful (cast failed). This exception shouldent be caught at
     *         runtime, since it proves a fatal error for the system and makes it
     *         unuseable.
     */
    public static ISessionListener<?> instantiateSessionListener(String game, UUID sessionId) {
        var className = "";

        var mapping = Workspace.instance.getConfigManager().getMapping(game);
        if (mapping != null && mapping.getListener() != null)
            className = mapping.getListener();

        ISessionListener<?> sessionListener = null;
        if (className.isBlank()) {
            // Instantiate a default listener
            sessionListener = new DefaultSessionListener(game, sessionId);
        } else {
            sessionListener = (ISessionListener<?>) instatiateClass(className, sessionId);
        }

        return sessionListener;
    }

    /**
     * Instantiates an object of a given class name using the fitting constructor
     * for the given parameters.
     * 
     * @param className The fully classified class name of the class to instantiate
     * @param params    The constructor parameters
     * @return An instance of the class if successful, null else
     */
    public static Object instatiateClass(String className, Object... params) {
        // Start by getting the right constructor
        var constructors = getConstructors(className);

        Constructor<?> correctConstructor = null;
        for (var ctor : constructors) {
            var ctorParameters = ctor.getParameters();

            // Find the constructor whose parameters types match the given params
            // By nature, selects the first constructor that matches
            var isCorrect = true;
            if (ctorParameters.length == params.length) {
                for (int i = 0; i < params.length; i++) {
                    var ctorParam = ctorParameters[i];
                    var param = params[i];

                    if (ctorParam.getType() != param.getClass()) {
                        isCorrect = false;
                        break;
                    }
                }

                // Stop loop if current constructor had right parameters types
                if (isCorrect) {
                    correctConstructor = ctor;
                    break;
                }
            }
        }

        if (correctConstructor == null) {
            logger.warn(StringFormat.niceString("Couldent find a fitting constructor for {0}", className));
            return null;
        }

        Object instance = null;
        try {
            instance = correctConstructor.newInstance(params);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            logger.fatal(StringFormat.niceString("Failed to create an instance of {0} using constructor {1}", className,
                    correctConstructor));
            e.printStackTrace();
        }

        return instance;
    }

    /**
     * Gets the constructors for a given class name.
     * 
     * @param className The fully classified class name of the class to get the
     *                  constructors of
     * @return A list of the constructors
     */
    private static List<Constructor<?>> getConstructors(String className) {
        var constructors = new ArrayList<Constructor<?>>();

        try {
            for (var ctor : Class.forName(className).getConstructors())
                constructors.add(ctor);
        } catch (SecurityException | ClassNotFoundException e) {
            logger.warn(StringFormat.niceString("Failed to get constructors for {0}", className));
            e.printStackTrace();
        }

        return constructors;
    }

    public static void main(String[] args) {
        var cbrListenerName = "org.visab.processing.cbrshooter.CBRShooterListener";
        var dyna = new DyanmicInstatiator();

        var listener = dyna.instantiateSessionListener("CBRShooter", UUID.randomUUID());

        var uuid = listener.getSessionId();
    }

}
