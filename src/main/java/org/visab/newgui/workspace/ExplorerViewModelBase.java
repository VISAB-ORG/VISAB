package org.visab.newgui.workspace;

import java.io.File;
import java.time.Instant;
import java.time.ZoneId;

import org.visab.newgui.ViewModelBase;
import org.visab.newgui.workspace.model.FileRow;
import org.visab.repository.BasicRepository;

import de.saxsys.mvvmfx.utils.commands.Command;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TreeItem;

public abstract class ExplorerViewModelBase extends ViewModelBase {

    protected String baseDirPath;

    protected FileRow baseFile;

    private BasicRepository basicRepo = new BasicRepository("");

    /**
     * This violates MVVM, but its the only way we can consistently have the
     * currently seletecd item in the ViewModel
     */
    private ObjectProperty<TreeItem<FileRow>> selectedFileRow = new SimpleObjectProperty<>();

    public ExplorerViewModelBase(String baseDirPath) {
        this.baseDirPath = baseDirPath;

        basicRepo.createMissingDirectories(baseDirPath);
    }

    /**
     * Handler for adding new files via drag and drop or dialog
     *
     * @param file The file to be added
     * @return True if file was added, false else
     */
    public abstract boolean addFile(File file);

    /**
     * A command to delete the currenlty selected file row
     *
     * @return The command to delete the currently selected file row
     */
    public Command deleteFileCommand() {
        return runnableCommand(() -> {
            var selectedRow = getSelectedFileRow();

            if (selectedRow != null && !selectedRow.getName().equals(baseFile.getName()))
                if (basicRepo.deleteFile(selectedRow.getAbsolutePath()))
                    removeFileRow(baseFile, selectedRow);
        });
    }

    public String getBaseDirPath() {
        return this.baseDirPath;
    }

    /**
     * Initializes the base file row item and subsequently returns it
     *
     * @return The base file row item
     */
    public FileRow getFreshBaseFileRow() {
        // Recursively set children
        var file = basicRepo.loadFile(baseDirPath);
        var baseFile = getFileRow(file, null);

        this.baseFile = baseFile;

        setChildFiles(baseFile);

        return baseFile;
    }

    /**
     * Initializes a FileRow object from a given file
     *
     * @param file The file to create a FileRow of
     * @return The FileRow
     */
    protected FileRow getFileRow(File file, FileRow parentDir) {
        var name = file.getName();
        var lastModified = Instant.ofEpochMilli(file.lastModified()).atZone(ZoneId.systemDefault()).toLocalDateTime();
        var size = FileSizeHelper.size(file.toPath()) / 1000L; // In kb
        var fullPath = file.getAbsolutePath();
        var isDirectory = file.isDirectory();

        return new FileRow(name, lastModified, size, fullPath, isDirectory, parentDir);
    }

    /**
     * Gets the currently selected file row
     *
     * @return The currently selected file row
     */
    protected FileRow getSelectedFileRow() {
        if (selectedFileRow.get() != null)
            return selectedFileRow.get().getValue();

        return null;
    }

    /**
     * Recursively removes a given FileRow from the file tree.
     *
     * @param parentRow The parent Row
     * @param removeRow The row to remove
     */
    protected void removeFileRow(FileRow parentRow, FileRow removeRow) {
        if (parentRow == null)
            return;

        for (var child : parentRow.getFiles()) {
            if (child == removeRow) {
                parentRow.getFiles().remove(child);
                // Finally update the parent directories information
                updateDirectoryInformation(child);
                return;
            }

            if (child.isDirectory())
                removeFileRow(child, removeRow);
        }
    }

    /**
     * This violates MVVM, but its the only way we can consistently have the
     * currently seletecd item in the ViewModel
     *
     * @return
     */
    public ObjectProperty<TreeItem<FileRow>> selectedFileRowProperty() {
        return selectedFileRow;
    }

    /**
     * Recursively sets files to a given FileRow
     *
     * @param fileRow The FileRow to add files to
     */
    protected void setChildFiles(FileRow fileRow) {
        if (fileRow.isDirectory()) {
            var files = basicRepo.loadFile(fileRow.getAbsolutePath()).listFiles();
            for (var file : files) {
                var row = getFileRow(file, fileRow);
                fileRow.getFiles().add(row);

                if (row.isDirectory())
                    setChildFiles(row);
            }
        }
    }

    /**
     * Recursively updates the file information (last modified + size) of the given
     * fileRow and all its parent directories
     * 
     * @param fileRow The fileRow to start from
     */
    protected void updateDirectoryInformation(FileRow fileRow) {
        if (fileRow.isDirectory()) {
            var file = basicRepo.loadFile(fileRow.getAbsolutePath());

            var lastModified = Instant.ofEpochMilli(file.lastModified()).atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            var size = FileSizeHelper.size(file.toPath()) / 1000L; // In kb

            fileRow.setLastModifies(lastModified);
            fileRow.setSize(size);
        }

        if (fileRow.getParentDir() != null)
            updateDirectoryInformation(fileRow.getParentDir());
    }

    public boolean renameSelectedFile(String newName) {
        var selectedRow = getSelectedFileRow();
        if (selectedRow != null && selectedRow != baseFile) {
            var newFilePath = basicRepo.combinePath(selectedRow.getParentDir().getAbsolutePath(), newName);

            if (basicRepo.renameFile(selectedRow.getAbsolutePath(), newFilePath)) {
                // Remove the current fileRow
                removeFileRow(selectedRow.getParentDir(), selectedRow);

                var newFile = basicRepo.loadFile(newFilePath);
                var newRow = getFileRow(newFile, selectedRow.getParentDir());

                // Add the new fileRow
                newRow.getParentDir().getFiles().add(newRow);

                // Set the child files if the
                if (newRow.isDirectory())
                    setChildFiles(newRow);

                return true;
            }

        }

        return false;
    }

}
