package org.visab.newgui;

import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.utils.commands.Action;
import de.saxsys.mvvmfx.utils.commands.Command;
import de.saxsys.mvvmfx.utils.commands.DelegateCommand;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public abstract class ViewModelBase implements ViewModel {

    /**
     * Creates a DelegateCommand whose action is to run the given Runnable. TODO:
     * Catch exception here properly.
     * 
     * @param runnable The runnable to invoke
     * @return A command
     */
    public Command runnableCommand(Runnable runnable) {
        DelegateCommand command = new DelegateCommand(() -> new Action() {
            @Override
            protected void action() {
                runnable.run();
            }
        });
        command.exceptionProperty().addListener((o, old, throwable) -> throwable.printStackTrace());

        return command;
    }

    public static void main(String[] args) {
        var command = new DelegateCommand(() -> new Action() {
            @Override
            protected void action() {
                Object x = null;
                x.toString();
            }
        });
        command.execute();
    }

    /**
     * The DialogHelper that can be used to show dialogs from the viewmodel.
     */
    protected DialogHelper dialogHelper = new DialogHelper();

    /**
     * Gets the DialogHelper.
     * 
     * @return The DialogHelper
     */
    public DialogHelper getDialogHelper() {
        return this.dialogHelper;
    }

}
