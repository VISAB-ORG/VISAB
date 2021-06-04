package org.visab.workspace;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;

import org.visab.util.JsonConvert;
import org.visab.util.UserObject;
import org.visab.workspace.config.model.MappingConfig;

public class ConfigRepository extends RepositoryBase {

    public ConfigRepository(String configPath) {
        super(configPath);
    }

    /**
     * Loads the list of mappings.
     * 
     * @param relativeMappingPath The relative path to the mappings file
     * @return The list of mappings if successful, null else
     */
    public List<MappingConfig> loadMappings(String relativeMappingPath) {
        var path = combinePath(baseDirectory, relativeMappingPath);
        var content = readFileContents(path);

        return JsonConvert.deserializeJson(content, new TypeReference<List<MappingConfig>>() {
        });
    }

    /**
     * Saves the list of mappings.
     * 
     * @param mappings         The list of mappings to save
     * @param relativeSavePath The relative path at which to save the mappings
     * @return True if successfully saved
     */
    public boolean saveMappings(List<MappingConfig> mappings, String relativeSavePath) {
        var json = JsonConvert.serializeObject(mappings);

        if (json != "")
            return writeToFileRelative(relativeSavePath, json);
        else
            return false;
    }
    
    /**
     * Loads the object of settings.
     * 
     * @param relativeSettingsPath The relative path to the settings file.
     * @return The object of settings.
     */
    public UserObject loadSettingsObject(String relativeSettingsPath) {
        String path = combinePath(baseDirectory, relativeSettingsPath);
        String content = readFileContents(path);
        
        return JsonConvert.deserializeJson(content, UserObject.class);
    }
    
    public void saveSettings(UserObject settingsObject, String relativeSavePath) {
        String json = JsonConvert.serializeObject(settingsObject);
        
        if (json != "") {
            writeToFileRelative(relativeSavePath, json);
        }
    }

}
