package org.visab.repository;

import org.visab.util.AssignByGame;
import org.visab.util.JsonConvert;

/**
 * Class for saving and loading VISAB files from VISABs database.
 *
 * @author moritz
 *
 */
public class DatabaseRepository extends RepositoryBase {

    public DatabaseRepository(String databasePath) {
        super(databasePath);
    }

    /**
     * Deletes a file of a give game with a given name
     *
     * @param game     The game of the file
     * @param fileName The name of the file
     * @return True if deleted, false else
     */
    public boolean deleteFileByName(String fileName, String game) {
        var filePath = combinePath(baseDirectory, game, fileName);

        return deleteFile(filePath);
    }

    /**
     * Loads a VISABFile file from the repository
     *
     * @param <T>      The type of the file
     * @param game     The game of the file
     * @param fileName The name of the file
     * @return
     */
    public <T extends IVISABFile> T loadVISABFile(String fileName, String game) {
        var filePath = combinePath(baseDirectory, game, fileName);

        return this.<T>loadVISABFileByPath(game, filePath);
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
    public <T extends IVISABFile> T loadVISABFileByPath(String filePath, String game) {
        var json = readFileContents(filePath);

        var file = AssignByGame.getDeserializedFile(json, game);

        return file != null ? (T) file : null;
    }

    /**
     * Loads a VISABFileBase object from a given path
     * 
     * @param filePath The path to the file
     * @return The VISABFileBase
     */
    public VISABFileBase loadBaseFile(String filePath) {
        var json = readFileContents(filePath);

        return JsonConvert.deserializeJson(json, VISABFileBase.class);
    }

    /**
     * Saves a file to the repository
     *
     * @param visabFile The file to save
     * @param fileName  The name of the file
     * @return True if successfully saved, false else
     */
    public boolean saveFile(IVISABFile visabFile, String fileName) {
        var json = JsonConvert.serializeObject(visabFile);

        var fileDir = combinePath(baseDirectory, visabFile.getGame());
        createMissingDirectories(fileDir);

        var filePath = combinePath(fileDir, fileName);
        // If file has no extension, make it .visab2
        if (!filePath.contains("."))
            filePath += ".visab2";

        return writeToFile(filePath, json);
    }
}
