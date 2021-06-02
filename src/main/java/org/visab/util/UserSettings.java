package org.visab.util;

import java.util.ArrayList;

import org.visab.workspace.config.ConfigManager;

/**
 * POJO for holding all settings in the project that can be configured by the user. 
 * 
 * @author tim
 *
 */
public class UserSettings {

    /**
     * Gets an object of the settings set in the settings.json file.
     * @return An object of the settings.
     */
    private static SettingsObject getSettingsObject() {
        ConfigManager cm = new ConfigManager();
        return cm.loadSettings();
    }
    
    /**
     * Gets the webApiPort.
     * @return The port on which the API is running on.
     */
    public static int getApiPort() {
        return getSettingsObject().getWebApiPort();
    }
    
    /**
     * Gets the WebApiHostName.
     * @return The base address of the web api.
     */
    public static String getWebApiHostName() {
        return getSettingsObject().getWebApiHostName();
    } 
    
    /**
     * Gets the time in seconds until a session is automatically timed out
     * if no statistics were received.
     * @return The timeout of the session.
     */
    public static int getSessionTimeout() {
        return getSettingsObject().getSessionTimeout();
    }
    
    /**
     * Gets the allowed games.
     * @return The list of allowed games.
     */
    public static ArrayList<String> getAllowedGames() {
        return getSettingsObject().getAllowedGames();
    }
    
    public void setWebbApiPort(int port) {
        getSettingsObject().setWebApiPort(port);
    }

}
