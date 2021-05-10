package org.visab.repository;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class RepositoryBase {

    protected String baseDirectory;

    public RepositoryBase(String baseDirectory) {
        this.baseDirectory = baseDirectory;

        // Create the base directory if it doesn't exist.
        new File(baseDirectory).mkdirs();
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
     * @return True if directories were created, false else
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
     * @return True if file was successfully written, false else
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
     * @return True if deleted, false else
     */
    public boolean deleteFile(String filePath) {
        var file = new File(filePath);

        if (!file.isDirectory())
            return file.delete();
        else
            return deleteFolder(file);
    }

    /**
     * Deletes a folder and all its contents
     * 
     * @param folder The folder to delete
     * @return True if fully deleted, false else
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
     * Writes to a file with a path relative to the base directory
     * 
     * @param content      The content to write to the file
     * @param relativePath The relative path of the file
     * @return True if successfully saved, false else
     */
    public boolean writeToFileRelative(String content, String relativePath) {
        var filePath = combinePath(baseDirectory, relativePath);

        return writeToFile(filePath, content);
    }

    /**
     * Loads a file with a path relative to the base directory
     * 
     * @param relativePath The relative path of the file
     * @return The loaded file
     */
    public File loadFileRelative(String relativePath) {
        var filePath = combinePath(baseDirectory, relativePath);

        return loadFile(filePath);
    }

}
