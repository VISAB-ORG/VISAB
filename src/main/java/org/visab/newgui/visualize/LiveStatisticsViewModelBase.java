package org.visab.newgui.visualize;

import org.visab.globalmodel.IStatistics;
import org.visab.globalmodel.IVISABFile;
import org.visab.processing.ILiveViewable;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public abstract class LiveStatisticsViewModelBase<TFile extends IVISABFile, TStatistics extends IStatistics>
        extends StatisticsViewModelBase<TFile> implements ILiveStatisticsViewModel<TStatistics> {

    /**
     * Whether the current listeners corresponding transmission session is still
     * active.
     */
    protected BooleanProperty liveSessionActiveProperty = new SimpleBooleanProperty(false);

    /**
     * Whether the current listeners corresponding transmission session is still
     * active.
     * 
     * @return The boolean property
     */
    public BooleanProperty liveSessionActiveProperty() {
        return liveSessionActiveProperty;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void initializeLive(ILiveViewable<? extends IStatistics> listener) {
        var concreteListener = (ILiveViewable<TStatistics>) listener;

        isLiveViewProperty.set(true);
        liveSessionActiveProperty.set(true);

        // dock onto listener
        concreteListener.addViewModel(this);

        // Set the file
        this.file = (TFile) concreteListener.getCurrentFile();

        afterInitializeLive(this.file, concreteListener);
    }

    /**
     * Called after the viewmodel docked onto the listener and set its own file.
     * What you would do here typically is to read in all the received statistics
     * from the listener.
     * 
     * @param listener The listener that was docked onto
     */
    protected abstract void afterInitializeLive(TFile file, ILiveViewable<TStatistics> listener);

    @Override
    public abstract void notifyStatisticsAdded(TStatistics newStatistics);

    @Override
    public abstract void notifySessionClosed();

    @Override
    protected abstract void afterInitialize(TFile file);

}
