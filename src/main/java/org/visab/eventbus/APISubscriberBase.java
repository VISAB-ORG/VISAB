package org.visab.eventbus;

/**
 * The APISubscriberBase class, that all API event subscribers should inherit
 * from.
 *
 * @param <TEvent> The event that the class will subscribe to.
 */
public abstract class APISubscriberBase<TEvent extends IAPIEvent> implements ISubscriber<TEvent> {

    private String subscribedEventType;

    /**
     * @param eventClass The class of the event to subscribe to
     */
    public APISubscriberBase(Class<TEvent> eventClass) {
        this.subscribedEventType = eventClass.getName();
        APIEventBus.getInstance().subscribe(this);
    }

    @Override
    public String getSubscribedEventType() {
        return subscribedEventType;
    }

    @Override
    public abstract void notify(TEvent event);

}
