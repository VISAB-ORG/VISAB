package org.visab.newgui.workspace.database;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;

import org.visab.newgui.workspace.ExplorerViewModelBase;
import org.visab.newgui.workspace.model.FileRow;
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
        if (file.isDirectory())
            return false;

        var wasSaved = false;
        try {
            // Read in the file as VISAB file
            var fileName = file.getName();

            var json = repo.readFileContents(file.getAbsolutePath());
            var asBaseFile = repo.loadBaseFile(file.getAbsolutePath());

            // Save the new file in database
            var savePath = repo.combinePath(asBaseFile.getGame(), fileName);
            wasSaved = repo.writeToFileRelative(json, savePath);

            // Add to the tree if saved succesfully
            if (wasSaved) {
                var savedFile = repo.loadFileRelative(savePath);

                // If a new dir was created, add as child of rootFile
                var game = asBaseFile.getGame();
                if (!baseFile.getFiles().stream().anyMatch(x -> x.getName().equals(game))) {
                    var newDir = repo.loadFileRelative(game);
                    var row = getFileRow(newDir, baseFile);
                    baseFile.getFiles().add(row);
                }

                // Add the file as a child of the dir in rootFile
                for (var fileRow : baseFile.getFiles()) {
                    if (fileRow.getName().equals(game)) {

                        // Make copy since we might modify fileRow.getFiles()
                        var currentFiles = new ArrayList<FileRow>(fileRow.getFiles());

                        // Remove potentially existing files with the same name
                        for (var childFile : currentFiles) {
                            if (childFile.getName().equals(fileName))
                                fileRow.getFiles().remove(childFile);
                        }

                        var newFileRow = getFileRow(savedFile, fileRow);
                        fileRow.getFiles().add(newFileRow);

                        // Finally update the file information for the parent directories
                        updateDirectoryInformation(newFileRow);

                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            wasSaved = false;
        }

        return wasSaved;
    }
}
