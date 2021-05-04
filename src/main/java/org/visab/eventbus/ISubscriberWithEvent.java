package org.visab.eventbus;

/**
 * The ISubscriberWithEvent interface that all subscribers have to implement.
 * This interface is needed, since so that the EventBus can publish the exact
 * event.
 *
 * @author moritz
 *
 * @param <TEvent> The event that will be subscribed to.
 */
public interface ISubscriberWithEvent<TEvent extends IEvent> {

    void notify(TEvent event);

}
