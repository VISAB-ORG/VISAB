package org.visab.processing;

import org.visab.globalmodel.IImage;
import org.visab.globalmodel.IStatistics;

/**
 * The IReplayLiveViewable interface, that all replay live viewable
 * SessionListeners have to implement. Extends the ILiveViewable interface by
 * the capability of getting the images received.
 * 
 * @param <TStatistics> The statistics type received and added by the listener
 * @param <TImage>      The image type received by the listener
 */
public interface IReplayLiveViewable<TStatistics extends IStatistics, TImage extends IImage>
        extends ILiveViewable<TStatistics> {

    /**
     * Returns the images that were received.
     * 
     * @return The received images
     */
    TImage getReceivedImage();

    /**
     * Notifies the observing ViewModels that images were added.
     * 
     * @param addedImage The added images
     */
    void notifyStatisticsAdded(TImage addedImage);

}
