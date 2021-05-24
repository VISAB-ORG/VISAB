package org.visab.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.main.Main;

import javafx.scene.control.TableView;

/**
 * Class containing various helper methods
 *
 * @author VISAB 1.0 and 2.0 group
 */

// @TODO rename this cunt
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
     * Gets the path to the directory where the currently running jar file is
     * located in.
     *
     * @return The path to the directory of the currently running jar file.
     */
    public static String getRunningJarRootDirPath() {
        var runningJarFile = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        var pathToJar = runningJarFile.getAbsolutePath();

        return pathToJar.replace(runningJarFile.getName(), "");
    }

    /**
     * Combines path strings to one string
     * 
     * @param path The base path
     * @param more The paths to add
     * @return The combined path
     */
    public static String combinePath(String path, String... more) {
        return Path.of(path, more).toString();
    }

    public static String readFile(String filePath) {
        String content = "";
        try {
            content = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content;
    }

    public static int sumIntegers(int... numbers) {

        var result = 0;
        for (int number : numbers)
            result += number;
        return result;
    }

    public static void writeFileToDatabase(String fileName, String content) throws URISyntaxException {

        File databaseDir = new File(Settings.DATA_PATH);
        databaseDir.mkdirs();
        File saveIntoDatabase = new File(Settings.DATA_PATH + fileName);

        BufferedWriter writer;
        try {

            writer = new BufferedWriter(new FileWriter(saveIntoDatabase));
            writer.write(content);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
