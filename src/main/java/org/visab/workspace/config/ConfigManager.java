package org.visab.workspace.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.api.WebApi;
import org.visab.util.JsonConvert;
import org.visab.util.StreamUtil;
import org.visab.util.UserSettings;
import org.visab.util.VISABUtil;
import org.visab.workspace.ConfigRepository;
import org.visab.workspace.Workspace;
import org.visab.workspace.config.model.Mapping;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * The ConfigManager that is used for loading and modifying settings and dynamic
 * mappings.
 * 
 * TODO: Allowed games has to be added to settings.
 */
public class ConfigManager {

    // Logger needs .class for each class to use for log traces
    private static Logger logger = LogManager.getLogger(ConfigManager.class);

    public static final String JSON_MIME_TYPE = "application/json";
    public static final String MEDIA_CONTENT_TYPE = JSON_MIME_TYPE;
    public static final String DATA_PATH_APPENDIX = "database";
    public static final String CONFIG_PATH_APPENDIX = "config";
    public static final String CSS_PATH = "/application.css";
    public static final String VISAB_DOC_PATH = "/pdf/visab_documentation.pdf";
    public static final String IMAGE_PATH = "/img/";

    private static final HashMap<String, String> gameLogoPaths = new HashMap<String, String>() {
        {
            put("CBRShooter", IMAGE_PATH + "CBRShooterLogo.png");
            put("Settlers", IMAGE_PATH + "settlersLogo.png");
        }
    };

    public static final String CBR_SHOOTER_STRING = "CBRShooter";
    public static final String SETTLERS_OF_CATAN_STRING = "Settlers";

    public static final String CONFIG_PATH = VISABUtil.combinePath(Workspace.WORKSPACE_PATH, CONFIG_PATH_APPENDIX);

    private static final String SETTINGS_PATH = "settings.json";

    private ConfigRepository repo = new ConfigRepository(CONFIG_PATH);

    private List<Mapping> mappings;

    private UserSettings settings;

    public ConfigManager() {
        loadSettings();
        loadMappings();
    }

    public List<Mapping> getMappings() {
        return this.mappings;
    }

    /**
     * Gets the mapping for a given game.
     * 
     * @param game The game to get the mapping for
     * @return The mapping if mappings contained it, null else
     */
    public Mapping getMapping(String game) {
        return StreamUtil.firstOrNull(mappings, x -> x.getGame().equals(game));
    }

    /**
     * Loads the mappings from the filesystem using the repository.
     * 
     * @return The list of mappings if successfully loaded, throws RuntimeException
     *         else
     */
    private List<Mapping> loadMappings() {
        var json = VISABUtil.readResourceContents("/configs/classMapping.json");
        var mappings = JsonConvert.deserializeJson(json, new TypeReference<List<Mapping>>() {
        });

        if (mappings == null) {
            logger.error("Failed to load mappings!");
            throw new RuntimeException("Failed to load mappings!");
        }

        Function<String, Boolean> classExists = s -> {
            try {
                Class.forName(s);
                return true;
            } catch (Exception e) {
                return false;
            }
        };

        // Do validation check
        for (var mapping : mappings) {
            var hasGame = mapping.getGame() != null && !mapping.getGame().isBlank();
            if (!hasGame)
                throw new RuntimeException("A mapping needs a game!");

            var hasMeta = mapping.getMetaInformation() != null && !mapping.getMetaInformation().isBlank()
                    && classExists.apply(mapping.getMetaInformation());
            if (!hasMeta)
                throw new RuntimeException("A mapping needs meta information!");

            var hasStatistics = mapping.getMetaInformation() != null && !mapping.getMetaInformation().isBlank()
                    && classExists.apply(mapping.getStatistics());
            if (!hasStatistics)
                throw new RuntimeException("A mapping needs statistics!");

            var hasListener = mapping.getMetaInformation() != null && !mapping.getMetaInformation().isBlank()
                    && classExists.apply(mapping.getListener());
            if (!hasListener)
                throw new RuntimeException("A mapping needs a listener!");

            var hasFile = mapping.getFile() != null && !mapping.getFile().isBlank()
                    && classExists.apply(mapping.getFile());
            if (!hasFile)
                throw new RuntimeException("A mapping needs a file!");

            var hasImage = mapping.getMetaInformation() != null && !mapping.getMetaInformation().isBlank();
        }

        this.mappings = mappings;

        return mappings;
    }

