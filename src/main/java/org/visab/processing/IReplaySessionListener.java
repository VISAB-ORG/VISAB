package org.visab.processing;

public interface IReplaySessionListener<TStatistics, TImage> {

    /**
     * Called upon reciving map images for the current session. Is only called if
     * the received Image object was not null.
     * 
     * @param image The received Image object
     */
    void processImage(TImage image);

    /**
     * Called upon reciving statistics for the current session. Is only called if
     * the received Statistics object was not null.
     * 
     * @param statistics The received TStatistics object
     */
    void processStatistics(TStatistics statistics);
}
