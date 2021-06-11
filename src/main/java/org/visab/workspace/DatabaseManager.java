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
import org.visab.util.StringFormat;
import org.visab.util.VISABUtil;

/**
 * The DatabaseManager that is used for deleting/adding/removing VISAB files.
 * TODO: Check if file name already exists in list of saved files.
 */
public class DatabaseManager implements IPublisher<VISABFileSavedEvent> {

    public static final String DATABASE_PATH = VISABUtil.combinePath(Workspace.WORKSPACE_PATH, "database");

    private static Logger logger = LogManager.getLogger(DatabaseManager.class);

    private static DatabaseRepository repo = new DatabaseRepository(DATABASE_PATH);

    /**
     * A list of file information for files that were recently saved via
     * SessionListeners or Database View.
     */
    private List<SavedFileInformation> savedFiles = new ArrayList<>();

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
     * Deletes a VISAB file from the database.
     * 
     * @param fileName The name of the file
     * @param game     The game of the file
     * @return True if successful
     */
    public boolean deleteFile(String fileName, String game) {
        var success = repo.deleteVISABFileDB(fileName, game);

        if (success)
            logger.info(StringFormat.niceString("Deleted {0} of {1} from database", fileName, game));
        else
            logger.error(StringFormat.niceString("Failed to delete {0} of {1} in database", fileName, game));

        return success;
    }

    public DatabaseRepository getRepository() {
        return repo;
    }

    /**
     * Loads a VISABFile from the database.
     * 
     * @param <T>      The type of the file
     * @param fileName The name of the file
     * @param game     The game of the file
     * @return The file if successfully loaded, null else
     */
    public <T extends IVISABFile> T loadFile(String fileName, String game) {
        var file = repo.<T>loadVISABFileDB(fileName, game);

        if (file != null)
            logger.info(StringFormat.niceString("Loaded {0} of {1} from database", fileName, game));
        else
            logger.error(StringFormat.niceString("Failed to load {0} of {1} in database", fileName, game));

        return file;
    }

    /**
     * Saves a VISAB file. When calling from SessionListeners, use the overload
     * containing the sessionId as 3rd parameter.
     * 
     * @param file     The file to save
     * @param fileName The name of the file
     * @return True if successful
     */
    public boolean saveFile(IVISABFile file, String fileName) {
        // If fileName has no extension, make it .visab2
        if (!fileName.contains("."))
            fileName += ".visab2";

        var success = repo.saveFileDB(file, fileName);

        if (!success) {
            logger.error(StringFormat.niceString("Failed to save {0} of {1} in database", fileName, file.getGame()));
        } else {
            logger.info(StringFormat.niceString("Saved {0} of {1} in database", fileName, file.getGame()));
            savedFiles.add(new SavedFileInformation(fileName, file.getGame()));
            var event = new VISABFileSavedEvent(file, false);
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
        // If fileName has no extension, make it .visab2
        if (!fileName.contains("."))
            fileName += ".visab2";

        var success = repo.saveFileDB(file, fileName);
        if (!success) {
            logger.error(StringFormat.niceString("Failed to save {0} of {1} in database", fileName, file.getGame()));
        } else {
            logger.info(StringFormat.niceString("Saved {0} of {1} in database", fileName, file.getGame()));
            savedFiles.add(new SavedFileInformation(fileName, file.getGame(), sessionId));
            var event = new VISABFileSavedEvent(file, true);
            publish(event);
        }

        return success;
    }

    @Override
    public void publish(VISABFileSavedEvent event) {
        GeneralEventBus.getInstance().publish(event);
    }

}
