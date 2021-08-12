package org.visab.gui;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.visab.globalmodel.GameName;
import org.visab.main.Main;

/**
 * Class containing constants and helper methods for obtaining resources from
 * the resource directory. Used mainly by the views, but is also used for
 * loading configuration files.
 */
public final class ResourceHelper {

    public static final String JSON_MIME_TYPE = "application/json";
    public static final String MEDIA_CONTENT_TYPE = JSON_MIME_TYPE;
    public static final String CSS_PATH = "/application.css";
    public static final String VISAB_DOC_PATH = "/pdf/visab_documentation.pdf";
    public static final String IMAGE_PATH = "/img/";

    private static final Map<String, String> gameLogoPaths = new HashMap<String, String>() {
        {
            put(GameName.CBR_SHOOTER, IMAGE_PATH + "CBRShooterLogo.png");
            put(GameName.SETTLERS_OF_CATAN, IMAGE_PATH + "settlersLogo.png");
        }
    };

    private static final Map<String, String> shooterBaseIconMap = new HashMap<String, String>() {
        {
            put("playerPlanChange", IMAGE_PATH + "playerPlanChange.png");
            put("playerDeath", IMAGE_PATH + "playerDeath.png");
        }
    };

    /**
     * Helper method that provides a specific game logo path by game name.
     * 
     * @param game the name of the game the logo is needed for.
     * @return the logo path for the respective game.
     */
    public static final String getLogoPathByGame(String game) {
        if (gameLogoPaths.containsKey(game))
            return gameLogoPaths.get(game);
        else
            return gameLogoPaths.get(GameName.CBR_SHOOTER); // TODO: Add default image
    }

    /**
     * Helper method that provides a player specific base icon. Base icon means that
     * it is white and can be recoloured.
     * 
     * @param iconId the id of the base icon needed.
     * @return the base icon for the given iconId.
     */
    public static final String getShooterBaseIconById(String iconId) {
        return shooterBaseIconMap.get(iconId);
    }

    /**
     * Reads the bytes for a resource at a given path and returns a the byte array
     * as a UTF-8 encoded string.
     * 
     * @param path The path to the resource
     * @return The UTF-8 encoded string
     */
    public static final String readResourceContents(String path) {
        if (!path.startsWith("/"))
            path = "/" + path;

        byte[] data = null;
        try (var stream = Main.class.getResourceAsStream(path)) {
            data = stream.readAllBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new String(data, StandardCharsets.UTF_8);
    }

}
