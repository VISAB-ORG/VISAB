package org.visab.workspace.config;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.gui.GUIMain;
import org.visab.util.StreamUtil;
import org.visab.util.UserObject;
import org.visab.util.VISABUtil;
import org.visab.workspace.ConfigRepository;
import org.visab.workspace.Workspace;
import org.visab.workspace.config.model.MappingConfig;
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

    public static final String CONFIG_PATH = VISABUtil.combinePath(Workspace.WORKSPACE_PATH, "config");

    private static final String MAPPING_PATH = "classMapping.json";

    private static final String SETTINGS_PATH = "settings.json";

    private ConfigRepository repo = new ConfigRepository(CONFIG_PATH);

    private List<MappingConfig> mappings;

    private UserObject settings;

    public ConfigManager() {
        loadSettings();
        // TODO: Load settings first, so that they can be used for mapping
        // initialization. Important in case we decide to make it customizable where to
        // save your mappings.
        loadMappings();
    }

    public List<MappingConfig> getMappings() {
        return this.mappings;
    }

    /**
     * Replaces an existing mapping with a new Mapping
     * 
     * @param game       The game to replace the mapping of
     * @param newMapping The new mapping
     * @return True if successful
     */
    public boolean replaceMapping(String game, MappingConfig newMapping) {
        var mappingCopy = new ArrayList<MappingConfig>(mappings);
        for (int i = 0; i < mappingCopy.size(); i++) {
            var mapping = mappingCopy.get(i);

            if (mapping.getGame().equals(newMapping.getGame())) {
                mappings.set(i, newMapping);

                return true;
            }
        }

        return false;
    }

    /**
     * Gets the mapping for a given game.
     * 
     * @param game The game to get the mapping for
     * @return The mapping if mappings contained it, null else
     */
    public MappingConfig getMapping(String game) {
        return StreamUtil.firstOrNull(mappings, x -> x.getGame().equals(game));
    }

    /**
     * Adds a mapping to the mappings.
     * 
     * @param newMapping The mapping to add
     * @return True if successful
     */
    public boolean addMapping(MappingConfig newMapping) {
        if (getMapping(newMapping.getGame()) == null) {
            mappings.add(newMapping);

            return true;
        }

        return false;
    }

    /**
     * Removes a mapping by game from the mappings.
     * 
     * @param game The game to remove the mapping for
     * @return True if successful
     */
    public boolean removeMapping(String game) {
        return mappings.removeIf(x -> x.getGame().equals(game));
    }

    /**
     * Removes a mappign from the mappings.
     * 
     * @return True if successful
     */
    public boolean removeMapping(MappingConfig mapping) {
        return mappings.remove(mapping);
    }

    /**
     * Saves the mappings to the filesystem using the repository.
     * 
     * @return True if successfully saved
     */
    public boolean saveMappings() {
        return repo.saveMappings(mappings, MAPPING_PATH);
    }

    /**
     * Loads the mappings from the filesystem using the repository.
     * 
     * @return True if successfully loaded
     */
    private boolean loadMappings() {
        var loadedMappings = repo.loadMappings(MAPPING_PATH);

        if (loadedMappings == null) {
        	logger.info("User mappings do not exist yet, loading defaults.");
            // Load the default file. Save it to file system. Load again.
            var defaultPath = VISABUtil.getResourcePath("/configs/classMapping_DEFAULT.json");
            var defaultJson = repo.readFileContents(defaultPath);
            repo.writeToFileRelative(MAPPING_PATH, defaultJson);
            loadedMappings = repo.loadMappings(MAPPING_PATH);
        }
        this.mappings = loadedMappings;

        return loadedMappings != null;
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
    public UserObject loadSettings() {
    	UserObject loadedSettings = repo.loadSettingsObject(SETTINGS_PATH);
        if (loadedSettings == null) {
        	logger.info("User settings do not exist yet, loading defaults.");
            String defaultPath = VISABUtil.getResourcePath("/configs/defaultSettings.json");
            String defaultSettings = repo.readFileContents(defaultPath);
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
    public void saveSettings(UserObject settingsObject) {
        repo.saveSettings(settingsObject, SETTINGS_PATH);
    }

    /**
     * Getter for the UserSettings.
     * 
     * @return The settingsObject of the user settings.
     */
    public UserObject getSettings() {
        return settings;
    }

}
