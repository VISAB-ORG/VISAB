package org.visab.workspace;

import org.visab.globalmodel.IVISABFile;
import org.visab.repository.DatabaseRepository;
import org.visab.util.VISABUtil;

/**
 * The DatabaseManager that is used for deleting/adding/removing VISAB files.
 */
public class DatabaseManager {

    public static final String DATABASE_PATH = VISABUtil.combinePath(Workspace.WORKSPACE_PATH, "database");

    private DatabaseRepository repo = new DatabaseRepository(DATABASE_PATH);

    public DatabaseRepository getRepository() {
        return repo;
    }

    public boolean saveFile(IVISABFile file, String fileName) {
        return repo.saveFile(file, fileName);
    }

}
