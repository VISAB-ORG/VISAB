package org.visab.newgui.settings;

import java.util.ArrayList;
import java.util.Arrays;

import org.visab.newgui.ViewModelBase;
import org.visab.workspace.Workspace;


import de.saxsys.mvvmfx.utils.commands.Command;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

/**
 * This class represents the viewmodel of the settings.
 * 
 * @author tim
 *
 */
public class SettingsViewModel extends ViewModelBase {

    private StringProperty webApiPort = new SimpleStringProperty(
            String.valueOf(Workspace.getInstance().getConfigManager().getWebApiPort()));

    private StringProperty sessionTimeout = new SimpleStringProperty(
            String.valueOf(Workspace.getInstance().getConfigManager().getDefaultSessionTimeout()));

    private StringProperty allowedGames = new SimpleStringProperty(Workspace.getInstance().getConfigManager()
            .getAllowedGames().toString().replace("[", "").replace("]", "").replace(" ", ""));
    
//    private MapProperty sessionTimeouts = new SimpleMapProperty<String, Integer>();
    private ObservableMap<String, Integer> sessionTimeouts = FXCollections.observableMap(Workspace.getInstance().getConfigManager().getSessionTimeouts());
    
    private ObservableList<SettingsItem> settingsItem = FXCollections.observableArrayList();

    /**
     * The webApiPort for the view.
     * 
     * @return The webApiPort as StringProperty.
     */
    public StringProperty webApiPortProperty() {
        return webApiPort;
    }

    /**
     * The sessionTimeout for view.
     * 
     * @return The sessionTimeout as StringProperty.
     */
    public StringProperty sessionTimeoutProperty() {
        return sessionTimeout;
    }

    /**
     * The allowedGames for the view.
     * 
     * @return The allowedGames as String.
     */
    public StringProperty allowedGamesProperty() {
        return allowedGames;
    }
    
    public ObservableMap<String, Integer> sessionTimeoutsProperty() {
        return sessionTimeouts;
    }
    
    public ObservableList<SettingsItem> settingsItemProperty() {
        var items = new ArrayList<SettingsItem>();
        for (int i = 0; i < Workspace.getInstance().getConfigManager().getAllowedGames().size(); i++) {
            items.add(new SettingsItem(Workspace.getInstance().getConfigManager().getAllowedGames().get(i), 
                    Workspace.getInstance().getConfigManager().getSessionTimeouts().get(Workspace.getInstance().getConfigManager().getAllowedGames().get(i))));
        }
        settingsItem.addAll(items);
        System.out.println(settingsItem.get(0).getGame());
        System.out.println(settingsItem.get(0).getTimeout());
        return settingsItem;
    }

    /**
     * Updates the settings values with the new values and saves them.
     */
    public Command updateSettingsCommand() {
        // converting the games String back to an ArrayList
        ArrayList<String> gamesArrray = new ArrayList<String>(Arrays.asList(allowedGames.get().split(",")));

        // updating and saving the settings
        Workspace.getInstance().getConfigManager().updateWebApiPort(Integer.parseInt(webApiPort.get()));
        Workspace.getInstance().getConfigManager().updateSessionTimeout(Integer.parseInt(sessionTimeout.get()));
        Workspace.getInstance().getConfigManager().updateAllowedGames(gamesArrray);

        return runnableCommand(() -> {
            Workspace.getInstance().getConfigManager().saveSettings();
        });
    }
}
