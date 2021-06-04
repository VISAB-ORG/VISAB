package org.visab.newgui.settings.model;

import java.util.ArrayList;

import org.visab.util.UserObject;
import org.visab.workspace.Workspace;

public class Settings {
    
    private int webApiPort;
    
    private String webApiHostName;
    
    private int sessionTimeout;
    
    private ArrayList<String> allowedGames = new ArrayList<>();
    
    private UserObject settings = Workspace.getInstance().getConfigManager().getSettings();
    
    public Settings() {
        this.webApiPort = settings.getWebApiPort();
        this.webApiHostName = settings.getWebApiHostName();
        this.sessionTimeout = settings.getSessionTimeout();
        this.allowedGames = settings.getAllowedGames();
    }
    
    public int getWebApiPort() {
        return webApiPort;
    }
    
    public String getWebApiHostName() {
        return webApiHostName;
    }
    
    public int getSessionTimeout() {
        return sessionTimeout;
    }
    
    public ArrayList<String> getAllowedGames() {
        return allowedGames;
    }
    
    public void updateWebApiPort(int port) {
        this.webApiPort = port;
    }
    
    public void updateWebApiHostName(String hostName) {
        this.webApiHostName = hostName;
    }
    
    public void updateSessionTimeout(int timeout) {
        this.sessionTimeout = timeout;
    }
    
    public void updateAllowedGames(ArrayList<String> games) {
        this.allowedGames = games;
    }
    
    public void saveSettings() {
        settings.setWebApiPort(webApiPort);
        settings.setWebApiHostName(webApiHostName);
        settings.setSessionTimeout(sessionTimeout);
        settings.setAllowedGames(allowedGames);
        Workspace.getInstance().getConfigManager().saveSettings(settings);
        }

}
