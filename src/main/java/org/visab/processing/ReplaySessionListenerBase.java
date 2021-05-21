package org.visab.processing;

import java.util.UUID;

import org.visab.api.WebApi;
import org.visab.eventbus.event.MapImageReceivedEvent;
import org.visab.eventbus.subscriber.SubscriberBase;
import org.visab.globalmodel.IStatistics;

/**
 *
 * @author moritz
 *
 * @param <TStatistics>
 * @param <TMapImage>
 * @param <TMapInformation>
 */
public abstract class ReplaySessionListenerBase<TStatistics extends IStatistics, TMapImage extends IMapImage>
        extends SessionListenerBase<TStatistics> implements IMapImageListener<TMapImage> {

    private class UnityMapImageSubscriber extends SubscriberBase<MapImageReceivedEvent> {

        public UnityMapImageSubscriber() {
            super(MapImageReceivedEvent.class);
        }

        @Override
        @SuppressWarnings("unchecked")
        public void notify(MapImageReceivedEvent event) {
            if (event.getSessionId().equals(sessionId)) {
                processMapImage((TMapImage) event.getImage());
            }
        }
    }

    public ReplaySessionListenerBase(String game, UUID sessionId) {
        super(game, sessionId);

        var mapImageSubscriber = new UnityMapImageSubscriber();
        WebApi.getEventBus().subscribe(mapImageSubscriber);
        subscribers.add(mapImageSubscriber);
    }

    @Override
    public abstract void processMapImage(TMapImage mapImage);

}
