package org.visab.newgui;

import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.utils.commands.Action;
import de.saxsys.mvvmfx.utils.commands.Command;
import de.saxsys.mvvmfx.utils.commands.DelegateCommand;

public abstract class ViewModelBase implements ViewModel {

    /**
     * Creates a DelegateCommand whose action is to run the given Runnable.
     * 
     * @param runnable The runnable to invoke
     * @return A command
     */
    public Command runnableCommand(Runnable runnable) {
        return new DelegateCommand(() -> new Action() {
            @Override
            protected void action() {
                try {
                    runnable.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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
