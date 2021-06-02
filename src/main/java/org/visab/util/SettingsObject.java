package org.visab.util;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonGetter;

/**
 * Creates a settings object with the settings from the settings.json file.
 * 
 * @author tim
 *
 */
public class SettingsObject {
    
    private int webApiPort;
    
    private String webApiHostName;

    private int sessionTimeout;
    
    private ArrayList<String> allowedGames;
    
    /**
     * Gets the webApiPort.
     * @return The port on which the web API is running on.
     */
    @JsonGetter
    public int getWebApiPort() {
        return webApiPort;
    }
    
    /**
     * Sets the webApiPort.
     * @param port The port of the webApi.
     */
    public void setWebApiPort(int port) {
        this.webApiPort = port;
    }
    
    /**
     * Gets the WebApiHostName.
     * @return The base address of the web api.
     */
    @JsonGetter
    public String getWebApiHostName() {
        return webApiHostName + webApiPort;
    }
    
    /**
     * Sets the webApiHostName
     * @param hostName The hostName from the webApi. 
     */
    public void setWebApiHostName(String hostName) {
        this.webApiHostName = hostName;
    }
    
    /**
     * Gets the time in seconds until a session is automatically timed out
     * if no statistics were received.
     * @return The timeout of the session.
     */
    @JsonGetter
    public int getSessionTimeout() {
        return sessionTimeout;
    }
    
    /**
     * Sets the sessionTimeout.
     * @param timeout The timeout of the session.
     */
    public void setSessionTimeout(int timeout) {
        this.sessionTimeout = timeout;
    }
    /**
     * Gets the allowed games.
     * @return The list of allowed games.
     */
    @JsonGetter
    public ArrayList<String> getAllowedGames() {
        return allowedGames;
    }
    
    /**
     * Sets the allowedGames.
     * @param games The games that are allowed.
     */
    public void setAllowedGames(ArrayList<String> games) {
        this.allowedGames = games;
    }
}
