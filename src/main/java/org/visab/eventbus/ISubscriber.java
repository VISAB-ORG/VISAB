package org.visab.eventbus;

/**
 * The ISubscriber interface that all subscribers have to implement.
 * This interface is needed, since so that the EventBus can publish the exact
 * event.
 *
 * @author moritz
 *
 * @param <TEvent> The event that will be subscribed to.
 */
public interface ISubscriber<TEvent> {

    void invoke(TEvent event);

    String getSubscribedEventType();

}
