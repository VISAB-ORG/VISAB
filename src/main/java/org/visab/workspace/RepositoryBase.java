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
 * IO operations inside of VISAB. If you need to import any java IO libs in non
 * repository classes, you are likely doing it wrong. Create a specific
 * repository instead or check if there isnt a repositry implementation that
 * supports your wanted action.
 */
public abstract class RepositoryBase implements IRepository {

    protected String baseDirectory;

    public RepositoryBase(String baseDirectory) {
        this.baseDirectory = baseDirectory;

        // Create the base directory if it doesn't exist.
        createMissingDirectories(baseDirectory);
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
    public String getBaseDirectory() {
        return this.baseDirectory;
    }

    @Override
    public File loadFile(String filePath) {
        return new File(filePath);
    }

    @Override
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

    @Override
    public boolean writeToFileRelative(String relativePath, String content) {
        var filePath = combinePath(baseDirectory, relativePath);

        return writeToFile(filePath, content);
    }

}
