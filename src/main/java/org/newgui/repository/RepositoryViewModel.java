package org.newgui.repository;

import java.io.File;
import java.time.Instant;
import java.time.ZoneId;

import org.newgui.repository.model.FileRow;

import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.visab.repository.VISABRepository;
import org.visab.util.AssignByGame;
import org.visab.util.Settings;

public class RepositoryViewModel implements ViewModel {

    private VISABRepository repo = new VISABRepository();

    public ObjectProperty<FileRow> rootFileRowProperty = new SimpleObjectProperty<>();

    private IntegerProperty fileChanges = new SimpleIntegerProperty(0);

    public IntegerProperty fileChangesProperty() {
        return fileChanges;
    }

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
                if (!rootFileRowProperty.get().getFiles().stream().anyMatch(x -> x.getName().equals(dirName))) {
                    var newDir = addedFile.getParentFile();
                    rootFileRowProperty.get().getFiles().add(initializeFileRow(newDir));
                }

                // Add the file as a child of the dir in rootFile
                for (var fileRow : rootFileRowProperty().get().getFiles()) {
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

    public RepositoryViewModel() {
        initializeRootFileRow();
    }

    public void filesRefreshed() {
        if (fileChanges != null)
            fileChanges.subtract(fileChanges.get());
    }

    public ObjectProperty<FileRow> rootFileRowProperty() {
        return rootFileRowProperty;
    }

    private void initializeRootFileRow() {
        // TODO: Load from settings
        var repoDir = new File(Settings.DATA_PATH);
        var fileRow = initializeFileRow(repoDir);

        // Recursively set children
        setChildFiles(fileRow);

        this.rootFileRowProperty.set(fileRow);
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

    private FileRow initializeFileRow(File file) {
        var name = file.getName();
        var lastModified = Instant.ofEpochMilli(file.lastModified()).atZone(ZoneId.systemDefault()).toLocalDateTime();
        var size = FileSizeHelper.size(file.toPath()) / 1000L; // In kb
        var fullPath = file.getAbsolutePath();
        var isDirectory = file.isDirectory();

        return new FileRow(name, lastModified, size, fullPath, isDirectory);
    }

}
