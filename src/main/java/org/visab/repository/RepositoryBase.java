package org.visab.repository;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The base repository that all repositories should inherit from. Used for any
 * IO operations inside of VISAB. If you need to import any java IO libs in non
 * repository classes, you are likely doing it wrong. Create a specific
 * repository instead or check if there isnt a repositry implementation that
 * supports your wanted action.
 */
public abstract class RepositoryBase {

    protected String baseDirectory;

    /**
     * @param baseDirectory The repositories base directory used for relative
     *                      methods
     */
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

    /**
     * Renames a file at a given path to
     * 
     * @param filePath    The path to the file to rename
     * @param newFilePath The new path for the file
     * @return True if file was renamed succesfully
     */
    public boolean renameFile(String filePath, String newFilePath) {
        var file = loadFile(filePath);

        var newFile = new File(newFilePath);

        return file.renameTo(newFile);
    }

    /**
     * Combines path strings to one string
     * 
     * @param path The base path
     * @param more The paths to add
     * @return The combined path
     */
    public String combinePath(String path, String... more) {
        return Path.of(path, more).toString();
    }

    /**
     * Creates missing directorie for a given file path
     * 
     * @param filePath The file path
     * @return True if directories were created
     */
    public boolean createMissingDirectories(String filePath) {
        return new File(filePath).mkdirs();
    }

    /**
     * Reads the contents of a file at a given path
     *
     * @param filePath The path to the file
     * @return The contents of the file or empty string if unsuccessful
     */
    public String readFileContents(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            e.printStackTrace();

            return "";
        }
    }

    /**
     * Reads in a file at a given path
     * 
     * @param filePath The path to the file
     * @return The read in file
     */
    public File loadFile(String filePath) {
        return new File(filePath);
    }

    /**
     * Writes string content to a file at a given path
     *
     * @param filePath The path to the file
     * @param content  The content of the file
     * @return True if file was successfully written
     */
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
     * Deletes a file at a given path
     *
     * @param filePath The path of the file to delete
     * @return True if deleted
     */
    public boolean deleteFile(String filePath) {
        var file = new File(filePath);

        if (!file.isDirectory())
            return file.delete();
        else
            return deleteFolder(file);
    }

    /**
     * Recursively deletes a folder and all its contents
     * 
     * @param folder The folder to delete
     * @return True if everything was deleted
     */
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

    /**
     * [CONVENIENCE] Writes to a file with to a path relative to the base directory
     * 
     * @param content      The content to write to the file
     * @param relativePath The relative path of the file
     * @return True if successfully saved
     */
    public boolean writeToFileRelative(String content, String relativePath) {
        var filePath = combinePath(baseDirectory, relativePath);

        return writeToFile(filePath, content);
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

}
