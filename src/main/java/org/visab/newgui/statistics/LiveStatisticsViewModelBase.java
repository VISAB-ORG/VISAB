package org.visab.newgui.statistics;

import org.visab.globalmodel.IStatistics;
import org.visab.globalmodel.IVISABFile;
import org.visab.processing.ILiveViewable;

public abstract class LiveStatisticsViewModelBase<TFile extends IVISABFile, TStatistics extends IStatistics>
        extends StatisticsViewModelBase<TFile> implements ILiveViewModel<TStatistics> {

    /**
     * Whether the current views corresponding transmission session is still active
     */
    protected boolean isActive;

    /**
     * 
     * Initializer for live view. Docks into the given listener and invokes
     * notifyStatisticsAdded for all statistics of the listener.
     * 
     * @param listener The listener to dock onto
     */
    @SuppressWarnings("unchecked")
    public void initialize(ILiveViewable<? extends IStatistics> listener) {
        var concreteListener = (ILiveViewable<TStatistics>) listener;

        isLive = true;
        isActive = true;

        // dock onto listener
        concreteListener.addViewModel(this);

        // Notify for all the already received statistics
        for (var statistics : concreteListener.getReceivedStatistics())
            notifyStatisticsAdded(statistics);
    }

    @Override
    public abstract void notifyStatisticsAdded(TStatistics newStatistics);

    @Override
    public abstract void notifySessionClosed();

}
