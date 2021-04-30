package org.visab.repository;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;
import java.nio.file.Files;

import org.visab.processing.IVISABFile;
import org.visab.processing.cbrshooter.CBRShooterFile;
import org.visab.util.AssignByGame;
import org.visab.util.JsonConvert;
import org.visab.util.Settings;
import org.visab.util.VISABUtil;


public class VISABRepository {
    
    public static void main(String[] args) {
        var file = new CBRShooterFile(UUID.randomUUID().toString());
        
        var repo = new VISABRepository();
        repo.saveFile(file);
        // repo.saveFile(file);
        var z = repo.<CBRShooterFile>loadFileByName(AssignByGame.CBR_SHOOTER_STRING, "a4e8bbcb-039e-4bfb-917a-91a31d2e0a9a");
        var x = repo.<CBRShooterFile>loadFileByPath(AssignByGame.CBR_SHOOTER_STRING, "C:\\Users\\moritz\\Desktop\\VISAB\\target\\database\\CBRShooter\\" + file.getFileName());
    }

    private static final String baseDir = Settings.DATA_PATH;

    // TODO: Can this really be the interface or does it have to be a generic type reference?
    public boolean saveFile(IVISABFile visabFile) {
        var json = JsonConvert.serializeObject(visabFile);

        var fileDir = baseDir + visabFile.getGame();
        new File(fileDir).mkdirs();

        var filePath = fileDir + "/" + visabFile.getFileName();
        if (!filePath.endsWith(".visab2") && !filePath.endsWith(".visab")) {
            filePath += ".visab2";
        }

        return writeFile(filePath, json);
    }

    @SuppressWarnings("unchecked")
    public <T> T loadFileByPath(String game, String filePath) {
        if (!filePath.endsWith(".visab2") && !filePath.endsWith(".visab"))
            filePath += ".visab2";
            
        var json = readFile(filePath);

        try {
            return (T)AssignByGame.getDeserializedFile(json, game);
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public <T> T loadFileByName(String game, String fileName) {
        var filePath = baseDir + game + "/" + fileName;

       return loadFileByPath(game, filePath);
    }

    private static boolean writeFile(String filePath, String content) {
        var file = new File(filePath);
        
        try (var writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();

            return false;
        }

        return true;
    }

    private static String readFile(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            e.printStackTrace();

            return "";
        }
    }

}
