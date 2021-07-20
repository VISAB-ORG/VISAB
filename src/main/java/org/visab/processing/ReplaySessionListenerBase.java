package org.visab.processing;

import java.util.UUID;

import org.visab.eventbus.event.ImageReceivedEvent;
import org.visab.eventbus.subscriber.ApiSubscriberBase;
import org.visab.globalmodel.IImageContainer;
import org.visab.globalmodel.IMetaInformation;
import org.visab.globalmodel.IStatistics;

/**
 * The base ReplaySessionListener class, that should be implemented by all
 * session listeners with replay functionality.
 * 
 * @param <TStatistics> The statistics type that will be processed by the
 *                      listener
 * @param <TImage>      The image type that will be processed by the listener
 */
public abstract class ReplaySessionListenerBase<TMeta extends IMetaInformation, TStatistics extends IStatistics, TImage extends IImageContainer>
        extends SessionListenerBase<TMeta, TStatistics> {

    /**
     * The ImageSubscriber that subscribes to the ImageReceivedEvent
     */
    private class ImageSubscriber extends ApiSubscriberBase<ImageReceivedEvent> {

        public ImageSubscriber() {
            super(ImageReceivedEvent.class);
        }

        @Override
        @SuppressWarnings("unchecked")
        public void notify(ImageReceivedEvent event) {
            if (event.getSessionId().equals(sessionId)) {
                processImage((TImage) event.getImage());
            }
        }
    }

    public ReplaySessionListenerBase(String game, UUID sessionId) {
        super(game, sessionId);

        var mapImageSubscriber = new ImageSubscriber();
        subscribers.add(mapImageSubscriber);
    }

    public abstract void processImage(TImage image);

}
