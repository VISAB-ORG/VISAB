package org.visab.newgui.visualize;

import org.visab.globalmodel.IStatistics;
import org.visab.globalmodel.IVISABFile;
import org.visab.processing.ILiveViewable;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * A base implementation for viewmodels that want to support live views. In a
 * live view, the datasource is the session listener instead of a VISAB file.
 * Live views extend regular non live views by the capability of reacting to
 * data the moment it is processed by the session listener.
 */
public abstract class LiveVisualizeViewModelBase<TFile extends IVISABFile, TStatistics extends IStatistics>
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
    public abstract void onStatisticsAdded(TStatistics newStatistics);

    @Override
    public void onSessionClosed() {
        liveViewActiveProperty.set(false);
        listener.removeViewModel(this);
    }

}
