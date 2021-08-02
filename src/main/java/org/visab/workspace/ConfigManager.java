package org.visab.workspace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.api.WebAPI;
import org.visab.newgui.ResourceHelper;
import org.visab.util.JSONConvert;
import org.visab.util.StreamUtil;
import org.visab.util.VISABUtil;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * The ConfigManager that is used for loading mappings and settings aswell as
 * modifying settings.
 */
public class ConfigManager {

    private static Logger logger = LogManager.getLogger(ConfigManager.class);

    public static final String CONFIG_PATH_SUFFIX = "config";
    private static final String CONFIG_PATH = VISABUtil.combinePath(Workspace.WORKSPACE_PATH, CONFIG_PATH_SUFFIX);

    private static final String SETTINGS_PATH = "settings.json";
    private static final String MAPPING_PATH = "classMapping.json";
    private static final String DEFAULT_SETTINGS_PATH = "/configs/defaultSettings.json";

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
     * Loads the mappings from the filesystem using the repository. Performs a
     * validation check on the Mappings POJOs and throws RuntimeException if not
     * valid.
     */
    private void loadMappings() {
        var json = ResourceHelper.readResourceContents(MAPPING_PATH);
        var mappings = JSONConvert.deserializeJson(json, new TypeReference<List<Mapping>>() {
        }, JSONConvert.ForgivingMapper);

        if (mappings == null) {
            logger.error("Failed to load mappings!");
            throw new RuntimeException("Failed to load mappings!");
        }

        Function<String, Boolean> mappingClassExists = s -> {
            if (s == null || s.isBlank())
                return false;

            try {
                Class.forName(s);
                return true;
            } catch (Exception e) {
                return false;
            }
        };

        // Do validation check
        for (var mapping : mappings) {
            if (mapping.getGame() == null || mapping.getGame().isBlank())
                throw new RuntimeException("A mapping needs a game!:" + mapping.getGame());

            if (!mappingClassExists.apply(mapping.getMetaInformation()))
                throw new RuntimeException("A mapping needs meta information!:" + mapping.getGame());

            if (!mappingClassExists.apply(mapping.getStatistics()))
                throw new RuntimeException("A mapping needs statistics!:" + mapping.getGame());

            if (!mappingClassExists.apply(mapping.getListener()))
                throw new RuntimeException("A mapping needs a listener!:" + mapping.getGame());

            if (!mappingClassExists.apply(mapping.getFile()))
                throw new RuntimeException("A mapping needs a file!");

            if (mapping.getImage() != null && !mappingClassExists.apply(mapping.getImage())) {
                throw new RuntimeException("Image class could not be resolved!:" + mapping.getGame());
            }

            if (mapping.getVisualizer() != null && !mappingClassExists.apply(mapping.getVisualizer())) {
                throw new RuntimeException("Visualizer class could not be resolved!:" + mapping.getGame());
            }
        }

        this.mappings = mappings;
    }

    /**
     * Loads the settings from the file system using the repository.;
     */
    private void loadSettings() {
        UserSettings loadedSettings = repo.loadSettingsObject(SETTINGS_PATH);
        if (loadedSettings == null) {
            logger.info("User settings do not exist yet, loading defaults.");
            String defaultSettings = ResourceHelper.readResourceContents(DEFAULT_SETTINGS_PATH);
            repo.writeToFileRelative(SETTINGS_PATH, defaultSettings);
            loadedSettings = repo.loadSettingsObject(SETTINGS_PATH);
        }

        settings = loadedSettings;
    }

    /**
     * Saves the settings to the file system using the repository.
     */
    public void saveSettings() {
        repo.saveSettings(this.settings, SETTINGS_PATH);
    }

    /**
     * Restores the default settings to the file system using the repository.
     */
    public void restoreDefaultSettings() {
        String defaultSettings = ResourceHelper.readResourceContents(DEFAULT_SETTINGS_PATH);
        repo.writeToFileRelative(SETTINGS_PATH, defaultSettings);
        UserSettings loadedSettings = repo.loadSettingsObject(SETTINGS_PATH);
        this.settings = loadedSettings;
    }

    /**
     * Syntactic sugar to wrap the access on the settings object.
     * 
     * @return The webApiPort.
     */
    public int getWebApiPort() {
        return this.settings.getWebApiPort();
    }

    /**
     * Syntactic sugar to wrap the access on the settings object.
     * 
     * @return The allowedGames.
     */
    public ArrayList<String> getAllowedGames() {
        return this.settings.getAllowedGames();
    }

    /**
     * Syntactic sugar to wrap the access on the settings object.
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
     * @param port The new port.
     */
    public void updateWebApiPort(int port) {
        int previousPort = this.settings.getWebApiPort();

        if (port != previousPort) {
            logger.info("Changed webApiPort from " + previousPort + " to " + port + ".");
            // WebApi needs to be restarted because the port changed
            WebAPI.getInstance().restart();
        }

        this.settings.setWebApiPort(port);
    }

    /**
     * Syntactic sugar to wrap the access on the settings object that also provides
     * detailed logging information according to the changes made and a validation
     * check.
     * 
     * @param games The new allowed games.
     */
    public boolean updateAllowedGames(ArrayList<String> games) {
        boolean settingsHaveChanged = false;

        // Check if current version of allowed games contains a new game
        for (String game : games) {
            if (!this.settings.getAllowedGames().contains(game)) {
                logger.info("Added new game to allowedGame list: " + game + ".");
                HashMap<String, Integer> map = getSessionTimeout();
                map.put(game, 10);
                updateSessionTimeout(map);
                logger.info("Added session timeout: 10, for new game: " + game);
                settingsHaveChanged = true;
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
                settingsHaveChanged = true;
            }
        }

        // Renaming a game will result in both logs to be printed
        this.settings.setAllowedGames(games);

        return settingsHaveChanged;
    }

    /**
     * Updates the timeouts from the games.
     * 
     * TODO: Needs validation so timeouts > or >= 0
     * 
     * @param timeouts The timeouts Map.
     */
    public void updateSessionTimeout(HashMap<String, Integer> timeouts) {
        logger.info("Updated the session timeouts");
        this.settings.setSessionTimeout(timeouts);
    }

    /**
     * Whether the given game is allowed by VISAB.
     * 
     * @param game The game to check
     * @return True if it is allowed
     */
    public boolean isGameAllowed(String game) {
        return this.settings.getAllowedGames().contains(game);
    }
}
