package org.visab.newgui.settings;

/**
 * Helper class to create a session item of the games and the corresponding sessionTimeout.
 * 
 * @author tim
 *
 */
public class SessionItem {
    
    private String game;
    private String timeout;
    
    /**
     * The constructor of the class.
     * 
     * @param game The game of the session.
     * @param timeout The timeout of the session.
     */
    public SessionItem(String game, int timeout) {
        this.game = game;
        this.timeout = String.valueOf(timeout) + " s";
    }
    
    /**
     * Gets the game.
     * 
     * @return The game of the session timeout.
     */
    public String getGame() {
        return this.game;
    }
    
    /**
     * Gets the timeout of the session.
     * 
     * @return The timeout of the session.
     */
    public String getTimeout() {
        return this.timeout;
    }

}
