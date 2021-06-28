package org.visab.newgui.settings.viewmodel;

import java.util.ArrayList;
import java.util.HashMap;

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
    
    private StringProperty timeout = new SimpleStringProperty();
    
    private StringProperty game = new SimpleStringProperty();
    
    private StringProperty newGame = new SimpleStringProperty();
    
    private StringProperty removedGame = new SimpleStringProperty();
    
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
     * Sets the game with the selected one in the choice box of the view.
     * 
     * @return The game that is selected.
     */
    public StringProperty selectedGame() {
        return game;
    }
    
    /**
     * The timeout for the session.
     * 
     * @param time The timeout time.
     * @return The timeout for the session.
     */
    public StringProperty timeoutProperty(StringProperty time) {
        return this.timeout = time;
    }
    
    /**
     * The name of the new game.
     * 
     * @return The new game.
     */
    public StringProperty newGameProperty() {
        return newGame;
    }
    
    /**
     * The removed game from the view choice box.
     * 
     * @return The removed game.
     */
    public StringProperty removedGame() {
        return this.removedGame;
    }
    
    /**
     * Adds the new game to the settings.
     * 
     * @return Saves the updated settings.
     */
    public Command addAllowedGameCommand() {
      ArrayList<String> games = new ArrayList<>(Workspace.getInstance().getConfigManager().getAllowedGames());
      games.add(newGame.get());
      
      Workspace.getInstance().getConfigManager().updateAllowedGames(games);

      return runnableCommand(() -> {          
          Workspace.getInstance().getConfigManager().saveSettings();
      });
    }
    
    /**
     * Removes the chosen game from the settings
     * 
     * @return Saves the updated settings.
     */
    public Command removeAllowedGameCommand() {
        ArrayList<String> games = new ArrayList<>(Workspace.getInstance().getConfigManager().getAllowedGames());
        
        for (int i = 0; i < games.size(); i++) {
            if (games.get(i).contains(removedGame.get())) {
                games.remove(i);
            }
        }
        
        Workspace.getInstance().getConfigManager().updateAllowedGames(games);
        
        return runnableCommand(() -> {
            Workspace.getInstance().getConfigManager().saveSettings();
        });
    }
    
    /**
     * Updates the sessionTimeout setting with the new value and saves it.
     * 
     * @return Saves the updated settings.
     */
    public Command updateSessionTimeoutCommand() {
        HashMap<String, Integer> sessionTimeout = Workspace.getInstance().getConfigManager().getSessionTimeout();
        sessionTimeout.replace(game.get(), Integer.parseInt(timeout.get()));
        Workspace.getInstance().getConfigManager().updateSessionTimeout(sessionTimeout);
       
        return runnableCommand(() -> {
            Workspace.getInstance().getConfigManager().saveSettings();
        });
    }
    
    /**
     * Command to open the sessionTimeoutEditView.
     * 
     * @return Opens the sessionTimeoutEditView.
     */
    public Command openSessionTimeoutEditViewCommand() {
        if (openSessionTimeoutEditViewCommand == null) {
            openSessionTimeoutEditViewCommand = runnableCommand(() -> {
                dialogHelper.showView(SessionTimeoutEditView.class, "Session Tiemout", true);
            });
        }
        return openSessionTimeoutEditViewCommand;
    }
    
    /**
     * Command to open the allowedGameEditView.
     * 
     * @return Opens the allowedGameEditView.
     */
    public Command openAllowedGameEditViewCommand() {
        if (openAllowedGameEditViewCommand == null) {
            openAllowedGameEditViewCommand = runnableCommand(() -> {
                dialogHelper.showView(AllowedGamesEditView.class, "Allowed Games", true);
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
