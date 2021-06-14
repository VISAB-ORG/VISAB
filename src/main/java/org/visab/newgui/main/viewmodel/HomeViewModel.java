package org.visab.newgui.main.viewmodel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;

import org.visab.dynamic.DynamicSerializer;
import org.visab.newgui.DynamicViewLoader;
import org.visab.newgui.ViewModelBase;
import org.visab.newgui.control.ExplorerFile;
import org.visab.newgui.main.MainScope;
import org.visab.newgui.sessionoverview.view.SessionOverviewView;
import org.visab.newgui.settings.SettingsView;
import org.visab.util.FileSizeHelper;
import org.visab.util.StreamUtil;
import org.visab.util.VISABUtil;
import org.visab.workspace.DatabaseManager;
import org.visab.workspace.DatabaseRepository;
import org.visab.workspace.Workspace;

import de.saxsys.mvvmfx.InjectScope;
import de.saxsys.mvvmfx.ScopeProvider;
import de.saxsys.mvvmfx.utils.commands.Command;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.stage.Stage;

@ScopeProvider(MainScope.class)
public class HomeViewModel extends ViewModelBase {

    @InjectScope
    MainScope scope;

    // Deprecated VISAB 1.0 GUI code @TODO: delete this later on
    // ----- Command class variables -----
    private Command openMain;
    private Command openStatisticsViewer;
    private Command openPathViewer;
    private Command openHelp;
    private Command openAbout;

