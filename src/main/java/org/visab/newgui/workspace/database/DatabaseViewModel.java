package org.visab.newgui.workspace.database;

import java.io.File;
import java.util.ArrayList;

import org.visab.globalmodel.BasicVISABFile;
import org.visab.newgui.workspace.ExplorerViewModelBase;
import org.visab.newgui.workspace.model.ExplorerFile;
import org.visab.util.StreamUtil;
import org.visab.workspace.DatabaseManager;
import org.visab.workspace.DatabaseRepository;
import org.visab.workspace.Workspace;

import de.saxsys.mvvmfx.utils.commands.Command;

public class DatabaseViewModel extends ExplorerViewModelBase {

    // TODO: This could also just use the Manager instead. Have to see if we want
    // that.
    private DatabaseRepository repo = Workspace.getInstance().getDatabaseManager().getRepository();

    public DatabaseViewModel() {
        super(DatabaseManager.DATABASE_PATH);
    }

    @Override
    public boolean addFile(File file) {
        if (file.isDirectory()) {
            for (var _file : file.listFiles())
                if (!addFile(_file))
                    return false;
        } else {
            try {
                var fileName = file.getName();

                // TODO:
                if (!file.getName().endsWith(".visab2"))
                    return false;

                var json = repo.readFileContents(file.getAbsolutePath());
                var basicFile = repo.loadBasicVISABFile(file.getAbsolutePath());

                var savePath = repo.combinePath(basicFile.getGame(), fileName);
                // Save the new file in database
                if (repo.writeToFileRelative(savePath, json)) {
                    var visabFile = repo.loadFileRelative(savePath);

                    // If dir for game does not exist yet, add it
                    var game = basicFile.getGame();
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
                    var newExplorerFile = getExplorerFile(visabFile, gameFile);
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

    @Override
    public Command addFileCommand() {
        // TODO Auto-generated method stub
        return null;
    }
}
