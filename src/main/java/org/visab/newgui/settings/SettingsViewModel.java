package org.visab.newgui.settings;

import org.visab.newgui.ViewModelBase;
import org.visab.newgui.settings.model.Settings;

public class SettingsViewModel extends ViewModelBase {
    
    private Settings settings = new Settings();
    
    public String webApiPortProperty() {
        return String.valueOf(settings.getWebApiPort());
    }
    
    public String webApiHostNameProperty() {
        return settings.getWebApiHostName();
    }
    
    public String sessionTimeoutProperty() {
        return String.valueOf(settings.getSessionTimeout());
    }
    
    public String allowedGamesProperty() {
        return settings.getAllowedGames().toString();
    }
    
    public void updateSettings(String port, String hostName, String timeout, String games) {
        settings.updateWebApiPort(Integer.parseInt(port));
        settings.updateWebApiHostName(hostName);
        settings.updateSessionTimeout(Integer.parseInt(timeout));
        settings.getAllowedGames().add(games);
        settings.updateAllowedGames(settings.getAllowedGames());
        settings.saveSettings();
    }
}
