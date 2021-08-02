package org.visab.workspace;

import java.io.File;


import org.visab.util.JSONConvert;

public class ConfigRepository extends RepositoryBase {

    public ConfigRepository(String configPath) {
        super(configPath);
    }

    /**
     * Loads the object of settings.
     * 
     * @param relativeSettingsPath The relative path to the settings file.
     * @return The object of settings.
     */
    public UserSettings loadSettingsObject(String relativeSettingsPath) {
        var path = combinePath(baseDirectory, relativeSettingsPath);
        var settingsFile = new File(path);

        if (settingsFile.exists()) {
            var content = readFileContents(path);
            return JSONConvert.deserializeJson(content, UserSettings.class, JSONConvert.UnforgivingMapper);
        } else {
            return null;
        }

    }

    public void saveSettings(UserSettings settingsObject, String relativeSavePath) {
        String json = JSONConvert.serializeObject(settingsObject);

        if (json != "") {
            writeToFileRelative(relativeSavePath, json);
        }
    }

}
