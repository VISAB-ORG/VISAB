package org.visab.newgui.statistics;

import org.visab.globalmodel.IVISABFile;
import org.visab.newgui.ViewModelBase;

public abstract class StatisticsViewModelBase<TFile extends IVISABFile> extends ViewModelBase {

    /**
     * Returns whether the StatisticsViewModel supports live viewing
     * 
     * @return True if live viewing is supported
     */
    public boolean supportsLiveViewing() {
        return this instanceof ILiveViewModel<?>;
    }

    /**
     * The file to visualize
     */
    protected TFile file;

    /**
     * Whether the opened view is opened as a live view
     */
    protected boolean isLive;

    /**
     * Initializer for non live view
     * 
     * @param file The file to present
     */
    @SuppressWarnings("unchecked")
    public void initialize(IVISABFile file) {
        this.file = (TFile) file;
        isLive = false;
    }
}
