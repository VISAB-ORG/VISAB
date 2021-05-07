package org.visab.processing;

import java.util.UUID;

import org.visab.api.WebApi;
import org.visab.eventbus.event.MapImageReceivedEvent;
import org.visab.eventbus.subscriber.SubscriberBase;

/**
 *
 * @author moritz
 *
 * @param <TStatistics>
 * @param <TMapImage>
 * @param <TMapInformation>
 */
public abstract class ReplaySessionListenerBase<TStatistics extends IStatistics, TMapImage extends IMapImage>
        extends SessionListenerBase<TStatistics> implements IReplaySessionListener<TStatistics, TMapImage> {

    private class MapImageSubscriber extends SubscriberBase<MapImageReceivedEvent> {

        public MapImageSubscriber() {
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

        var mapImageSubscriber = new MapImageSubscriber();
        WebApi.getEventBus().subscribe(mapImageSubscriber);
        subscribers.add(mapImageSubscriber);
    }

    @Override
    public abstract void processMapImage(TMapImage mapImage);

}
