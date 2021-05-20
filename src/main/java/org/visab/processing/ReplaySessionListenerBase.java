package org.visab.processing;

import java.util.UUID;

import org.visab.api.WebApi;
import org.visab.eventbus.event.ImageReceivedEvent;
import org.visab.eventbus.subscriber.SubscriberBase;
import org.visab.generalmodelchangeme.IStatistics;

/**
 *
 * @author moritz
 *
 * @param <TStatistics>
 * @param <TImage>
 * @param <TMapInformation>
 */
public abstract class ReplaySessionListenerBase<TStatistics extends IStatistics, TImage extends IImage>
        extends SessionListenerBase<TStatistics> implements IImageListener<TImage> {

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

        var imageSubscriber = new ImageSubscriber();
        WebApi.getEventBus().subscribe(imageSubscriber);
        subscribers.add(imageSubscriber);
    }

    @Override
    public abstract void processImage(TImage image);

}
