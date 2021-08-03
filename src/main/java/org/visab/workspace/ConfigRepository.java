package org.visab.workspace;

import java.io.File;

import org.visab.util.JSONConvert;

/**
 * The ConfigRepository that is used for loading and saving Settings to the
 * database.
 */
public class ConfigRepository extends RepositoryBase {

    public ConfigRepository(String configPath) {
        super(configPath);
    }

    /**
     * Loads the settings json from the database and deserializes it into
     * UserSettings.
     * 
     * @param relativeSettingsPath The relative path to the settings file.
     * @return The UserSettings instance
     */
    public UserSettings loadSettings(String relativeSettingsPath) {
        var path = combinePath(baseDirectory, relativeSettingsPath);
        var settingsFile = new File(path);

        if (settingsFile.exists()) {
            var content = readFileContents(path);
            return JSONConvert.deserializeJson(content, UserSettings.class, JSONConvert.UnforgivingMapper);
        } else {
            return null;
        }

    }

    /**
     * Saves a given UserSettings instance to the database.
     * 
     * @param settings         The settings to save
     * @param relativeSavePath The relative path to save them to
     */
    public void saveSettings(UserSettings settings, String relativeSavePath) {
        String json = JSONConvert.serializeObject(settings);

        if (json != "") {
            writeToFileRelative(relativeSavePath, json);
        }
    }

}
