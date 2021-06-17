package org.visab.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Creates a settings object with the settings from the settings.json file.
 * 
 * @author tim
 *
 */
public class UserSettings {

    private int webApiPort;

    private int defaultTimeout;

    private ArrayList<String> allowedGames;
    
    private Map<String, Integer> sessionTimeouts;

    /**
     * Gets the webApiPort.
     * 
     * @return The port on which the web API is running on.
     */
    public int getWebApiPort() {
        return webApiPort;
    }

    /**
     * Sets the webApiPort.
     * 
     * @param port The port of the webApi.
     */
    public void setWebApiPort(int port) {
        this.webApiPort = port;
    }

    /**
     * Gets the time in seconds until a session is automatically timed out if no
     * statistics were received.
     * 
     * @return The default timeout of the session.
     */
    public int getDefaultSessionTimeout() {
        return defaultTimeout;
    }

    /**
     * Sets the defaultSessionTimeout.
     * 
     * @param timeout The default timeout of the session.
     */
    public void setDefaultSessionTimeout(int timeout) {
        this.defaultTimeout = timeout;
    }

    /**
     * Gets the allowed games.
     * 
     * @return The list of allowed games.
     */
    public ArrayList<String> getAllowedGames() {
        return allowedGames;
    }

    /**
     * Sets the allowedGames.
     * 
     * @param games The games that are allowed.
     */
    public void setAllowedGames(ArrayList<String> games) {
        this.allowedGames = games;
    }
    
    /**
     * Gets the time in seconds until a session is automatically timed out if no statistics were received for each game.
     * 
     * @return The timeout of the sessions.
     */
    public Map<String, Integer> getSessionTimeout() {
        return sessionTimeouts;
    }
    
    /**
     * Sets the sessionTimeouts for each game.
     * 
     * @param timeout The timeout of the sessions.
     */
    public void setSessionTimeouts(Map<String, Integer> timeout) {
        this.sessionTimeouts = timeout;
    }
    
}
