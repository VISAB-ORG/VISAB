package org.visab.newgui.settings;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Helper class to create a session item of the games and the corresponding sessionTimeout.
 * 
 * @author tim
 *
 */
public class SessionItem {
    
    private StringProperty game;
    private StringProperty timeout;
    
    /**
     * The constructor of the class.
     * 
     * @param game The game of the session.
     * @param timeout The timeout of the session.
     */
    public SessionItem(String game, int timeout) {
        this.game = new SimpleStringProperty(game);
        this.timeout = new SimpleStringProperty(String.valueOf(timeout));
    }
    
    /**
     * Gets the game.
     * 
     * @return The game of the session timeout.
     */
    public StringProperty getGame() {
        return this.game;
    }
    
    /**
     * Gets the timeout of the session.
     * 
     * @return The timeout of the session.
     */
    public StringProperty getTimeout() {
        return this.timeout;
    }

}
