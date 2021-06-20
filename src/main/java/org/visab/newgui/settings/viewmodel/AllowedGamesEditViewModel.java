package org.visab.newgui.settings.viewmodel;

import java.util.ArrayList;

import org.visab.newgui.ViewModelBase;
import org.visab.workspace.Workspace;

import de.saxsys.mvvmfx.utils.commands.Command;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This class represents the viewmodel of the allowedGamesEdit.
 * 
 * @author tim
 *
 */
public class AllowedGamesEditViewModel extends ViewModelBase {
    
    private StringProperty newGame = new SimpleStringProperty();
    
    private ObservableList<String> allowedGames = FXCollections.observableArrayList();
    
    private StringProperty removedGame = new SimpleStringProperty();
    
    /**
     * The name of the new game.
     * 
     * @return The new game.
     */
    public StringProperty newGameProperty() {
        return newGame;
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
}
