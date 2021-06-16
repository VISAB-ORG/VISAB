package org.visab.processing;

import java.util.List;

import org.visab.globalmodel.IStatistics;
import org.visab.globalmodel.IVISABFile;
import org.visab.newgui.visualize.ILiveViewModel;

/**
 * The ILiveViewable interface, that all live viewable SessionListeners have to
 * implement. ILiveViewables can have a multitude of ILiveViewModel as
 * observers.
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
     * Returns a copy of all statistics that were added till now.
     * 
     * @return The list of received statistics
     */
    List<TStatistics> getReceivedStatistics();

    /**
     * Returns the current (unfinished) IVISABFile.
     */
    IVISABFile getCurrentFile();

    /**
     * Notifies the observing ViewModels that the session was closed.
     */
    void notifySessionClosed();

    /**
     * Notifies the observing ViewModels that statistics were added.
     * 
     * @param addedStatistics The added statistics
     */
    void notifyStatisticsAdded(TStatistics addedStatistics);
}