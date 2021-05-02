package org.visab.processing;

import java.util.UUID;

import org.visab.eventbus.subscriber.SubscriberBase;

public abstract class UnitySessionListenerBase<TStatistics, TMapImage, TMapInformation> extends SessionListenerBase<TStatistics> {

    private class UnityMapImageSubscriber extends SubscriberBase<TMapImage> {

        public UnityMapImageSubscriber(String subscribedEventType) {
            super(subscribedEventType);
        }

        @Override
        public void invoke(TMapImage event) {
            
        }
        
    }

    public UnitySessionListenerBase(String game, UUID sessionId) {
        super(game, sessionId);
    }
    
}
