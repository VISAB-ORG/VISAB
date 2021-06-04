package org.visab.util;

import java.util.ArrayList;

import org.visab.workspace.Workspace;

/**
 * POJO for holding all settings in the project that can be configured by the user. 
 * 
 * @author tim
 *
 */
public class UserSettings {

    /**
     * Gets the webApiPort.
     * @return The port on which the API is running on.
     */
    public static int getApiPort() {
        return Workspace.getInstance().getSettings().getWebApiPort();
    }
    
    /**
     * Gets the WebApiHostName.
     * @return The base address of the web api.
     */
    public static String getWebApiHostName() {
        return Workspace.getInstance().getSettings().getWebApiHostName();
    } 
    
    /**
     * Gets the time in seconds until a session is automatically timed out
     * if no statistics were received.
     * @return The timeout of the session.
     */
    public static int getSessionTimeout() {
        return Workspace.getInstance().getSettings().getSessionTimeout();
    }
    
    /**
     * Gets the allowed games.
     * @return The list of allowed games.
     */
    public static ArrayList<String> getAllowedGames() {
        return Workspace.getInstance().getSettings().getAllowedGames();
    }

}
