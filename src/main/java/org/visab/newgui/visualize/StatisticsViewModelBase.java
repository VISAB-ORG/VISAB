package org.visab.newgui.visualize;

import org.visab.globalmodel.IVISABFile;
import org.visab.newgui.ViewModelBase;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public abstract class StatisticsViewModelBase<TFile extends IVISABFile> extends ViewModelBase
        implements IStatisticsViewModel {

    /**
     * Returns whether the StatisticsViewModel supports live viewing.
     * 
     * @return True if live viewing is supported
     */
    public boolean supportsLiveViewing() {
        return this instanceof ILiveStatisticsViewModel<?>;
    }

    /**
     * The file to visualize. The reference contained in this variable should not
     * change.
     */
    protected TFile file;

    /**
     * Whether the opened view is opened as a live view.
     */
    protected BooleanProperty isLiveViewProperty = new SimpleBooleanProperty();

    /**
     * Whether the opened view is opened as a live view.
     * 
     * @return The boolean property
     */
    public BooleanProperty isLiveViewProperty() {
        return isLiveViewProperty;
    }

    /**
     * Initializer for non live view.
     * 
     * @param file The file to visualize
     */
    @SuppressWarnings("unchecked")
    public void initialize(IVISABFile file) {
        if (file == null)
            throw new RuntimeException("File was null!");

        this.file = (TFile) file;
        isLiveViewProperty.set(false);
    }

}
