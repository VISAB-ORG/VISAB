package org.visab.gui.settings.viewmodel;

import java.util.ArrayList;
import java.util.HashMap;

import org.visab.gui.ShowViewConfiguration;
import org.visab.gui.ViewModelBase;
import org.visab.gui.settings.SessionTimeoutItem;
import org.visab.gui.settings.view.AllowedGamesEditView;
import org.visab.gui.settings.view.SessionTimeoutEditView;
import org.visab.util.StreamUtil;
import org.visab.workspace.ConfigManager;
import org.visab.workspace.Workspace;

import de.saxsys.mvvmfx.utils.commands.Command;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
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

    private static ConfigManager configManager = Workspace.getInstance().getConfigManager();

    /**
     * Called by javafx/mvvmfx after view is loaded
     */
    public void initialize() {
        webApiPort.set(configManager.getWebApiPort());
        allowedGames.addAll(configManager.getAllowedGames());

        // Create the session item instances
        for (var entry : configManager.getSessionTimeout().entrySet()) {
            var sessionItem = new SessionTimeoutItem(entry.getKey(), entry.getValue());
            gameSessionTimeouts.add(sessionItem);
        }
    }

    private IntegerProperty webApiPort = new SimpleIntegerProperty();

    private ObservableList<SessionTimeoutItem> gameSessionTimeouts = FXCollections.observableArrayList();

    private ObservableList<String> allowedGames = FXCollections.observableArrayList();

    private ObjectProperty<String> selectedAllowedGame = new SimpleObjectProperty<>();

    private ObjectProperty<SessionTimeoutItem> selectedSessionTimeout = new SimpleObjectProperty<>();

    private IntegerProperty editTimeoutsTimeout = new SimpleIntegerProperty();

    private StringProperty editTimeoutsSelectedGame = new SimpleStringProperty();

    private StringProperty editAllowedNewGame = new SimpleStringProperty();

    private StringProperty editAllowedSelectedGame = new SimpleStringProperty();

    private Command openSessionTimeoutEditViewCommand;

    private Command openAllowedGameEditViewCommand;

    /**
     * The webApiPort for the view.
     * 
     * @return The webApiPort as StringProperty.
     */
    public IntegerProperty webApiPortProperty() {
        return this.webApiPort;
    }

    /**
     * The session timeout for every game.
     * 
     * @return List of SettingsItemts.
     */
    public ObservableList<SessionTimeoutItem> gameSessionTimeouts() {
        return gameSessionTimeouts;
    }

    /**
     * The allowed Games.
     * 
     * @return ObservableList of allowed Games.
     */
    public ObservableList<String> allowedGames() {
        return allowedGames;
    }

    public ObjectProperty<String> selectedAllowedGameProperty() {
        return selectedAllowedGame;
    }

    public ObjectProperty<SessionTimeoutItem> selectedSessionTimeoutProperty() {
        return selectedSessionTimeout;
    }

    /**
     * Sets the game with the selected one in the choice box of the view.
     * 
     * @return The game that is selected.
     */
    public StringProperty editTimeoutsSelectedGameProperty() {
        return editTimeoutsSelectedGame;
    }

    public IntegerProperty editTimeoutsTimeoutProperty() {
        return editTimeoutsTimeout;
    }

    /**
     * The name of the new game.
     * 
     * @return The new game.
     */
    public StringProperty editAllowedNewGameProperty() {
        return editAllowedNewGame;
    }

    /**
     * The removed game from the view choice box.
     * 
     * @return The removed game.
     */
    public StringProperty editAllowedSelectedGameProperty() {
        return editAllowedSelectedGame;
    }

    /**
     * Adds the new game to the settings.
     * 
     * @return Saves the updated settings.
     */
    public Command addAllowedGameCommand() {
        return makeCommand(() -> {
            ArrayList<String> games = new ArrayList<>(configManager.getAllowedGames());
            games.add(editAllowedNewGame.get());

            // Add to allowed games and timeouts
            allowedGames.add(editAllowedNewGame.get());
            var newSessionTimeout = new SessionTimeoutItem(editAllowedNewGame.get(), 10);
            gameSessionTimeouts.add(newSessionTimeout);

            configManager.updateAllowedGames(games);
            configManager.saveSettings();
        });
    }

    /**
     * Removes the chosen game from the settings
     * 
     * @return Saves the updated settings.
     */
    public Command removeAllowedGameCommand() {
        return makeCommand(() -> {
            ArrayList<String> games = new ArrayList<>(configManager.getAllowedGames());

            for (int i = 0; i < games.size(); i++) {
                if (games.get(i).contains(editAllowedSelectedGame.get())) {
                    games.remove(i);

                    gameSessionTimeouts.removeIf(x -> x.getGame().equals(editAllowedSelectedGame.get()));
                    allowedGames.remove(editAllowedSelectedGame.get());
                }
            }

            configManager.updateAllowedGames(games);
            configManager.saveSettings();
        });
    }

    /**
     * Updates the sessionTimeout setting with the new value and saves it.
     * 
     * @return Saves the updated settings.
     */
    public Command updateSessionTimeoutCommand() {
        return makeCommand(() -> {
            HashMap<String, Integer> sessionTimeout = configManager.getSessionTimeout();
            sessionTimeout.replace(editTimeoutsSelectedGame.get(), editTimeoutsTimeout.get());

            if (configManager.updateSessionTimeout(sessionTimeout)) {
                var item = StreamUtil.firstOrNull(gameSessionTimeouts,
                        x -> x.getGame().equals(editTimeoutsSelectedGame.get()));
                item.setTimeout(editTimeoutsTimeout.get());
                editAllowedNewGame.set(null);
                configManager.saveSettings();
            } else {
                dialogHelper.showWarning(
                        "Couldent update session timeouts, since atleast one of the given timeouts was < 1.");
            }
        });
    }

    /**
     * Command to open the sessionTimeoutEditView.
     * 
     * @return Opens the sessionTimeoutEditView.
     */
    public Command openSessionTimeoutEditViewCommand() {
        if (openSessionTimeoutEditViewCommand == null) {
            openSessionTimeoutEditViewCommand = makeCommand(() -> {
                if (selectedSessionTimeout.get() != null) {
                    editTimeoutsSelectedGame.set(selectedSessionTimeout.get().getGame());
                    editTimeoutsTimeout.set(selectedSessionTimeout.get().getTimeout());
                } else {
                    editTimeoutsSelectedGame.set(gameSessionTimeouts.get(0).getGame());
                    editTimeoutsTimeout.set(gameSessionTimeouts.get(0).getTimeout());
                }

                var viewConfig = new ShowViewConfiguration(SessionTimeoutEditView.class, "Session Timeout", true);
                dialogHelper.showView(viewConfig, this);
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
            openAllowedGameEditViewCommand = makeCommand(() -> {
                if (selectedAllowedGame.get() != null) {
                    editAllowedSelectedGame.set(selectedAllowedGame.get());
                } else {
                    editAllowedSelectedGame.set(allowedGames.get(0));
                }

                var viewConfig = new ShowViewConfiguration(AllowedGamesEditView.class, "Allowed Games", true);
                dialogHelper.showView(viewConfig, this);
            });
        }
        return openAllowedGameEditViewCommand;
    }

    /**
     * Updates the settings values with the new values and saves them.
     * 
     * @return Saves the settings per runnableCommand.
     */
    public Command updateSettingsCommand() {
        return makeCommand(() -> {
            // updating and saving the settings
            configManager.updateWebApiPort(webApiPort.get());

            Workspace.getInstance().getConfigManager().saveSettings();
        });
    }

    /**
     * Restores the default settings.
     * 
     * @return REstores the default settings per runnableCommand.
     */
    public Command restoreDefaultSettingsCommand() {
        return makeCommand(() -> {
            Workspace.getInstance().getConfigManager().restoreDefaultSettings();
        });
    }
}