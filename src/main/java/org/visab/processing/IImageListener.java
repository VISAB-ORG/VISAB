package org.visab.processing;

public interface IImageListener<TImage extends IImage> {

    void processImage(TImage image);

}
