package org.visab.newgui.settings.viewmodel;

import java.util.ArrayList;
import java.util.HashMap;

import org.visab.newgui.ViewModelBase;
import org.visab.workspace.Workspace;

import de.saxsys.mvvmfx.utils.commands.Command;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This class represents the viewmodel of the sessionTimeoutEditView.
 * 
 * @author tim
 *
 */
public class SessionTimeoutEditViewModel extends ViewModelBase{
    
    private StringProperty timeout = new SimpleStringProperty();
    
    private ObservableList<String> allowedGames = FXCollections.observableArrayList();
    
    private String game;
    
    /**
     * Sets the game with the selected one in the choicebox of the view.
     * 
     * @param game The selected game.
     * @return The game that is selected.
     */
    public String selectedGame(String game) {
        return this.game = game;
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
     * The allowedGame for the view.
     * 
     * @return List of the allowed game.
     */
    public ObservableList<String> allowedGamesProperty() {
        ArrayList<String> addGames  = Workspace.getInstance().getConfigManager().getAllowedGames();
        for (String game: addGames) {
            allowedGames.add(game);
        }
        return allowedGames;
    }
    
    /**
     * Updates the sessionTimeout setting with the new value and saves it.
     * 
     * @return Saves the settings per runnableCommand.
     */
    public Command updateSessionTimeoutCommand() {
        HashMap<String, Integer> sessionTimeout = Workspace.getInstance().getConfigManager().getSessionTimeout();
        sessionTimeout.replace(game, Integer.parseInt(timeout.get()));
        Workspace.getInstance().getConfigManager().updateSessionTimeout(sessionTimeout);
       
        return runnableCommand(() -> {
            Workspace.getInstance().getConfigManager().saveSettings();
        });
    }
}
