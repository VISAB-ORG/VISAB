package org.visab.newgui.settings.model;

import java.util.ArrayList;

import org.visab.util.UserObject;
import org.visab.workspace.Workspace;

/**
 * The class represents the model of the settings.
 * 
 * @author tim
 *
 */
public class Settings {
    
    private int webApiPort;
    
    private String webApiHostName;
    
    private int sessionTimeout;
    
    private ArrayList<String> allowedGames = new ArrayList<>();
    
    private UserObject settings = Workspace.getInstance().getConfigManager().getSettings();
    
    /**
     * Constructor of the class. Initializes the settings.
     */
    public Settings() {
        this.webApiPort = settings.getWebApiPort();
        this.webApiHostName = settings.getWebApiHostName();
        this.sessionTimeout = settings.getSessionTimeout();
        this.allowedGames = settings.getAllowedGames();
    }
    
    /**
     * Getter for the webApiPort.
     * @return The webApiPort.
     */
    public int getWebApiPort() {
        return webApiPort;
    }
    
    /**
     * Getter for the webApiHostName.
     * @return The webApiHostName.
     */
    public String getWebApiHostName() {
        return webApiHostName;
    }
    
    /**
     * Getter for the sessionTimeout.
     * @return The sessionTimeout.
     */
    public int getSessionTimeout() {
        return sessionTimeout;
    }
    
    /**
     * Getter for the allowedGames.
     * @return The allowedGames.
     */
    public ArrayList<String> getAllowedGames() {
        return allowedGames;
    }
    
    /**
     * Updates the webApiPort.
     * @param port The new port.
     */
    public void updateWebApiPort(int port) {
        this.webApiPort = port;
    }
    
    /**
     * Updates the webApiHostName.
     * @param hostName The new webApiHostName.
     */
    public void updateWebApiHostName(String hostName) {
        this.webApiHostName = hostName;
    }
    
    /**
     * Updates the sessionTimeout time.
     * @param timeout The new sessionTimeout.
     */
    public void updateSessionTimeout(int timeout) {
        this.sessionTimeout = timeout;
    }
    
    /**
     * Updates the list auf allowed games.
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
        settings.setWebApiHostName(webApiHostName);
        settings.setSessionTimeout(sessionTimeout);
        settings.setAllowedGames(allowedGames);
        Workspace.getInstance().getConfigManager().saveSettings(settings);
        }

}
