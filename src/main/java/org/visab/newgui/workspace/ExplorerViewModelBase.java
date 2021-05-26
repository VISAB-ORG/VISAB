package org.visab.newgui.workspace;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.ZoneId;

import org.visab.newgui.ViewModelBase;
import org.visab.newgui.workspace.model.ExplorerFile;
import org.visab.workspace.BasicRepository;

import de.saxsys.mvvmfx.utils.commands.Command;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TreeItem;

public abstract class ExplorerViewModelBase extends ViewModelBase {

    protected String baseDirPath;

    protected ExplorerFile baseFile;

    private BasicRepository basicRepo;

    private Command deleteFileCommand;

    private Command renameSelectedFileCommand;

    /**
     * This violates MVVM, but its the only way we can consistently have the
     * currently seletecd file in the ViewModel.
     */
    private ObjectProperty<TreeItem<ExplorerFile>> selectedExplorerFile = new SimpleObjectProperty<>();

    public ExplorerViewModelBase(String baseDirPath) {
        this.baseDirPath = baseDirPath;
        basicRepo = new BasicRepository(baseDirPath);
    }

    /**
     * Recursively adds files to a given ExplorerFile.
     *
     * @param explorerFile The ExplorerFile to add files to
     */
    protected void addChildFiles(ExplorerFile explorerFile) {
        if (explorerFile.isDirectory()) {
            var files = basicRepo.loadFile(explorerFile.getAbsolutePath()).listFiles();
            for (var file : files) {
                var newExplorerFile = getExplorerFile(file, explorerFile);

                // Add to parents files
                explorerFile.getFiles().add(newExplorerFile);

                if (newExplorerFile.isDirectory())
                    addChildFiles(newExplorerFile);
            }
        }
    }

    /**
     * Command for adding new files via drag and drop or dialog.
     *
     * @param file The file to be added
     * @return The command
     */
    public abstract Command addFileCommand(File file);

    /**
     * Recursively changes the absolute path of all children of the given Explorer
     * file by replacing the oldName with the replacement.
     *
     * @param file    The file to whose children to change all paths for.
     * @param oldName The old name of the parent file
     * @param newName The new name of the parent file
     */
    private void changeChildrenAbsolutePath(ExplorerFile parentFile, String oldName, String newName) {
        for (var file : parentFile.getFiles()) {
            var newAbsolutePath = file.getAbsolutePath().replace(oldName, newName);
            file.setAbsolutePath(newAbsolutePath);

            // Change for all children
            if (file.isDirectory())
                changeChildrenAbsolutePath(file, oldName, newName);
        }
    }

    /**
     * A command to delete the currenlty selected ExplorerFile
     *
     * @return The command to delete the currently selected ExplorerFile
     */
    public Command deleteFileCommand() {
        if (deleteFileCommand == null) {
            deleteFileCommand = runnableCommand(() -> {
                var selectedRow = getSelectedFile();
                // Dont allow removing deleting base file
                if (selectedRow != null && !selectedRow.getName().equals(baseFile.getName()))
                    if (basicRepo.deleteFile(selectedRow.getAbsolutePath()))
                        removeExplorerFile(selectedRow, baseFile);
            });
        }

        return deleteFileCommand;
    }

    public String getBaseDirPath() {
        return this.baseDirPath;
    }

    /**
     * Initializes a ExplorerFile object from a given file.
     *
     * @param file The file to create a ExplorerFile of
     * @return The created ExplorerFile
     */
    protected ExplorerFile getExplorerFile(File file, ExplorerFile parentDir) {
        BasicFileAttributes attrs = null;
        try {
            attrs = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        var name = file.getName();
        var creationDate = Instant.ofEpochMilli(attrs.creationTime().toMillis()).atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        var size = FileSizeHelper.size(file.toPath());
        var fullPath = file.getAbsolutePath();
        var isDirectory = file.isDirectory();

        return new ExplorerFile(name, creationDate, size, fullPath, isDirectory, parentDir);
    }

    /**
     * Initializes the baseFile and returns it
     *
     * @return The initialized baseFile
     */
    public ExplorerFile getFreshBaseFile() {
        var file = basicRepo.loadFile(baseDirPath);
        var baseFile = getExplorerFile(file, null);

        this.baseFile = baseFile;

        // Recursively add all child files
        addChildFiles(baseFile);

        return baseFile;
    }

    /**
     * Gets the currently selected ExplorerFile
     *
     * @return The currently selected ExplorerFile if one is selected, null else
     */
    protected ExplorerFile getSelectedFile() {
        if (selectedExplorerFile.get() != null)
            return selectedExplorerFile.get().getValue();

        return null;
    }

    /**
     * Recursively removes a given ExplorerFile from the file tree. To remove a
     * file, pass the file object and the baseFile.
     *
     * @param fileToRemove The file to remove
     * @param parentFile   The parent file
     */
    protected void removeExplorerFile(ExplorerFile fileToRemove, ExplorerFile parentFile) {
        for (var childFile : parentFile.getFiles()) {
            if (childFile == fileToRemove) {
                parentFile.getFiles().remove(fileToRemove);
                updateFileSize(fileToRemove, -fileToRemove.getSize());

                // updateDirectoryInformation(childFile);
                return;
            } else if (childFile.isDirectory()) {
                removeExplorerFile(fileToRemove, childFile);
            }
        }
    }

    /**
     * Gets a command for renaming the currenlty selected file to the given new
     * name.
     *
     * @param newName The new name for file.
     * @return The command
     */
    public Command renameSelectedFileCommand(String newName) {
        if (renameSelectedFileCommand == null) {
            renameSelectedFileCommand = runnableCommand(() -> {
                var selectedRow = getSelectedFile();
                if (selectedRow != null && selectedRow != baseFile) {
                    var newFilePath = basicRepo.combinePath(selectedRow.getParentDir().getAbsolutePath(), newName);
                    if (basicRepo.renameFile(selectedRow.getAbsolutePath(), newFilePath)) {
                        var oldName = selectedRow.getName();

                        selectedRow.setName(newName);
                        selectedRow.setAbsolutePath(newFilePath);

                        if (selectedRow.isDirectory())
                            changeChildrenAbsolutePath(selectedRow, oldName, newName);
                    }
                }
            });
        }

        return renameSelectedFileCommand;
    }

    /**
     * This violates MVVM, but its the only way we can consistently have the
     * currently seletecd item in the ViewModel
     *
     * @return
     */
    public ObjectProperty<TreeItem<ExplorerFile>> selectedExplorerFileProperty() {
        return selectedExplorerFile;
    }

    /**
     * Recursively updates the size of a given ExplorerFile by adding the given long
     * to the file and its parent files.
     *
     * @param file The file to updates the size for
     * @param add  The size to add to the current size
     */
    private void updateFileSize(ExplorerFile file, long add) {
        file.setSize(file.getSize() + add);

        var parentFile = file.getParentDir();
        if (parentFile != null)
            updateFileSize(parentFile, add);
    }

}
