package org.visab.workspace;

import java.io.File;


import org.visab.util.JsonConvert;
import org.visab.util.UserSettings;

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
            return JsonConvert.deserializeJson(content, UserSettings.class);
        } else {
            return null;
        }

    }

    public void saveSettings(UserSettings settingsObject, String relativeSavePath) {
        String json = JsonConvert.serializeObject(settingsObject);

        if (json != "") {
            writeToFileRelative(relativeSavePath, json);
        }
    }

}
