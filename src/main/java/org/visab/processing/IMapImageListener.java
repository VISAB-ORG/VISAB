package org.visab.processing;

public interface IMapImageListener<TMapImage extends IMapImage> {

    void processMapImage(TMapImage mapImage);

}
