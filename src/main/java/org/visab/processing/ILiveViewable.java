package org.visab.processing;

import java.util.List;

import org.visab.globalmodel.IStatistics;
import org.visab.globalmodel.IVISABFile;
import org.visab.newgui.visualize.ILiveViewModel;

/**
 * The ILiveViewable interface, that all live viewable SessionListeners have to
 * implement. ILiveViewables can have a multitude of ILiveViewModel as
 * observers, that will be informed when statistics are received or the session
 * is closed.
 * 
 * @param <TStatistics> The statistics type received and added by the listener
 */
public interface ILiveViewable<TStatistics extends IStatistics> {

    /**
     * Adds a ViewModel to the list of ViewModels that will be informed of added
     * statistics.
     * 
     * @param viewModel The ViewModel to add
     */
    void addViewModel(ILiveViewModel<TStatistics> viewModel);

    /**
     * Removes a ViewModel from the list of ViewModels that will be informed of
     * added statistics.
     * 
     * @param viewModel The ViewModel to remove
     */
    void removeViewModel(ILiveViewModel<TStatistics> viewModel);

    /**
     * Returns the current (unfinished) IVISABFile.
     */
    IVISABFile getCurrentFile();

    /**
     * Returns a list of all statistics that were added till now.
     */
    List<TStatistics> getStatistics();

    /**
     * Notifies the observing ViewModels that statistics were added.
     * 
     * Serves as a reminder that this functionality should be implemented, but
     * should never be called outside the classes themselves.
     * 
     * @param addedStatistics The added statistics
     */
    void notifyStatisticsAdded(TStatistics addedStatistics);

    /**
     * Notifies the observing ViewModels that the session was closed.
     * 
     * Serves as a reminder that this functionality should be implemented, but
     * should never be called outside the classes themselves.
     */
    void notifySessionClosed();

}