package org.visab.processing;

import org.visab.globalmodel.IImage;
import org.visab.globalmodel.IStatistics;

/**
 * The IReplaySessionListener interface extending the ISessionListener interface by
 * the functionality of processing received IImage objects.
 * 
 * @param <TStatistics> The type of the statistics that will be processed by the
 *                      listener
 * @param <TImage>      The type of the image that will be processed by the
 *                      listener
 * @author moritz
 *
 */
public interface IReplaySessionListener<TStatistics extends IStatistics, TImage extends IImage>
        extends ISessionListener<TStatistics> {

    /**
     * Called upon reciving map images for the current session. Is only called if
     * the received Image object was not null.
     * 
     * @param image The received Image object
     */
    void processImage(TImage image);

}
