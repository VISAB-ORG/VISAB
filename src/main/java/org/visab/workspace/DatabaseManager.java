package org.visab.workspace;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.globalmodel.IVISABFile;
import org.visab.repository.DatabaseRepository;
import org.visab.util.StringFormat;
import org.visab.util.VISABUtil;

/**
 * The DatabaseManager that is used for deleting/adding/removing VISAB files.
 */
public class DatabaseManager {

    private Logger logger = LogManager.getLogger(DatabaseManager.class);

    public static final String DATABASE_PATH = VISABUtil.combinePath(Workspace.WORKSPACE_PATH, "database");

    private DatabaseRepository repo = new DatabaseRepository(DATABASE_PATH);

    public DatabaseRepository getRepository() {
        return repo;
    }

    /**
     * Saves a VISAB file
     * 
     * @param file     The file to save
     * @param fileName The name of the file
     * @return True if successful
     */
    public boolean saveFile(IVISABFile file, String fileName) {
        // If fileName has no extension, make it .visab2
        if (!fileName.contains("."))
            fileName += ".visab2";

        var success = repo.saveFile(file, fileName);

        if (success)
            logger.info(StringFormat.niceString("Saved {0} of {1} in database", fileName, file.getGame()));
        else
            logger.error(StringFormat.niceString("Failed to save {0} of {1} in database", fileName, file.getGame()));

        return success;
    }

    /**
     * Deletes a VISAB file from the database
     * 
     * @param fileName The name of the file
     * @param game     The game of the file
     * @return True if successful
     */
    public boolean deleteFile(String fileName, String game) {
        var success = repo.deleteFileByName(fileName, game);

        if (success)
            logger.info(StringFormat.niceString("Deleted {0} of {1} from database", fileName, game));
        else
            logger.error(StringFormat.niceString("Failed to delete {0} of {1} in database", fileName, game));

        return success;
    }

}
