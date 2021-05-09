package org.visab.newgui.workspace.database;

import java.io.File;
import java.nio.file.Path;

import org.visab.newgui.workspace.ExplorerViewModelBase;
import org.visab.repository.DatabaseRepository;
import org.visab.util.VISABUtil;

public class DatabaseViewModel extends ExplorerViewModelBase {

    // TODO: Have this somewhere else but still static
    private static final String DATABASE_DIR = Path.of(VISABUtil.getRunningJarRootDirPath(), "database").toString();

    private DatabaseRepository repo = new DatabaseRepository();

    public DatabaseViewModel() {
        super(DATABASE_DIR);
    }

    @Override
    public boolean addFile(File file) {
        var added = false;
        try {
            var fileName = file.getName();

            var json = repo.readFileContents(file.getAbsolutePath());
            var fileBase = repo.loadBaseFile(file.getAbsolutePath());

            var saved = repo.writeToFileRelative(json, repo.combinePath(fileBase.getGame(), fileName));

            // Add to the tree if saved succesfully
            if (saved) {
                // If a new dir was created, add as child of rootFile
                var dirName = fileBase.getGame();
                if (!baseFile.getFiles().stream().anyMatch(x -> x.getName().equals(dirName))) {
                    var newDir = repo.loadFileRelative(dirName);
                    var row = getFileRow(newDir);
                    baseFile.getFiles().add(row);
                }

                // Add the file as a child of the dir in rootFile
                for (var fileRow : baseFile.getFiles()) {
                    if (fileRow.getName().equals(dirName)) {
                        // Simply reload all files, so that replacements are supported
                        // Could do this smarter, but its not neccessary
                        fileRow.getFiles().clear();
                        setChildFiles(fileRow);
                    }
                }
            }
            added = saved;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return added;
    }

}
