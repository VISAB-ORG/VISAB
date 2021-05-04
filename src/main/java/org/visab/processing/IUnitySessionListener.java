package org.visab.processing;

public interface IUnitySessionListener<TStatistics, TMapImage, TMapInformation> {

    void processMapImage(TMapImage mapImage);

    void processMapInformation(TMapInformation mapInformation);

    void processStatistics(TStatistics statistics);
}
