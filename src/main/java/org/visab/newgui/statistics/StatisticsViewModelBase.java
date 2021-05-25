package org.visab.newgui.statistics;

import org.visab.newgui.ViewModelBase;

public abstract class StatisticsViewModelBase extends ViewModelBase {

    /**
     * Returns whether the StatisticsViewModel supports live viewing
     * 
     * @return True if live viewing is supported
     */
    public boolean supportsLiveViewing() {
        return this instanceof ILiveViewModel;
    }
}
