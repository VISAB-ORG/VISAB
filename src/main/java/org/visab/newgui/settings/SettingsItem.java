package org.visab.newgui.settings;

public class SettingsItem {
    
    private String game;
    private int timeout;
    
    public SettingsItem(String game, int timeout) {
        this.game = game;
        this.timeout = timeout;
    }
    
    public String getGame() {
        return this.game;
    }
    
    public int getTimeout() {
        return this.timeout;
    }

}
