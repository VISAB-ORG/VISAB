package org.visab.processing;

import java.util.UUID;

import org.visab.api.WebApi;
import org.visab.eventbus.event.MapImageReceivedEvent;
import org.visab.eventbus.event.MapInformationReceivedEvent;
import org.visab.eventbus.subscriber.SubscriberBase;

/**
 *
 * @author moritz
 *
 * @param <TStatistics>
 * @param <TMapImage>
 * @param <TMapInformation>
 */
public abstract class UnitySessionListenerBase<TStatistics, TMapImage, TMapInformation> extends
        SessionListenerBase<TStatistics> implements IUnitySessionListener<TStatistics, TMapImage, TMapInformation> {

    private class UnityMapImageSubscriber extends SubscriberBase<MapImageReceivedEvent> {

        public UnityMapImageSubscriber() {
            super(MapImageReceivedEvent.class);
        }

        @Override
        @SuppressWarnings("unchecked")
        public void invoke(MapImageReceivedEvent event) {
            if (event.getSessionId().equals(sessionId)) {
                processMapImage((TMapImage) event.getImage());
            }
        }
    }

    private class UnityMapInformationSubscriber extends SubscriberBase<MapInformationReceivedEvent> {

        public UnityMapInformationSubscriber() {
            super(MapInformationReceivedEvent.class);
        }

        @Override
        @SuppressWarnings("unchecked")
        public void invoke(MapInformationReceivedEvent event) {
            if (event.getSessionId().equals(sessionId)) {
                processMapInformation((TMapInformation) event.getInformation());
            }
        }
    }

    public UnitySessionListenerBase(String game, UUID sessionId) {
        // TODO: Be careful here, since the super contructor invokes onSessionStart()
        // and we can't know how much time will be spent in the call. Potentially call
        // onSessionStart from the factory instead.
        super(game, sessionId);

        var mapImageSubscriber = new UnityMapImageSubscriber();
        var mapInfoSubscriber = new UnityMapInformationSubscriber();

        WebApi.getEventBus().subscribe(mapImageSubscriber);
        WebApi.getEventBus().subscribe(mapInfoSubscriber);

        subscribers.add(mapImageSubscriber);
        subscribers.add(mapInfoSubscriber);
    }

    @Override
    public abstract void processMapImage(TMapImage mapImage);

    @Override
    public abstract void processMapInformation(TMapInformation mapInformation);
}
