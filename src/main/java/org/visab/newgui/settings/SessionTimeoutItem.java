package org.visab.newgui.settings;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Helper class to create a session item of the games and the corresponding
 * sessionTimeout.
 * 
 * @author tim
 *
 */
public class SessionTimeoutItem {

    private String game;
    private IntegerProperty timeoutProperty;

    /**
     * The constructor of the class.
     * 
     * @param game    The game of the session.
     * @param timeout The timeout of the session.
     */
    public SessionTimeoutItem(String game, int timeout) {
        this.game = game;
        this.timeoutProperty = new SimpleIntegerProperty(timeout);
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
    public int getTimeout() {
        return this.timeoutProperty.get();
    }

    public IntegerProperty timeoutProperty() {
        return this.timeoutProperty;
    }

    public void setTimeout(int timeout) {
        this.timeoutProperty.set(timeout);
    }
}
