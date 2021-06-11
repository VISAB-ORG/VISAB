package org.visab.util;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Creates a settings object with the settings from the settings.json file.
 * 
 * @author tim
 *
 */
public class UserSettings {

    private static Logger logger = LogManager.getLogger(UserSettings.class);

    private int webApiPort;

    private String webApiHostName;

    private int sessionTimeout;

    private ArrayList<String> allowedGames;

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
        if (this.webApiPort != 0) {
            if (port != this.webApiPort) {
                logger.info("Changed webApiPort from " + this.webApiPort + " to " + port + ".");
            }
        }
        this.webApiPort = port;
    }

    /**
     * Gets the WebApiHostName.
     * 
     * @return The base address of the web api.
     */
    public String getWebApiHostName() {
        return webApiHostName;
    }

    /**
     * Sets the webApiHostName
     * 
     * @param hostName The hostName from the webApi.
     */
    public void setWebApiHostName(String hostName) {
        if (this.getWebApiHostName() != null) {
            if (!hostName.equals(this.getWebApiHostName())) {
                logger.info("Changed webApiHostName from " + this.webApiHostName + " to " + hostName + ".");
            }
        }
        this.webApiHostName = hostName;
    }

    /**
     * Gets the time in seconds until a session is automatically timed out if no
     * statistics were received.
     * 
     * @return The timeout of the session.
     */
    public int getSessionTimeout() {
        return sessionTimeout;
    }

    /**
     * Sets the sessionTimeout.
     * 
     * @param timeout The timeout of the session.
     */
    public void setSessionTimeout(int timeout) {
        if (this.sessionTimeout != 0) {
            if (timeout == 0) {
                logger.error("Value 0 is not allowed for sessionTimeout, please provide a value > 0.");
                timeout = this.sessionTimeout;
            } else if (timeout > 0 && timeout != this.sessionTimeout) {
                logger.info("Changed sessionTimeout from " + this.sessionTimeout + "s to " + timeout + "s.");
            }
        }
        this.sessionTimeout = timeout;
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
        if (this.allowedGames != null) {

            // Check if current version of allowed games contains a new game
            for (String game : games) {
                if (!this.allowedGames.contains(game)) {
                    logger.info("Added new game to allowedGame list: " + game + ".");
                }
            }
            // Check if current version of allowed games is missing a game which was
            // previously allowed
            for (String game : this.allowedGames) {
                if (!games.contains(game)) {
                    logger.info("Removed game: " + game + " from allowedGame list.");
                }
            }
            // Renaming a game will result in both logs to be printed
        }

        this.allowedGames = games;
    }
}
