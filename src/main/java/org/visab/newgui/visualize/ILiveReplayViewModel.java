package org.visab.newgui.visualize;

import org.visab.globalmodel.IImageContainer;
import org.visab.globalmodel.IStatistics;
import org.visab.processing.IReplayLiveViewable;

/**
 * The ILiveReplayViewModel interface, that view models wanting to support live
 * views that depend on images have to implement. The session listener that is
 * docked onto has this interface as its dependancy to the viewmodel.
 */
public interface ILiveReplayViewModel<TStatistics extends IStatistics, TImage extends IImageContainer> {

    /**
     * Initalizes the viewmodel with the listener by docking on to it.
     * 
     * @param listener The lister to dock on to
     */
    void initialize(IReplayLiveViewable<? extends IStatistics, ? extends IImageContainer> listener);

    /**
     * Called by the docked listener upon reciving new statistics.
     * 
     * @param newStatistics The newely received statistics.
     */
    void notifyStatisticsAdded(TStatistics newStatistics);

    /**
     * Called by the docked listener upon reciving new images.
     * 
     * @param newStatistics The newely received images.
     */
    void notifyImageAdded(TImage image);

    /**
     * Called by the docked listener upon closing the session.
     */
    void notifySessionClosed();

}