    /**
     * Loads the settings from the file system using the repository
     * 
     * @return Object of the loaded settings.
     */
    public UserSettings loadSettings() {
        UserSettings loadedSettings = repo.loadSettingsObject(SETTINGS_PATH);
        if (loadedSettings == null) {
            logger.info("User settings do not exist yet, loading defaults.");
            String defaultSettings = VISABUtil.readResourceContents("/configs/defaultSettings.json");
            repo.writeToFileRelative(SETTINGS_PATH, defaultSettings);
            loadedSettings = repo.loadSettingsObject(SETTINGS_PATH);
        }

        settings = loadedSettings;
        return settings;
    }

    /**
     * Saves the settings to the file system using the repository.
     * 
     * @param settingsObject The object of the settings.
     */
    public void saveSettings() {
        repo.saveSettings(this.settings, SETTINGS_PATH);
    }

    /**
     * Syntactic sugar to wrap the access on the settings object.
     * 
     * Getter for the webApiPort.
     * 
     * @return The webApiPort.
     */
    public int getWebApiPort() {
        return this.settings.getWebApiPort();
    }

    /**
     * Syntactic sugar to wrap the access on the settings object.
     * 
     * Getter for the allowedGames.
     * 
     * @return The allowedGames.
     */
    public ArrayList<String> getAllowedGames() {
        return this.settings.getAllowedGames();
    }

    /**
     * Syntactic sugar to wrap the access on the settings object.
     * 
     * Getter for the sessionTimeouts.
     * 
     * @return The SessionTimeouts.
     */
    public HashMap<String, Integer> getSessionTimeout() {
        return this.settings.getSessionTimeout();
    }

    /**
     * Syntactic sugar to wrap the access on the settings object that also provides
     * detailed logging information according to the changes made.
     * 
     * Updates the webApiPort.
     * 
     * @param port The new port.
     */
    public void updateWebApiPort(int port) {
        int previousPort = this.settings.getWebApiPort();

        if (port != previousPort) {
            logger.info("Changed webApiPort from " + previousPort + " to " + port + ".");
            // WebApi needs to be restarted because the port changed
            WebApi.getInstance().restart();
        }

        this.settings.setWebApiPort(port);
    }

    /**
     * Syntactic sugar to wrap the access on the settings object that also provides
     * detailed logging information according to the changes made.
     * 
     * Updates the list of allowed games.
     * 
     * @param games The new allowed games.
     */
    public void updateAllowedGames(ArrayList<String> games) {

        // Check if current version of allowed games contains a new game
        for (String game : games) {
            if (!this.settings.getAllowedGames().contains(game)) {
                logger.info("Added new game to allowedGame list: " + game + ".");
                HashMap<String, Integer> map = getSessionTimeout();
                map.put(game, 10);
                updateSessionTimeout(map);
                logger.info("Added session timeout: 10, for new game: " + game);
            }
        }
        // Check if current version of allowed games is missing a game which was
        // previously allowed
        for (String game : this.settings.getAllowedGames()) {
            if (!games.contains(game)) {
                logger.info("Removed game: " + game + " from allowedGame list.");
                HashMap<String, Integer> map = getSessionTimeout();
                map.remove(game);
                updateSessionTimeout(map);
                logger.info("Removed session timeout for game: " + game);
            }
        }
        // Renaming a game will result in both logs to be printed

        this.settings.setAllowedGames(games);
    }

    /**
     * Helper method that provides a specific game logo path by game name.
     * 
     * @param game the name of the game the logo is needed for.
     * @return the logo path for the respective game.
     */
    public String getLogoPathByGame(String game) {
        return gameLogoPaths.get(game);
    }

    /**
     * Updates the timeouts from the games.
     * 
     * @param timeouts The timeouts Map.
     */
    public void updateSessionTimeout(HashMap<String, Integer> timeouts) {
        logger.info("Updated the session timeouts");
        this.settings.setSessionTimeout(timeouts);
    }

    public boolean isGameSupported(String game) {
        return this.settings.getAllowedGames().contains(game);
    }
}
