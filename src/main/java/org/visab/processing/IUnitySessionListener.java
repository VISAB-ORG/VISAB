package org.visab.processing;

public interface IUnitySessionListener<TStatistics extends IStatistics, TMapImage extends IUnityMapImage, TMapInformation extends IUnityMapInformation> {

    void processMapImage(TMapImage mapImage);

    void processMapInformation(TMapInformation mapInformation);

    void processStatistics(TStatistics statistics);
}
