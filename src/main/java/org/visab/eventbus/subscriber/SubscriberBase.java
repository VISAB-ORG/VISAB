package org.visab.eventbus.subscriber;

import org.visab.eventbus.IEvent;
import org.visab.eventbus.ISubscriber;

/**
 * The base subscriber class, that all subscribers should inherit from.
 *
 * @author moritz
 *
 * @param <TEvent> The event that the class will subscribe to.
 */
public abstract class SubscriberBase<TEvent extends IEvent> implements ISubscriber<TEvent> {

    private String subscribedEventType;

    public SubscriberBase(Class<TEvent> eventClass) {
        this.subscribedEventType = eventClass.getSimpleName();
    }

    @Override
    public String getSubscribedEventType() {
        return subscribedEventType;
    }

    @Override
    public abstract void notify(TEvent event);

}
