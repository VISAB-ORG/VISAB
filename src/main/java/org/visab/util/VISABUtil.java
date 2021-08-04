package org.visab.util;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.main.Main;

import javafx.scene.control.TableView;

/**
 * Class containing various, non specific helper methods.
 *
 * @author VISAB 1.0 and 2.0 group
 */
public final class VISABUtil {

    // Logger needs .class for each class to use for log traces
    private static Logger logger = LogManager.getLogger(VISABUtil.class);

    private static String[] acceptedExternalDataEndings = { ".txt" };

    @SuppressWarnings("rawtypes")
    public static void clearTable(TableView statisticsTable) {
        statisticsTable.getColumns().clear();
        statisticsTable.getItems().clear();
    }

    public static List<List<String>> convertStringToList(String content) {

        // Create List to store the data
        List<List<String>> data = new ArrayList<List<String>>();

        // Filter content of file & store in HashMap
        var m = Pattern.compile("\\[(.*?)=(.*?)\\]").matcher(content);
        while (m.find()) {
            List<String> temp = new ArrayList<String>();
            temp.add(m.group(1));
            temp.add(m.group(2));
            data.add(temp);
        }

        return data;
    }

    public enum OS {
        WINDOWS, LINUX, MAC, SOLARIS
    }

    private static OS os = null;

    /**
     * Gets the OS that VISAB is currenlty running on
     * 
     * @return The OS enum
     */
    public static OS getOS() {
        if (os == null) {
            String operSys = System.getProperty("os.name").toLowerCase();
            if (operSys.contains("win")) {
                os = OS.WINDOWS;
            } else if (operSys.contains("nix") || operSys.contains("nux") || operSys.contains("aix")) {
                os = OS.LINUX;
            } else if (operSys.contains("mac")) {
                os = OS.MAC;
            } else if (operSys.contains("sunos")) {
                os = OS.SOLARIS;
            }
        }
        return os;
    }

    public static String[] getAcceptedExternalDataEndings() {
        return acceptedExternalDataEndings;
    }

    public static String getAcceptedExternalDataEndingsAsString() {
        var output = new StringBuilder();
        for (String acceptedExternalDataEnding : acceptedExternalDataEndings)
            output.append(acceptedExternalDataEnding);
        return output.toString();
    }

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
