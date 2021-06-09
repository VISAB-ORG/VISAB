package org.visab.newgui.visualize;

import org.visab.globalmodel.IVISABFile;
import org.visab.newgui.ViewModelBase;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public abstract class StatisticsViewModelBase<TFile extends IVISABFile> extends ViewModelBase {

    /**
     * Returns whether the StatisticsViewModel supports live viewing.
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
        this.file = (TFile) file;
        isLiveViewProperty.set(false);

        afterInitialize(this.file);
    }

    /**
     * Is called after initilization from a file. What you would do here typically,
     * is to read in the file into your view models datastrctures.
     * 
     * @param file The file that the view was initialized with.
     */
    public abstract void afterInitialize(TFile file);
}
