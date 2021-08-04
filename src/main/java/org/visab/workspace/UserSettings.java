package org.visab.workspace;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents the settings that may be manipulated using VISABs Settings view.
 * This class is serialized as json and written to the settings.json file.
 * 
 * @author tim
 *
 */
public class UserSettings {

    private int webApiPort;

    private ArrayList<String> allowedGames;

    private HashMap<String, Integer> sessionTimeout;

    /**
     * Gets the webApiPort.
     * 
     * @return The port on which the web API is running on
     */
    public int getWebApiPort() {
        return webApiPort;
    }

    /**
     * Sets the webApiPort.
     * 
     * @param port The port of the webApi
     */
    public void setWebApiPort(int port) {
        this.webApiPort = port;
    }

    /**
     * Gets the allowed games.
     * 
     * @return The list of allowed games
     */
    public ArrayList<String> getAllowedGames() {
        return allowedGames;
    }

    /**
     * Sets the allowedGames.
     * 
     * @param games The games that are allowed
     */
    public void setAllowedGames(ArrayList<String> games) {
        this.allowedGames = games;
    }

    /**
     * Gets the time in seconds until a session is automatically timed out if no
     * statistics were received for each game.
     * 
     * @return The timeout of the sessions
     */
    public HashMap<String, Integer> getSessionTimeout() {
        return sessionTimeout;
    }

    /**
     * Sets the sessionTimeouts for each game.
     * 
     * @param timeout The timeout of the sessions
     */
    public void setSessionTimeout(HashMap<String, Integer> timeout) {
        this.sessionTimeout = timeout;
    }

}
