package org.visab.util;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.visab.main.Main;


/**
 * Class containing various, non specific helper methods.
 *
 * @author VISAB 1.0 and 2.0 group
 */
public final class VISABUtil {

    /**
     * Combines path strings to one string
     * 
     * @param path The base path
     * @param more The paths to add
     * @return The combined path
     */
    public static final String combinePath(String path, String... more) {
        return Path.of(path, more).toString();
    }

    /**
     * This method properly checks from within code, if the application is ran as a
     * jar or not.
     * 
     * @return True if ran from jar
     */
    public static final boolean isRunningFromJar() {
        String className = Main.class.getName().replace('.', '/');
        String classJar = Main.class.getResource("/" + className + ".class").toString();
        if (classJar.startsWith("jar:")) {
            return true;
        }
        return false;
    }

    /**
     * Recursively gets all implemented interfaces for a class.
     * 
     * @param class_ The class to get the interfaces for.
     * @return All implemented interfaces
     */
    public static final List<Class<?>> getAllInterfaces(Class<?> class_) {
        return getAllInterfaces(class_, new ArrayList<Class<?>>());
    }

    /**
     * Recursively gets all implemented interfaces for a class.
     * 
     * @param class_ The class to get the interfaces for.
     * @return All implemented interfaces
     */
    private static final List<Class<?>> getAllInterfaces(Class<?> class_, List<Class<?>> list) {
        var interfaces = class_.getInterfaces();
        for (int i = 0; i < interfaces.length; i++) {
            list.add(interfaces[i]);
        }

        var superClass = class_.getSuperclass();
        if (superClass != null)
            getAllInterfaces(superClass, list);

        return list;
    }

}
