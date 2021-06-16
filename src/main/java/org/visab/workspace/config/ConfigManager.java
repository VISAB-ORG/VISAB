package org.visab.workspace.config;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;

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
import org.visab.workspace.config.model.ViewConfig;

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
        var json = VISABUtil.readResourceContents("/configs/classMapping_DEFAULT.json");
        var mappings = JsonConvert.deserializeJson(json, new TypeReference<List<Mapping>>() {
        });

        if (mappings == null) {
            logger.error("Failed to load mappings!");
            throw new RuntimeException("Failed to load mappings!");
        }

        this.mappings = mappings;

        return mappings;
    }

    /**
     * Gets a view configuration of a game based on the views identifier.
     * 
     * Example usage: getViewMapping(CBRShooter, statistics);
     * 
     * @param game           The game to get the view configuration for
     * @param viewIdentifier The identifier for the view
     * @return The ViewConfig if found, null else
     */
    public ViewConfig getViewMapping(String game, String viewIdentifier) {
        var mapping = getMapping(game);
        if (mapping != null) {
            return StreamUtil.firstOrNull(mapping.getViewConfigurations(),
                    x -> x.getIdentifier().equals(viewIdentifier));
        }

        return null;
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
     * Getter for the sessionTimeout.
     * 
     * @return The sessionTimeout.
     */
    public int getSessionTimeout() {
        return this.settings.getSessionTimeout();
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
     * Updates the sessionTimeout time.
     * 
     * @param timeout The new sessionTimeout.
     */
    public void updateSessionTimeout(int timeout) {
        int oldTimeout = this.settings.getSessionTimeout();
        if (timeout == 0) {
            logger.error("Value 0 is not allowed for sessionTimeout, please provide a value > 0.");
            timeout = oldTimeout;
        } else if (timeout > 0 && timeout != oldTimeout) {
            logger.info("Changed sessionTimeout from " + oldTimeout + "seconds to " + timeout + "seconds.");
        }

        this.settings.setSessionTimeout(timeout);
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
            }
        }
        // Check if current version of allowed games is missing a game which was
        // previously allowed
        for (String game : this.settings.getAllowedGames()) {
            if (!games.contains(game)) {
                logger.info("Removed game: " + game + " from allowedGame list.");
            }
        }
        // Renaming a game will result in both logs to be printed

        this.settings.setAllowedGames(games);
    }

    public boolean isGameSupported(String game) {
        return this.settings.getAllowedGames().contains(game);
    }
}
