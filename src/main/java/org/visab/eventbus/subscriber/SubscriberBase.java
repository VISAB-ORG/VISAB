package org.visab.eventbus.subscriber;

import org.visab.eventbus.ISubscriber;
import org.visab.eventbus.ISubscriberWithEvent;

/**
 * The base subscriber class, that all subscribers should inherit from.
 *
 * @author moritz
 *
 * @param <TEvent> The event that the class will subscribe to.
 */
public abstract class SubscriberBase<TEvent> implements ISubscriber, ISubscriberWithEvent<TEvent> {

    private String subscribedEventType;

    public SubscriberBase(Class<TEvent> eventClass) {
        this.subscribedEventType = eventClass.getSimpleName();
    }

    @Override
    public String getSubscribedEventType() {
        return subscribedEventType;
    }
}
