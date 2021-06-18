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

/**
 * This class represents the viewmodel of the settings.
 * 
 * @author tim
 *
 */
public class SettingsViewModel extends ViewModelBase {

    private StringProperty webApiPort = new SimpleStringProperty(
            String.valueOf(Workspace.getInstance().getConfigManager().getWebApiPort()));

    private StringProperty allowedGames = new SimpleStringProperty(Workspace.getInstance().getConfigManager()
            .getAllowedGames().toString().replace("[", "").replace("]", "").replace(" ", ""));
    
    private ObservableList<SessionItem> settingsItem = FXCollections.observableArrayList();

    /**
     * The webApiPort for the view.
     * 
     * @return The webApiPort as StringProperty.
     */
    public StringProperty webApiPortProperty() {
        return webApiPort;
    }

    /**
     * The allowedGames for the view.
     * 
     * @return The allowedGames as String.
     */
    public StringProperty allowedGamesProperty() {
        return allowedGames;
    }
    
    /**
     * The session timeout for very game.
     * 
     * @return List of SettingsItemts.
     */
    public ObservableList<SessionItem> settingsItemProperty() {
        //TODO: find a better solution
        var items = new ArrayList<SessionItem>();
        for (int i = 0; i < Workspace.getInstance().getConfigManager().getAllowedGames().size(); i++) {
            items.add(new SessionItem(Workspace.getInstance().getConfigManager().getAllowedGames().get(i), 
                    Workspace.getInstance().getConfigManager().getSessionTimeout().get(Workspace.getInstance().getConfigManager().getAllowedGames().get(i))));
        }
        settingsItem.addAll(items);

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
        Workspace.getInstance().getConfigManager().updateAllowedGames(gamesArrray);
        
//        HashMap<String, Integer> map = Workspace.getInstance().getConfigManager().getSessionTimeout();
//        Workspace.getInstance().getConfigManager().updateSessionTimeout(map);

        return runnableCommand(() -> {
            Workspace.getInstance().getConfigManager().saveSettings();
        });
    }
}
