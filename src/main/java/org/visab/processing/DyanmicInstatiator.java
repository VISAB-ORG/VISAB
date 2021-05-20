package org.visab.processing;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.visab.processing.starter.DefaultSessionListener;

public class DyanmicInstatiator {

    public DyanmicInstatiator() {
    }

    // TODO: This is loaded from the workspace (saved under ViewMapping or a
    // different name)
    public Map<String, String> listenerMap = new HashMap<>();

    public ISessionListener<?> instantiateSessionListener(String game, UUID sessionId) {
        var className = listenerMap.getOrDefault(game, "");

        ISessionListener<?> sessionListener = null;
        // Instantiate the base class
        if (className.isBlank()) {
            sessionListener = new DefaultSessionListener(game, sessionId);
        } else {
            sessionListener = (ISessionListener<?>) instatiateClass(className, sessionId);
        }

        return sessionListener;
    }

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
            // TODO: Log
            return null;
        }

        Object instance = null;
        try {
            instance = correctConstructor.newInstance(params);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            // TODO: Log
            e.printStackTrace();
        }

        return instance;
    }

    private static List<Constructor<?>> getConstructors(String className) {
        var constructors = new ArrayList<Constructor<?>>();

        try {
            for (var ctor : Class.forName(className).getConstructors())
                constructors.add(ctor);
        } catch (SecurityException | ClassNotFoundException e) {
            e.printStackTrace();
            // TODO: Log
        }

        return constructors;
    }

    public static void main(String[] args) {
        var cbrListenerName = "org.visab.processing.cbrshooter.CBRShooterListener";
        var dyna = new DyanmicInstatiator();
        dyna.listenerMap.put("CBRShooter", cbrListenerName);

        var listener = dyna.instantiateSessionListener("CBRShooter", UUID.randomUUID());

        var uuid = listener.getSessionId();
    }

}
