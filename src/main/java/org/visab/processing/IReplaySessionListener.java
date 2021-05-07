package org.visab.processing;

public interface IReplaySessionListener<TStatistics, TMapImage> {

    /**
     * Called upon reciving map images for the current session. Is only called if
     * the received MapImage object was not null.
     * 
     * @param mapImage The received MapImage object
     */
    void processMapImage(TMapImage mapImage);

    /**
     * Called upon reciving statistics for the current session. Is only called if
     * the received Statistics object was not null.
     * 
     * @param statistics The received TStatistics object
     */
    void processStatistics(TStatistics statistics);
}
