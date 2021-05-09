package org.visab.repository;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.visab.processing.IVISABFile;
import org.visab.processing.VISABFileBase;
import org.visab.util.AssignByGame;
import org.visab.util.JsonConvert;
import org.visab.util.Settings;

/**
 * Class for saving and loading VISAB files from VISABs database.
 *
 * @author moritz
 *
 */
public class VISABRepository {

    private static final String baseDir = Settings.DATA_PATH;

    /**
     * Reads the contents of a file at a given path
     *
     * @param filePath The path to the file
     * @return The contents of the file or empty string if unsuccessful
     */
    public String readFile(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            e.printStackTrace();

            return "";
        }
    }

    /**
     * Writes string content to a file at a given path
     *
     * @param filePath The path to the file
     * @param content  The content of the file
     * @return True if file was successfully written, false else
     */
    public boolean writeFile(String filePath, String content) {
        var file = new File(filePath);

        try (var writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();

            return false;
        }

        return true;
    }

    public File loadFile(String game, String fileName) {
        var filePath = baseDir + game + "/" + fileName;

        return loadFile(filePath);
    }

    public File loadFile(String filePath) {
        return new File(filePath);
    }

    /**
     * Deletes a file of a give game with a given name
     *
     * @param game     The game of the file
     * @param fileName The name of the file
     * @return True if deleted, false else
     */
    public boolean deleteFileByName(String game, String fileName) {
        var filePath = baseDir + game + "/" + fileName;

        return deleteFileByPath(filePath);
    }

    /**
     * Deletes a file at a given path
     *
     * @param filePath The path of the file to delete
     * @return True if deleted, false else
     */
    public boolean deleteFileByPath(String filePath) {
        return new File(filePath).delete();
    }

    /**
     * Loads a file from the database
     *
     * @param <T>      The type of the file
     * @param game     The game of the file
     * @param fileName The name of the file
     * @return
     */
    public <T extends IVISABFile> T loadFileByName(String game, String fileName) {
        var filePath = baseDir + game + "/" + fileName;

        return loadFileByPath(game, filePath);
    }

    /**
     * Loads a file from a given path
     *
     * @param <T>      The type of the file
     * @param game     The game of the file
     * @param filePath The path to the file
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends IVISABFile> T loadFileByPath(String game, String filePath) {
        var json = readFile(filePath);

        var file = AssignByGame.getDeserializedFile(json, game);

        return file != null ? (T) file : null;
    }

    public VISABFileBase loadBaseFile(String filePath) {
        var json = readFile(filePath);

        return JsonConvert.deserializeJson(json, VISABFileBase.class);
    }

    /**
     * Saves a file to the database
     *
     * @param visabFile The file to save
     * @param fileName  The name of the file
     * @return True if successfully saved, false else
     */
    public boolean saveFile(IVISABFile visabFile, String fileName) {
        var json = JsonConvert.serializeObject(visabFile);

        var fileDir = baseDir + visabFile.getGame();
        new File(fileDir).mkdirs();

        var filePath = fileDir + "/" + fileName;
        // If file has no extension, make it .visab2
        if (!filePath.contains("."))
            filePath += ".visab2";

        return writeFile(filePath, json);
    }

}
