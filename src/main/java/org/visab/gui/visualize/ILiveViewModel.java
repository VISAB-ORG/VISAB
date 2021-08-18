package org.visab.gui.visualize;

import org.visab.globalmodel.IStatistics;
import org.visab.processing.ILiveViewable;

/**
 * The ILiveViewModel interface, that view models wanting to support live views
 * have to implement. The session listener that is docked onto has this
 * interface as its dependancy to the viewmodel.
 */
public interface ILiveViewModel<TStatistics extends IStatistics> {

    /**
     * Initalizes the viewmodel with the listener by docking on to it.
     * 
     * @param listener The lister to dock on to
     */
    void initialize(ILiveViewable<? extends IStatistics> listener);

    /**
     * Called by the docked listener upon reciving new statistics.
     * 
     * @param newStatistics The newely received statistics.
     */
    void onStatisticsAdded(TStatistics newStatistics);

    /**
     * Called by the docked listener upon closing the session.
     */
    void onSessionClosed();

}
