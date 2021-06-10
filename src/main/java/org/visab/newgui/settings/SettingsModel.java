package org.visab.newgui.settings;

import java.util.ArrayList;

import org.visab.util.UserSettings;
import org.visab.workspace.Workspace;

/**
 * The class represents the model of the settings.
 * 
 * @author tim
 *
 */
public class SettingsModel {

    private int webApiPort;

    private int sessionTimeout;

    private ArrayList<String> allowedGames = new ArrayList<>();

    private UserSettings settings = Workspace.getInstance().getConfigManager().getSettings();

    /**
     * Constructor of the class. Initializes the settings.
     */
    public SettingsModel() {
        this.webApiPort = settings.getWebApiPort();
        this.sessionTimeout = settings.getSessionTimeout();
        this.allowedGames = settings.getAllowedGames();
    }

    /**
     * Getter for the webApiPort.
     * 
     * @return The webApiPort.
     */
    public int getWebApiPort() {
        return webApiPort;
    }

    /**
     * Getter for the sessionTimeout.
     * 
     * @return The sessionTimeout.
     */
    public int getSessionTimeout() {
        return sessionTimeout;
    }

    /**
     * Getter for the allowedGames.
     * 
     * @return The allowedGames.
     */
    public ArrayList<String> getAllowedGames() {
        return allowedGames;
    }

    /**
     * Updates the webApiPort.
     * 
     * @param port The new port.
     */
    public void updateWebApiPort(int port) {
        this.webApiPort = port;
    }

    /**
     * Updates the sessionTimeout time.
     * 
     * @param timeout The new sessionTimeout.
     */
    public void updateSessionTimeout(int timeout) {
        this.sessionTimeout = timeout;
    }

    /**
     * Updates the list auf allowed games.
     * 
     * @param games The new allowed games.
     */
    public void updateAllowedGames(ArrayList<String> games) {
        this.allowedGames = games;
    }

    /**
     * Sets the new values of the settings and saves them to the workspace.
     */
    public void saveSettings() {
        settings.setWebApiPort(webApiPort);
        settings.setSessionTimeout(sessionTimeout);
        settings.setAllowedGames(allowedGames);
        Workspace.getInstance().getConfigManager().saveSettings(settings);
    }

}
