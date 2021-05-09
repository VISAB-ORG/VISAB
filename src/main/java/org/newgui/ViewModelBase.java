package org.newgui;

import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.utils.commands.Action;
import de.saxsys.mvvmfx.utils.commands.Command;
import de.saxsys.mvvmfx.utils.commands.DelegateCommand;

public abstract class ViewModelBase implements ViewModel {

    public Command runnableCommand(Runnable runnable) {
        return new DelegateCommand(() -> new Action() {
            @Override
            protected void action() throws Exception {
                runnable.run();
            }
        });
    }

}
