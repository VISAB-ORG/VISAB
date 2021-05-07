package org.visab.processing;

public interface IReplaySessionListener<TStatistics, TMapImage> {

    void processMapImage(TMapImage mapImage);

    void processStatistics(TStatistics statistics);
}
