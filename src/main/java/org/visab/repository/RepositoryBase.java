package org.visab.repository;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class RepositoryBase {

    protected String baseDirectory;

    public RepositoryBase(String baseDirectory) {
        this.baseDirectory = baseDirectory;

        // Create the base directory if it doesn't exist.
        new File(baseDirectory).mkdirs();
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

}
