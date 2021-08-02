package org.visab.workspace;

import java.io.File;

/**
 * The IRepository interface that all Repositories have to implement.
 * Repositories are used for I/O operations, so that we dont have to spread
 * those accross the project.
 */
public interface IRepository {

    /**
     * Combines path strings to one string
     * 
     * @param path The base path
     * @param more The paths to add
     * @return The combined path
     */
    String combinePath(String path, String... more);

    /**
     * Creates missing directorie for a given file path
     * 
     * @param filePath The file path
     * @return True if directories were created
     */
    boolean createMissingDirectories(String filePath);

    /**
     * Deletes a file at a given path
     *
     * @param filePath The path of the file to delete
     * @return True if deleted
     */
    boolean deleteFile(String filePath);

    /**
     * Recursively deletes a folder and all its contents
     * 
     * @param folder The folder to delete
     * @return True if everything was deleted
     */
    boolean deleteFolder(File folder);

    /**
     * Reads in a file at a given path
     * 
     * @param filePath The path to the file
     * @return The read in file
     */
    File loadFile(String filePath);

    /**
     * Reads the contents of a file at a given path
     *
     * @param filePath The path to the file
     * @return The contents of the file or empty string if unsuccessful
     */
    String readFileContents(String filePath);

    /**
     * Renames a file at a given path to
     * 
     * @param filePath    The path to the file to rename
     * @param newFilePath The new path for the file
     * @return True if file was renamed succesfully
     */
    boolean renameFile(String filePath, String newFilePath);

    /**
     * Writes string content to a file at a given path
     *
     * @param filePath The path to the file
     * @param content  The content of the file
     * @return True if file was successfully written
     */
    boolean writeToFile(String filePath, String content);

}
