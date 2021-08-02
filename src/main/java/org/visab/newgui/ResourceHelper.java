package org.visab.newgui;

import java.util.HashMap;
import java.util.Map;

import org.visab.globalmodel.GameName;

/**
 * Class containing constants and helper methods for obtaining resources used
 * inside the views.
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
        return gameLogoPaths.get(game);
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

}
