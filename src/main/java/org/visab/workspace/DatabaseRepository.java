package org.visab.workspace;

import org.visab.dynamic.DynamicSerializer;
import org.visab.globalmodel.BasicVISABFile;
import org.visab.globalmodel.IVISABFile;
import org.visab.util.JsonConvert;

/**
 * A repository for saving and loading VISAB files from VISABs database.
 *
 */
public class DatabaseRepository extends RepositoryBase {

    public DatabaseRepository(String databasePath) {
        super(databasePath);
    }

    /**
     * Deletes a file from the database.
     *
     * @param game     The game of the file
     * @param fileName The name of the file
     * @return True if deleted
     */
    public boolean deleteVISABFileDB(String fileName, String game) {
        var filePath = combinePath(baseDirectory, game, fileName);

        return deleteFile(filePath);
    }

    /**
     * Loads a BasicVISABFile from a given path.
     * 
     * @param filePath The path to the file
     * @return The BasicVISABFile
     */
    public BasicVISABFile loadBasicVISABFile(String filePath) {
        var json = readFileContents(filePath);

        return JsonConvert.deserializeJson(json, BasicVISABFile.class);
    }

    /**
     * Loads a VISABFile from a given path.
     *
     * @param game     The game of the file
     * @param filePath The path to the file
     * @return The VISABFile
     */
    public IVISABFile loadVISABFile(String filePath, String game) {
        var json = readFileContents(filePath);

        var file = DynamicSerializer.deserializeVISABFile(json, game);

        return file;
    }

    /**
     * Loads a VISABFile file from the database.
     *
     * @param game     The game of the file
     * @param fileName The name of the file
     * @return The VISABFile
     */
    public IVISABFile loadVISABFileDB(String fileName, String game) {
        var filePath = combinePath(baseDirectory, game, fileName);

        return loadVISABFile(filePath, game);
    }

    /**
     * Saves a file to the database.
     *
     * @param visabFile The file to save
     * @param fileName  The name of the file
     * @return True if successfully saved
     */
    public boolean saveFileDB(IVISABFile visabFile, String fileName) {
        var json = JsonConvert.serializeObject(visabFile);

        var fileDir = combinePath(baseDirectory, visabFile.getGame());
        createMissingDirectories(fileDir);

        var filePath = combinePath(fileDir, fileName);

        return writeToFile(filePath, json);
    }
}
