package org.visab.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.main.Main;
import org.visab.workspace.DatabaseManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

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

    public static Image recolorImage(Image inputImage, Color newColor) {
        final double r = newColor.getRed();
        final double g = newColor.getGreen();
        final double b = newColor.getBlue();
        final int w = (int) inputImage.getWidth();
        final int h = (int) inputImage.getHeight();
        final WritableImage outputImage = new WritableImage(w, h);
        final PixelWriter writer = outputImage.getPixelWriter();
        final PixelReader reader = inputImage.getPixelReader();
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                // Keeping the opacity of every pixel as it is.
                writer.setColor(x, y, new Color(r, g, b, reader.getColor(x, y).getOpacity()));
            }
        }
        return outputImage;
    }

    public static Color translateHexToRgbColor(String hexCode) {
        return Color.valueOf(hexCode);
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

        File databaseDir = new File(DatabaseManager.DATABASE_PATH);
        databaseDir.mkdirs();
        File saveIntoDatabase = new File(DatabaseManager.DATABASE_PATH + fileName);

        BufferedWriter writer;
        try {

            writer = new BufferedWriter(new FileWriter(saveIntoDatabase));
            writer.write(content);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method properly checks from within code, if the application is ran as a
     * jar or not.
     * 
     * @return
     */
    public static boolean isRunningFromJar() {
        String className = Main.class.getName().replace('.', '/');
        String classJar = Main.class.getResource("/" + className + ".class").toString();
        if (classJar.startsWith("jar:")) {
            return true;
        }
        return false;
    }

    public static String readResourceContents(String path) {
        if (!path.startsWith("/"))
            path = "/" + path;

        byte[] data = null;
        try (var stream = Main.class.getResourceAsStream(path)) {
            data = stream.readAllBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new String(data, StandardCharsets.UTF_8);
    }

    /**
     * Recursively gets all implemented interfaces for a class.
     * 
     * @param class_ The class to get the interfaces for.
     * @return All implemented interfaces
     */
    public static List<Class<?>> getAllInterfaces(Class<?> class_) {
        return getAllInterfaces(class_, new ArrayList<Class<?>>());
    }

    /**
     * Recursively gets all implemented interfaces for a class.
     * 
     * @param class_ The class to get the interfaces for.
     * @return All implemented interfaces
     */
    private static List<Class<?>> getAllInterfaces(Class<?> class_, List<Class<?>> list) {
        var interfaces = class_.getInterfaces();
        for (int i = 0; i < interfaces.length; i++) {
            list.add(interfaces[i]);
        }

        var superClass = class_.getSuperclass();
        if (superClass != null)
            getAllInterfaces(superClass, list);

        return list;
    }

    /**
     * !! COPIED FROM OLD VISAB GUI TO HAVE EVERYTHING TOGETHER !!
     * 
     * @TODO: Delete this
     * 
     *        This method is responsible for retreiving the files located in the
     *        location-specific database.
     * 
     * @return an observable list of file names that are displayed in the GUI.
     */
    public static ObservableList<String> loadFilesFromDatabase() {
        File database = new File(DatabaseManager.DATABASE_PATH);
        File[] visabFiles = database.listFiles();
        ObservableList<String> filesComboBox = FXCollections.observableArrayList();

        // Check if there are files in the database or the database does even exist
        if (visabFiles != null) {
            for (int i = 0; i < visabFiles.length; i++) {
                if (visabFiles[i].isFile()) {
                    filesComboBox.add(visabFiles[i].getName());
                }
            }
        }
        return filesComboBox;
    }

}
