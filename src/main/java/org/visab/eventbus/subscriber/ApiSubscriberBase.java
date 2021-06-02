package org.visab.eventbus.subscriber;

import org.visab.eventbus.ApiEventBus;
import org.visab.eventbus.IApiEvent;
import org.visab.eventbus.ISubscriber;

/**
 * The base subscriber class, that all api event subscribers should inherit
 * from.
 *
 * @param <TEvent> The event that the class will subscribe to.
 */
public abstract class ApiSubscriberBase<TEvent extends IApiEvent> implements ISubscriber<TEvent> {

    private String subscribedEventType;

    /**
     * @param eventClass The class of the event to subscribe to
     */
    public ApiSubscriberBase(Class<TEvent> eventClass) {
        this.subscribedEventType = eventClass.getName();
        ApiEventBus.getInstance().subscribe(this);
    }

    @Override
    public String getSubscribedEventType() {
        return subscribedEventType;
    }

    @Override
    public abstract void notify(TEvent event);

}
