package org.visab.newgui.settings;

import java.util.ArrayList;
import java.util.Arrays;

import org.visab.newgui.ViewModelBase;

import de.saxsys.mvvmfx.utils.commands.Command;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * This class represents the viewmodel of the settings.
 * 
 * @author tim
 *
 */
public class SettingsViewModel extends ViewModelBase {
    
    private SettingsModel settings = new SettingsModel();
    
    private StringProperty webApiPort = new SimpleStringProperty(String.valueOf(settings.getWebApiPort()));
    
    private StringProperty webApiHostName = new SimpleStringProperty(settings.getWebApiHostName());
    
    private StringProperty sessionTimeout = new SimpleStringProperty(String.valueOf(settings.getSessionTimeout()));
    
    private StringProperty allowedGames = new SimpleStringProperty(settings.getAllowedGames().toString().
            replace("[", "").replace("]", "").replace(" ", ""));
    
    /**
     * The webApiPort for the view.
     * @return The webApiPort as StringProperty.
     */
    public StringProperty webApiPortProperty() {
        return webApiPort;
    }
    
    /**
     * The webApiHostName for the view.
     * @return The webApiHostName as StringProperty.
     */
    public StringProperty webApiHostNameProperty() {
        return webApiHostName;
    }
    
    /**
     * The sessionTimeout for view.
     * @return The sessionTimeout as StringProperty.
     */
    public StringProperty sessionTimeoutProperty() {
        return sessionTimeout;
    }
    
    /**
     * The allowedGames for the view.
     * @return The allowedGames as String.
     */
    public StringProperty allowedGamesProperty() {
        return allowedGames;
    }
    
    /**
     * Updates the settings values with the new values and saves them.
     * @param port The used port.
     * @param hostName The used hostName.
     * @param timeout The sessionTimeout time.
     * @param games The games that are allowed.
     */
    public Command updateSettingsCommand() {
        // converting the games String back to an ArrayList
        ArrayList<String> gamesArrray = new ArrayList<String>(Arrays.asList(allowedGames.get().split(",")));
        
        // updating and saving the settings
        settings.updateWebApiPort(Integer.parseInt(webApiPort.get()));
        settings.updateWebApiHostName(webApiHostName.get());
        settings.updateSessionTimeout(Integer.parseInt(sessionTimeout.get()));
        settings.updateAllowedGames(gamesArrray); 
        
        return runnableCommand(() -> {
            settings.saveSettings();
        });
    }
}
