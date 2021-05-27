package org.visab.newgui.workspace.database;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.visab.dynamic.DynamicSerializer;
import org.visab.newgui.workspace.ExplorerViewModelBase;
import org.visab.newgui.workspace.model.ExplorerFile;
import org.visab.util.StreamUtil;
import org.visab.workspace.DatabaseManager;
import org.visab.workspace.DatabaseRepository;
import org.visab.workspace.Workspace;

import de.saxsys.mvvmfx.utils.commands.Command;

public class DatabaseViewModel extends ExplorerViewModelBase {

    private DatabaseManager manager = Workspace.getInstance().getDatabaseManager();

    private DatabaseRepository repo = manager.getRepository();

    public DatabaseViewModel() {
        super(DatabaseManager.DATABASE_PATH);
    }

    @Override
    public boolean addFile(File fileToAdd) {
        if (fileToAdd.isDirectory()) {
            for (var _file : fileToAdd.listFiles())
                if (!addFile(_file))
                    return false;
        } else {
            try {
                var fileName = fileToAdd.getName();
                // TODO:
                if (!fileToAdd.getName().endsWith(".visab2"))
                    return false;

                var json = repo.readFileContents(fileToAdd.getAbsolutePath());
                // Load the basic file to find out the game.
                var basicVISABFile = repo.loadBasicVISABFile(fileToAdd.getAbsolutePath());
                var game = basicVISABFile.getGame();
                var concreteVISABFile = DynamicSerializer.deserializeVISABFile(json, game);

                // Save the new file in database
                if (concreteVISABFile != null && manager.saveFile(concreteVISABFile, fileName)) {
                    var relSavePath = repo.combinePath(game, fileName);
                    // Load the java.nio.File object
                    var file = repo.loadFileRelative(relSavePath);

                    // If dir for game does not exist yet, add it
                    if (!StreamUtil.contains(baseFile.getFiles(), x -> x.getName().equals(game))) {
                        var newDir = repo.loadFileRelative(game);
                        var row = getExplorerFile(newDir, baseFile);
                        baseFile.getFiles().add(row);
                    }

                    var gameFile = StreamUtil.firstOrNull(baseFile.getFiles(), x -> x.getName().equals(game));
                    // Remove existing files with the same name
                    var existingFile = StreamUtil.contains(gameFile.getFiles(), x -> x.getName().equals(fileName));
                    if (existingFile) {
                        var existingFiles = new ArrayList<ExplorerFile>(gameFile.getFiles());
                        for (var exisiting : existingFiles) {
                            if (exisiting.getName().equals(fileName))
                                removeExplorerFile(exisiting, gameFile);
                        }
                    }

                    // Finally create the new ExplorerFile
                    var newExplorerFile = getExplorerFile(file, gameFile);
                    gameFile.getFiles().add(newExplorerFile);
                    updateFileSize(gameFile, newExplorerFile.getSize());
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        return true;
    }

    private Command addFileCommand;

    @Override
    public Command addFileCommand() {
        if (addFileCommand == null) {
            addFileCommand = runnableCommand(() -> {
                var allowedExtensions = new HashMap<String, String>();
                allowedExtensions.put("VISAB files", "*.visab2");

                // This blocks until the dialog is canceled or accepted
                var files = dialogHelper.showFileDialog(baseDirPath, allowedExtensions, "Add VISAB files");
                for (var file : files)
                    addFile(file);
            });
        }

        return addFileCommand;
    }
}