    // ----- Command methods -----
    public Command openMain() {
        if (openMain == null) {
            openMain = runnableCommand(() -> {
                FXMLLoader fxmlLoader = new FXMLLoader(HomeViewModel.class.getResource("/MainWindow.fxml"));
                Scene scene;
                try {
                    scene = new Scene(fxmlLoader.load(), 1200, 1000);
                    Stage stage = new Stage();
                    stage.setTitle("Old Main");
                    stage.setScene(scene);
                    stage.show();

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            });
        }
        return openMain;
    }

    public Command openStatisticsViewer() {
        if (openStatisticsViewer == null) {
            openStatisticsViewer = runnableCommand(() -> {
                FXMLLoader fxmlLoader = new FXMLLoader(HomeViewModel.class.getResource("/StatisticsWindow.fxml"));
                Scene scene;
                try {
                    scene = new Scene(fxmlLoader.load(), 1200, 1000);
                    Stage stage = new Stage();
                    stage.setTitle("Old StatisticsViewer");
                    stage.setScene(scene);
                    stage.show();

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            });
        }

        return openStatisticsViewer;
    }

    public Command openPathViewer() {
        if (openPathViewer == null) {
            openPathViewer = runnableCommand(() -> {
                FXMLLoader fxmlLoader = new FXMLLoader(HomeViewModel.class.getResource("/PathViewerWindow.fxml"));
                Scene scene;
                try {
                    scene = new Scene(fxmlLoader.load(), 1200, 1000);
                    Stage stage = new Stage();
                    stage.setTitle("Old PathViewer");
                    stage.setScene(scene);
                    stage.show();

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            });
        }

        return openPathViewer;
    }

    public Command openAbout() {
        if (openAbout == null) {
            openAbout = runnableCommand(() -> {
                FXMLLoader fxmlLoader = new FXMLLoader(HomeViewModel.class.getResource("/AboutWindow.fxml"));
                Scene scene;
                try {
                    scene = new Scene(fxmlLoader.load(), 1200, 1000);
                    Stage stage = new Stage();
                    stage.setTitle("Old About");
                    stage.setScene(scene);
                    stage.show();

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            });
        }

        return openAbout;
    }

    public Command openHelp() {
        if (openHelp == null) {
            openHelp = runnableCommand(() -> {
                FXMLLoader fxmlLoader = new FXMLLoader(HomeViewModel.class.getResource("/HelpWindow.fxml"));
                Scene scene;
                try {
                    scene = new Scene(fxmlLoader.load(), 1200, 1000);
                    Stage stage = new Stage();
                    stage.setTitle("New Window");
                    stage.setScene(scene);
                    stage.show();

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            });
        }

        return openHelp;
    }
    // ----- End of deprecated VISAB 1.0 GUI code -----

    private Command openApiDashboard;
    private Command openSettings;

    public Command openApi() {
        if (openApiDashboard == null) {
            openApiDashboard = runnableCommand(() -> {
                DynamicViewLoader.showView(SessionOverviewView.class, "API Dashboard");
            });
        }

        return openApiDashboard;
    }

    public Command openSettings() {
        if (openSettings == null) {
            openSettings = runnableCommand(() -> {
                DynamicViewLoader.showView(SettingsView.class, "Settings");
            });
        }

        return openSettings;
    }

    /** REGION: DATABASE VIEW */

    private String baseDirPath = DatabaseManager.DATABASE_PATH;

    private ExplorerFile baseFile;

    private Command deleteFileCommand;

    private Command renameSelectedFileCommand;

    private Command addFileCommand;

    private DatabaseManager manager = Workspace.getInstance().getDatabaseManager();

    private DatabaseRepository repo = manager.getRepository();

    /**
     * Adds a file to the files.
     * 
     * @param file The file to add
     * @return True if file added
     */
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

    /**
     * Command for adding new files via dialog.
     *
     * @return The command
     */
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

    /**
     * This violates MVVM, but its the only way we can consistently have the
     * currently seletecd file in the ViewModel.
     */
    private ObjectProperty<TreeItem<ExplorerFile>> selectedExplorerFile = new SimpleObjectProperty<>();

    /**
     * Recursively adds files to a given ExplorerFile.
     *
     * @param explorerFile The ExplorerFile to add files to
     */
    private void addChildFiles(ExplorerFile explorerFile) {
        if (explorerFile.isDirectory()) {
            var files = repo.loadFile(explorerFile.getAbsolutePath()).listFiles();
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
     * A command to delete the currenlty selected ExplorerFile.
     *
     * @return The command
     */
    public Command deleteSelectedFileCommand() {
        if (deleteFileCommand == null) {
            deleteFileCommand = runnableCommand(() -> {
                var selectedFile = getSelectedFile();
                // Dont allow removing deleting base file
                if (selectedFile != null && !selectedFile.getName().equals(baseFile.getName()))
                    deleteFile(selectedFile);
            });
        }

        return deleteFileCommand;
    }

    /**
     * Deletes a given explorer file.
     */
    public boolean deleteFile(ExplorerFile file) {
        if (file != null && !file.getName().equals(baseFile.getName()))
            if (repo.deleteFile(file.getAbsolutePath())) {
                removeExplorerFile(file, baseFile);
                return true;
            }

        return false;
    }

    private Command showInExplorerCommand;

    /**
     * A command to show the selected file in the os explorer.
     * 
     * @return The command
     */
    public Command showInExplorerCommand() {
        if (showInExplorerCommand == null) {
            showInExplorerCommand = runnableCommand(() -> {
                var selectedFile = getSelectedFile();
                try {
                    switch (VISABUtil.getOS()) {
                    case WINDOWS:
                        var osCommand = "explorer.exe " + baseDirPath;
                        if (selectedFile != null)
                            osCommand = "explorer.exe /select," + selectedFile.getAbsolutePath();

                        Runtime.getRuntime().exec(osCommand);
                        break;
                    default:
                        System.out.println("Opening explorer not supported for non windows OS.");
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        return showInExplorerCommand;
    }

    /**
     * Gets the path of the base directory of the ExplorerViewModel
     * 
     * @return The path to the base directory
     */
    public String getBaseDirPath() {
        return this.baseDirPath;
    }

    /**
     * Initializes a ExplorerFile object from a given file.
     *
     * @param file The file to create a ExplorerFile of
     * @return The created ExplorerFile
     */
    private ExplorerFile getExplorerFile(File file, ExplorerFile parentDir) {
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
        var file = repo.loadFile(baseDirPath);
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
    private ExplorerFile getSelectedFile() {
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
    private void removeExplorerFile(ExplorerFile fileToRemove, ExplorerFile parentFile) {
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
     * @return The command
     */
    public Command renameFileCommand() {
        if (renameSelectedFileCommand == null) {
            renameSelectedFileCommand = runnableCommand(() -> {
                var selectedFile = getSelectedFile();
                if (selectedFile != null && selectedFile != baseFile) {
                    var newName = dialogHelper.showInputDialog("New File Name:", selectedFile.getName(), "Rename File");

                    if (newName != "") {
                        var newFilePath = repo.combinePath(selectedFile.getParentDir().getAbsolutePath(), newName);
                        if (repo.renameFile(selectedFile.getAbsolutePath(), newFilePath)) {
                            var oldName = selectedFile.getName();

                            selectedFile.setName(newName);
                            selectedFile.setAbsolutePath(newFilePath);

                            if (selectedFile.isDirectory())
                                changeChildrenAbsolutePath(selectedFile, oldName, newName);
                        }
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

    /** ENDREGION: DATABASE VIEW */

}
