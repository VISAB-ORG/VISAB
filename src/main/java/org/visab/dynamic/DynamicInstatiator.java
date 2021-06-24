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
public final class DynamicInstatiator {

    private static Logger logger = LogManager.getLogger(DynamicInstatiator.class);

    /**
     * Instantiates a SessionListener based on the dynamic mappings configuration.
     * 
     * @param game      The game to instantiate a listener of
     * @param sessionId The sessionId
     * @return A SessionListener instance if successful, null else
     */
    public static ISessionListener<?> instantiateSessionListener(String game, UUID sessionId) {
        var className = "";

        var mapping = Workspace.getInstance().getConfigManager().getMapping(game);
        if (mapping != null && mapping.getListener() != null)
            className = mapping.getListener();

        ISessionListener<?> sessionListener = null;
        if (className.isBlank()) {
            // Instantiate a default listener
            sessionListener = new DefaultSessionListener(game, sessionId);
        } else {
            var instance = instatiateClass(className, sessionId);
            if (instance == null) {
                logger.error("Couldent create instance of " + className);
            } else {
                try {
                    sessionListener = (ISessionListener<?>) instance;
                } catch (Exception e) {
                    logger.error(StringFormat.niceString("Failed to cast instance of {0} to ISessionListener<?>.",
                            className));
                }
            }
        }

        return sessionListener;
    }

    /**
     * Instantiates an object of a given class name using the fitting constructor
     * for the given parameters.
     * 
     * @param className The fully classified class name of the class to instantiate
     * @param arguments The constructor arguments
     * @return An instance of the class if successful, null else
     */
    public static Object instatiateClass(String className, Object... arguments) {
        var class_ = DynamicHelper.tryGetClass(className);
        if (class_ == null) {
            logger.error("Couldent resove class for " + className);
            return null;
        } else {
            return instantiateClass(class_, arguments);
        }
    }

    /**
     * Instantiates an object of a given class name using the fitting constructor
     * for the given arguments.
     * 
     * @param class_    The class to instantiate
     * @param arguments The constructor arguments
     * @return An instance of the class if successful, null else
     */
    public static Object instantiateClass(Class<?> class_, Object... arguments) {
        if (class_ == null) {
            logger.info("Cant create an instance of null.");
            return null;
        }

        // Start by getting the right constructor
        var constructors = getConstructors(class_);

        Constructor<?> correctConstructor = null;
        for (var ctor : constructors) {
            var ctorParameters = ctor.getParameters();

            // Find the constructor whose parameters types match the given params
            // By nature, selects the first constructor that matches
            var isCorrect = true;
            if (ctorParameters.length == arguments.length) {
                for (int i = 0; i < arguments.length; i++) {
                    var ctorParam = ctorParameters[i];
                    var param = arguments[i];

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

        // If there was not correct constructor found, throw exception
        if (correctConstructor == null) {
            var message = StringFormat.niceString("Couldent find a fitting constructor for {0}.", class_.getName());
            logger.error(message);
            return null;
        }

        Object instance = null;
        try {
            instance = correctConstructor.newInstance(arguments);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            // If instatiation via reflection fails eventhought constructor was found, throw
            // exception
            var message = StringFormat.niceString("Failed to create an instance of {0} using constructor {1}.",
                    class_.getName(), correctConstructor);

            logger.error(message);
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
    private static List<Constructor<?>> getConstructors(Class<?> class_) {
        var constructors = new ArrayList<Constructor<?>>();

        try {
            for (var ctor : class_.getConstructors())
                constructors.add(ctor);
        } catch (SecurityException e) {
            logger.warn(StringFormat.niceString("Failed to get constructors for {0}.", class_.getName()));
            e.printStackTrace();
        }

        return constructors;
    }

    public static void main(String[] args) {
        var cbrListenerName = "org.visab.processing.cbrshooter.CBRShooterListener";
        var dyna = new DynamicInstatiator();

        var listener = DynamicInstatiator.instantiateSessionListener("CBRShooter", UUID.randomUUID());

        var uuid = listener.getSessionId();
    }

}
