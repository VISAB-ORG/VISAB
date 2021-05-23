package org.visab.newgui.workspace.database;

import java.io.File;
import java.util.ArrayList;

import org.visab.globalmodel.BasicVISABFile;
import org.visab.newgui.workspace.ExplorerViewModelBase;
import org.visab.newgui.workspace.model.FileRow;
import org.visab.repository.DatabaseRepository;
import org.visab.workspace.DatabaseManager;
import org.visab.workspace.Workspace;

public class DatabaseViewModel extends ExplorerViewModelBase {

    // TODO: This could also just use the Manager instead. Have to see if we want
    // that.
    private DatabaseRepository repo = Workspace.instance.getDatabaseManager().getRepository();

    public DatabaseViewModel() {
        super(DatabaseManager.DATABASE_PATH);
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
            var basicFile = repo.loadBasicVISABFile(file.getAbsolutePath());

            // Save the new file in database
            var savePath = repo.combinePath(basicFile.getGame(), fileName);
            wasSaved = repo.writeToFileRelative(json, savePath);

            // Add to the tree if saved succesfully
            if (wasSaved) {
                var savedFile = repo.loadFileRelative(savePath);

                // If a new dir was created, add as child of rootFile
                var game = basicFile.getGame();
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
