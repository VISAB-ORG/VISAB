package org.visab.processing;

import java.util.UUID;

import org.visab.api.WebApi;
import org.visab.eventbus.event.ImageReceivedEvent;
import org.visab.eventbus.subscriber.SubscriberBase;
import org.visab.globalmodel.IImage;
import org.visab.globalmodel.IStatistics;

/**
 * The base ReplaySessionListener class, that should be implemented by all
 * session listeners with replay functionality.
 * 
 * @param <TStatistics> The statistics type that will be processed by the
 *                      listener
 * @param <TImage>      The image type that will be processed by the listener
 */
public abstract class ReplaySessionListenerBase<TStatistics extends IStatistics, TImage extends IImage>
        extends SessionListenerBase<TStatistics> implements IReplaySessionListener<TStatistics, TImage> {

    /**
     * The ImageSubscriber that subscribes to the ImageReceivedEvent
     */
    private class ImageSubscriber extends SubscriberBase<ImageReceivedEvent> {

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
        WebApi.instance.getEventBus().subscribe(mapImageSubscriber);
        subscribers.add(mapImageSubscriber);
    }

    @Override
    public abstract void processImage(TImage image);

}
