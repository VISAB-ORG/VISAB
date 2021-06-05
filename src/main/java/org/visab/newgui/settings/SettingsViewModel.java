package org.visab.newgui.settings;

import java.util.ArrayList;
import java.util.Arrays;

import org.visab.newgui.ViewModelBase;
import org.visab.newgui.settings.model.Settings;

/**
 * This class represents the viewmodel of the settings.
 * 
 * @author tim
 *
 */
public class SettingsViewModel extends ViewModelBase {
    
    private Settings settings = new Settings();
    
    /**
     * Converts the webApiPort to a String for the view.
     * @return The webApiPort as String.
     */
    public String webApiPortProperty() {
        return String.valueOf(settings.getWebApiPort());
    }
    
    /**
     * The webApiHostName for the view.
     * @return The webApiHostName as String.
     */
    public String webApiHostNameProperty() {
        return settings.getWebApiHostName();
    }
    
    /**
     * Converts the sessionTimeout to a String for the view.
     * @return The sessionTimeout as String.
     */
    public String sessionTimeoutProperty() {
        return String.valueOf(settings.getSessionTimeout());
    }
    
    /**
     * Converts the allowedGames to a String for the view.
     * @return The allowedGames as String.
     */
    public String allowedGamesProperty() {
        return settings.getAllowedGames().toString().replace("[", "").replace("]", "").replace(" ", "");
    }
    
    /**
     * Updates the settings values with the new values and saves them.
     * @param port The used port.
     * @param hostName The used hostName.
     * @param timeout The sessionTimeout time.
     * @param games The games that are allowed.
     */
    public void updateSettings(String port, String hostName, String timeout, String games) {
        // converting the games String back to an ArrayList
        ArrayList<String> gamesArrray = new ArrayList<String>(Arrays.asList(games.split(",")));
        
        // updating and saving the settings
        settings.updateWebApiPort(Integer.parseInt(port));
        settings.updateWebApiHostName(hostName);
        settings.updateSessionTimeout(Integer.parseInt(timeout));
        settings.updateAllowedGames(gamesArrray); 
        settings.saveSettings();
    }
}
