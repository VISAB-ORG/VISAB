package org.newgui.repository;

import java.io.File;
import java.time.Instant;
import java.time.ZoneId;

import org.newgui.ViewModelBase;
import org.newgui.repository.model.FileRow;
import org.visab.repository.VISABRepository;
import org.visab.util.AssignByGame;
import org.visab.util.Settings;

import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.utils.commands.Command;
import de.saxsys.mvvmfx.utils.commands.DelegateCommand;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TreeItem;

public class RepositoryViewModel extends ViewModelBase {

    private VISABRepository repo = new VISABRepository();
    private FileRow rootFile;

    public void addFile(File file) {
        try {
            var fileName = file.getName();

            var json = repo.readFile(file.getAbsolutePath());
            var baseFile = repo.loadBaseFile(file.getAbsolutePath());

            var concreteFile = AssignByGame.getDeserializedFile(json, baseFile.getGame());
            if (repo.saveFile(concreteFile, fileName)) {
                // Add to the tree if saved succesfully
                var addedFile = repo.loadFile(concreteFile.getGame(), fileName);

                // If a new dir was created, add as child of rootFile
                var dirName = concreteFile.getGame();
                if (!rootFile.getFiles().stream().anyMatch(x -> x.getName().equals(dirName))) {
                    var newDir = addedFile.getParentFile();
                    rootFile.getFiles().add(initializeFileRow(newDir));
                }

                // Add the file as a child of the dir in rootFile
                for (var fileRow : rootFile.getFiles()) {
                    if (fileRow.getName().equals(dirName)) {
                        // Simply reload all files, so that replacements are supported
                        fileRow.getFiles().clear();
                        setChildFiles(fileRow);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public FileRow getRootFileRow() {
        // TODO: Load from settings
        var repoDir = new File(Settings.DATA_PATH);
        var rootFile = initializeFileRow(repoDir);

        // Recursively set children
        setChildFiles(rootFile);

        this.rootFile = rootFile;

        return this.rootFile;
    }

    private FileRow initializeFileRow(File file) {
        var name = file.getName();
        var lastModified = Instant.ofEpochMilli(file.lastModified()).atZone(ZoneId.systemDefault()).toLocalDateTime();
        var size = FileSizeHelper.size(file.toPath()) / 1000L; // In kb
        var fullPath = file.getAbsolutePath();
        var isDirectory = file.isDirectory();

        return new FileRow(name, lastModified, size, fullPath, isDirectory);
    }

    /**
     * Recursively sets files to a given FileRow
     *
     * @param fileRow The FileRow to add files to
     */
    private void setChildFiles(FileRow fileRow) {
        if (fileRow.isDirectory()) {
            var files = new File(fileRow.getFullPath()).listFiles();
            for (var file : files) {
                var row = initializeFileRow(file);
                fileRow.getFiles().add(row);

                if (row.isDirectory())
                    setChildFiles(row);
            }
        }
    }

    private ObjectProperty<TreeItem<FileRow>> selectedFileRow = new SimpleObjectProperty<>();

    // TODO: This violates MVVM, but makes it so that we dont have can define
    // commands in the viewmodel
    public ObjectProperty<TreeItem<FileRow>> selectedFileRowProperty() {
        return selectedFileRow;
    }

    public Command deleteFileCommand() {
        return runnableCommand(() -> removeFileRow(selectedFileRowProperty().get().getValue()));
    }

    private void removeFileRow(FileRow fileRow) {
        for (var child : fileRow.getFiles()) {
            if (child == fileRow) {
                fileRow.getFiles().remove(child);
                return;
            }

            if (child.isDirectory())
                removeFileRow(child);
        }
    }

}