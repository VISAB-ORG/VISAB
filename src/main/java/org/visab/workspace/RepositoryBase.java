package org.visab.workspace;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The base repository that all repositories should inherit from. Used for any
 * I/O operations inside of VISAB. If you need to import any java IO libs in non
 * repository classes, you are likely doing it wrong. Create a specific
 * repository instead or check if there isnt a repositry implementation that
 * supports your wanted functionality already.
 */
public abstract class RepositoryBase implements IRepository {

    protected String baseDirectory;

    public RepositoryBase(String baseDirectory) {
        this.baseDirectory = baseDirectory;

        // Create the base directory if it doesn't exist.
        createMissingDirectories(baseDirectory);
    }

    /**
     * Returns the path to the base directory.
     * 
     * @return The base directory
     */
    public String getBaseDirectory() {
        return this.baseDirectory;
    }

    @Override
    public String combinePath(String path, String... more) {
        return Path.of(path, more).toString();
    }

    @Override
    public boolean createMissingDirectories(String filePath) {
        return new File(filePath).mkdirs();
    }

    @Override
    public boolean deleteFile(String filePath) {
        var file = new File(filePath);

        if (!file.isDirectory())
            return file.delete();
        else
            return deleteFolder(file);
    }

    @Override
    public boolean deleteFolder(File folder) {
        var files = folder.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory())
                    deleteFolder(f);
                else {
                    if (!f.delete())
                        return false;
                }
            }
        }

        return folder.delete();
    }

    @Override
    public File loadFile(String filePath) {
        return new File(filePath);
    }

    /**
     * [CONVENIENCE] Loads a file with a path relative to the base directory
     * 
     * @param relativePath The relative path of the file
     * @return The loaded file
     */
    public File loadFileRelative(String relativePath) {
        var filePath = combinePath(baseDirectory, relativePath);

        return loadFile(filePath);
    }

    @Override
    public String readFileContents(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            e.printStackTrace();

            return "";
        }
    }

    @Override
    public boolean renameFile(String filePath, String newFilePath) {
        var file = loadFile(filePath);

        var newFile = new File(newFilePath);

        return file.renameTo(newFile);
    }

    @Override
    public boolean writeToFile(String filePath, String content) {
        var file = new File(filePath);
        createMissingDirectories(file.getParent());

        try (var writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();

            return false;
        }

        return true;
    }

    /**
     * [CONVENIENCE] Writes to a file with to a path relative to the base directory
     * 
     * @param relativePath The relative path of the file
     * @param content      The content to write to the file
     * @return True if successfully saved
     */
    public boolean writeToFileRelative(String relativePath, String content) {
        var filePath = combinePath(baseDirectory, relativePath);

        return writeToFile(filePath, content);
    }

}
