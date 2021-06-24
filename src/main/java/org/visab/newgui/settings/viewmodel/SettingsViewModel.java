package org.visab.newgui.settings.viewmodel;

import java.util.ArrayList;

import org.visab.newgui.DynamicViewLoader;
import org.visab.newgui.ViewModelBase;
import org.visab.newgui.settings.SessionItem;
import org.visab.newgui.settings.view.AllowedGamesEditView;
import org.visab.newgui.settings.view.SessionTimeoutEditView;
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
    
    private ObservableList<SessionItem> settingsItem = FXCollections.observableArrayList();
    
    private ObservableList<String> allowedGames = FXCollections.observableArrayList(Workspace.getInstance().
            getConfigManager().getAllowedGames());
    
    private Command openSessionTimeoutEditViewCommand;
    
    private Command openAllowedGameEditViewCommand;
    
    /**
     * The webApiPort for the view.
     * 
     * @return The webApiPort as StringProperty.
     */
    public StringProperty webApiPortProperty() {
        return webApiPort;
    }
    
    /**
     * The session timeout for very game.
     * 
     * @return List of SettingsItemts.
     */
    public ObservableList<SessionItem> settingsItemProperty() {
        //TODO: find a better solution needs to be reloaded when sessionTimeout edit is done
        var items = new ArrayList<SessionItem>();
        for (int i = 0; i < Workspace.getInstance().getConfigManager().getAllowedGames().size(); i++) {
            items.add(new SessionItem(Workspace.getInstance().getConfigManager().getAllowedGames().get(i), 
                    Workspace.getInstance().getConfigManager().getSessionTimeout().get(Workspace.getInstance().
                            getConfigManager().getAllowedGames().get(i))));
        }
        settingsItem.addAll(items);

        return settingsItem;
    }
    
    /**
     * The allowed Games.
     * 
     * @return ObservableList of allowed Games.
     */
    public ObservableList<String> allowedGamesProperty() {
        return allowedGames;
    }
    
    /**
     * Command to open the sessionTimeoutEditView.
     * 
     * @return Opens the sessionTimeoutEditView.
     */
    public Command openSessionTimeoutEditViewCommand() {
        if (openSessionTimeoutEditViewCommand == null) {
            openSessionTimeoutEditViewCommand = runnableCommand(() -> {
                DynamicViewLoader.showView(SessionTimeoutEditView.class, "Session Tiemout");
            });
        }
        return openSessionTimeoutEditViewCommand;
    }
    
    /**
     * Command to open the allowedGameEditView.
     * 
     * @return Opens the allowedGameEditView.
     */
    public Command openAllowedGameEditViewComman() {
        if (openAllowedGameEditViewCommand == null) {
            openAllowedGameEditViewCommand = runnableCommand(() -> {
                DynamicViewLoader.showView(AllowedGamesEditView.class, "Allowed Games");
            });
        }
        return  openAllowedGameEditViewCommand;
    }
    
    /**
     * Updates the settings values with the new values and saves them.
     * 
     * @return Saves the settings per runnableCommand.
     */
    public Command updateSettingsCommand() {        
        // updating and saving the settings
        Workspace.getInstance().getConfigManager().updateWebApiPort(Integer.parseInt(webApiPort.get()));

        return runnableCommand(() -> {
            Workspace.getInstance().getConfigManager().saveSettings();
        });
    }
}
