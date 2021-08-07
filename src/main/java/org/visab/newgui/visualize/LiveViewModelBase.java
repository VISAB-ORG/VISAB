package org.visab.newgui.visualize;

import java.util.List;

import org.visab.globalmodel.IStatistics;
import org.visab.globalmodel.IVISABFile;
import org.visab.processing.ILiveViewable;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public abstract class LiveViewModelBase<TFile extends IVISABFile, TStatistics extends IStatistics>
        extends VisualizeViewModelBase<TFile> implements ILiveViewModel<TStatistics> {

    /**
     * The listener that the viewmodel is docked onto.
     */
    protected ILiveViewable<TStatistics> listener;

    /**
     * Whether the current listeners corresponding transmission session is still
     * active.
     */
    protected BooleanProperty liveViewActiveProperty = new SimpleBooleanProperty(false);

    /**
     * Whether the current listeners corresponding transmission session is still
     * active.
     * 
     * @return The boolean property
     */
    public BooleanProperty liveViewActiveProperty() {
        return liveViewActiveProperty;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void initialize(ILiveViewable<? extends IStatistics> listener) {
        if (listener == null)
            throw new RuntimeException("Listener was null!");

        this.listener = (ILiveViewable<TStatistics>) listener;

        // dock onto listener
        this.listener.addViewModel(this);

        liveViewActiveProperty.set(true);

        // Set the file
        this.file = (TFile) listener.getCurrentFile();
    }

    @Override
    public abstract void onStatisticsAdded(TStatistics newStatistics, List<TStatistics> statisticsCopy);

    @Override
    public void onSessionClosed() {
        liveViewActiveProperty.set(false);
        if (listener != null)
            listener.removeViewModel(this);
    }

}
