package org.visab.workspace;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.eventbus.GeneralEventBus;
import org.visab.eventbus.IPublisher;
import org.visab.eventbus.event.VISABFileSavedEvent;
import org.visab.globalmodel.IVISABFile;
import org.visab.util.NiceString;
import org.visab.util.VISABUtil;

/**
 * The DatabaseManager that is used for deleting, adding, and removing VISAB
 * files.
 */
public class DatabaseManager implements IPublisher<VISABFileSavedEvent> {

    private static Logger logger = LogManager.getLogger(DatabaseManager.class);

    public static final String DATA_PATH_SUFFIX = "database";

    public static final String DATABASE_PATH = VISABUtil.combinePath(Workspace.WORKSPACE_PATH, DATA_PATH_SUFFIX);

    private static DatabaseRepository repo = new DatabaseRepository(DATABASE_PATH);

    private List<SavedFileInformation> savedFiles = new ArrayList<>();

    /**
     * The repository that is internally used for the I/O operations.
     */
    public DatabaseRepository getRepository() {
        return repo;
    }

    /**
     * A list of file information for files that were recently saved via
     * SessionListeners or FileExplorer view.
     */
    public List<SavedFileInformation> getSavedFiles() {
        return savedFiles;
    }

    /**
     * Loads a file that was saved by a session listener during the current runtime.
     * 
     * @param sessionId The sessionId of the session for which the file was saved
     * @return The saved file if there was one, null else
     */
    public IVISABFile loadSessionFile(UUID sessionId) {
        for (var saveInfo : savedFiles) {
            if (saveInfo.isSavedByListener() && saveInfo.getSessionId().equals(sessionId)) {
                var file = loadFile(saveInfo.getFileName(), saveInfo.getGame());

                return file;
            }
        }
        
        return null;
    }

    /**
     * Gets the name of a file that was saved under the given sessionId.
     * 
     * @param sessionId The sessionId to get the file for
     * @return The saved files name if one was saved, "" else
     */
    public String getSessionFileName(UUID sessionId) {
        for (var saveInfo : savedFiles) {
            if (saveInfo.isSavedByListener() && saveInfo.getSessionId().equals(sessionId))
                return saveInfo.getFileName();
        }

        return "";
    }

    /**
     * Deletes a VISAB file from the database.
     * 
     * @param fileName The name of the file
     * @param game     The game of the file
     * @return True if successful
     */
    public boolean deleteFile(String fileName, String game) {
        var success = repo.deleteVISABFileDB(fileName, game);

        if (success)
            logger.info(NiceString.make("Deleted {0} of {1} from database", fileName, game));
        else
            logger.error(NiceString.make("Failed to delete {0} of {1} in database", fileName, game));

        return success;
    }

    /**
     * Loads a VISAB file from the database.
     * 
     * @param fileName The name of the file
     * @param game     The game of the file
     * @return The file if successfully loaded, null else
     */
    public IVISABFile loadFile(String fileName, String game) {
        var file = repo.loadVISABFileDB(fileName, game);

        if (file != null)
            logger.info(NiceString.make("Loaded {0} of {1} from database", fileName, game));
        else
            logger.error(NiceString.make("Failed to load {0} of {1} in database", fileName, game));

        return file;
    }

    /**
     * Loads a VISAB file from the given path.
     * 
     * @param absolutePath The path to the file
     * @return The file if successfully loaded, null else
     */
    public IVISABFile loadFile(String absolutePath) {
        var file = repo.loadBasicVISABFile(absolutePath);

        var concreteFile = repo.loadVISABFile(absolutePath, file.getGame());
        if (concreteFile != null)
            logger.info(NiceString.make("Loaded file at {0} of {1}.", absolutePath, file.getGame()));
        else
            logger.error(NiceString.make("Failed to load file at {0} of {1}.", absolutePath, file.getGame()));

        return concreteFile;
    }

    /**
     * Saves a VISAB file. When calling from SessionListeners, use the overload
     * containing the sessionId as 3rd parameter instead.
     * 
     * @param file     The file to save
     * @param fileName The name of the file
     * @return True if successful
     */
    public boolean saveFile(IVISABFile file, String fileName) {
        if (file == null) {
            logger.info("Given visab file was null. Wont save it.");
            return false;
        }

        // If fileName has no extension, make it .visab2
        if (!fileName.contains("."))
            fileName += ".visab2";

        var success = repo.saveFileDB(file, fileName);

        if (!success) {
            logger.error(NiceString.make("Failed to save {0} of {1} in database.", fileName, file.getGame()));
        } else {
            logger.info(NiceString.make("Saved {0} of {1} in database.", fileName, file.getGame()));
            savedFiles.add(new SavedFileInformation(fileName, file.getGame()));
            var event = new VISABFileSavedEvent(fileName, file.getGame());
            publish(event);
        }

        return success;
    }

    /**
     * Saves a VISAB file. This method should be called by SessionListeners, so that
     * newly saved files can be accessed by sessionId.
     * 
     * @param file      The file to save
     * @param fileName  The name of the file
     * @param sessionId The Id of the session
     * @return True if successful
     */
    public boolean saveFile(IVISABFile file, String fileName, UUID sessionId) {
        if (file == null) {
            logger.info("Given visab file was null. Wont save it.");
            return false;
        }

        // If fileName has no extension, make it .visab2
        if (!fileName.contains("."))
            fileName += ".visab2";

        var success = repo.saveFileDB(file, fileName);
        if (!success) {
            logger.error(NiceString.make("Failed to save {0} of {1} in database", fileName, file.getGame()));
        } else {
            logger.info(NiceString.make("Saved {0} of {1} in database", fileName, file.getGame()));
            savedFiles.add(new SavedFileInformation(fileName, file.getGame(), sessionId));
            var event = new VISABFileSavedEvent(fileName, file.getGame(), sessionId);
            publish(event);
        }

        return success;
    }

    @Override
    public void publish(VISABFileSavedEvent event) {
        GeneralEventBus.getInstance().publish(event);
    }

}
