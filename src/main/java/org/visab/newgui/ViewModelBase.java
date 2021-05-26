package org.visab.newgui;

import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.utils.commands.Action;
import de.saxsys.mvvmfx.utils.commands.Command;
import de.saxsys.mvvmfx.utils.commands.DelegateCommand;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public abstract class ViewModelBase implements ViewModel {

    /**
     * Gets a Command that invokes the given runnable
     * 
     * @param runnable The runnable to invoke
     * @return A command
     */
    public Command runnableCommand(Runnable runnable) {
        return new DelegateCommand(() -> new Action() {
            @Override
            protected void action() throws Exception {
                runnable.run();
            }
        });
    }

    public void openWindow(Parent parent, String title) {
        var stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(parent));
        stage.show();
    }

    public void openWindow(Parent parent, String title, StageStyle style) {
        var stage = new Stage(style);
        stage.setTitle(title);
        stage.setScene(new Scene(parent));
        stage.show();
    }

}
