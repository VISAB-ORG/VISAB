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

}
