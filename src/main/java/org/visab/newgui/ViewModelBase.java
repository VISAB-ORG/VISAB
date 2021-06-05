package org.visab.newgui;

import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.utils.commands.Action;
import de.saxsys.mvvmfx.utils.commands.Command;
import de.saxsys.mvvmfx.utils.commands.DelegateCommand;

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

    /**
     * The DialogHelper that can be used to show dialogs from the viewmodel. To use
     * the helper, you first have to set the parent window from the View by calling
     * getDialogHelper().setParentWindow().
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
