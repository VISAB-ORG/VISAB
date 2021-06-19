package org.visab.newgui.settings.viewmodel;

import java.util.ArrayList;

import org.visab.newgui.ViewModelBase;
import org.visab.workspace.Workspace;

import de.saxsys.mvvmfx.utils.commands.Command;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SessionTimeoutEditViewModel extends ViewModelBase{
    
    private ObservableList<String> allowedGames = FXCollections.observableArrayList();
    
    public ObservableList<String> allowedGamesProperty() {
        ArrayList<String> addGames  = Workspace.getInstance().getConfigManager().getAllowedGames();
        for (String game: addGames) {
            allowedGames.add(game);
        }
        return allowedGames;
    }
    
    public Command updateSessionTimeoutCommand() {
        
        
        return null;
    }
}
