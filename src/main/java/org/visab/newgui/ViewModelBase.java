package org.visab.newgui;

import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.utils.commands.Action;
import de.saxsys.mvvmfx.utils.commands.Command;
import de.saxsys.mvvmfx.utils.commands.DelegateCommand;

/**
 * The base viewmodel implementation should be used instead of implementing
 * ViewModel yourself.
 */
public abstract class ViewModelBase implements ViewModel {

    /**
     * Creates a DelegateCommand whose action is to run the given Runnable.
     * 
     * @param runnable The runnable that should be ran
     * @return A command
     */
    protected Command makeCommand(Runnable runnable) {
        DelegateCommand command = new DelegateCommand(() -> new Action() {
            @Override
            protected void action() {
                runnable.run();
            }
        });

        // Add a handler for writing the exceptions stacktrace.
        command.exceptionProperty().addListener((o, old, throwable) -> throwable.printStackTrace());

        return command;
    }

    /**
     * The DialogHelper that can be used to show dialogs from the viewmodel.
     */
    protected DialogHelper dialogHelper = new DialogHelper();
}
