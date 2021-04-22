package org.visab.eventbus;

/**
 * The ISubscriber interface that all subscribers have to implement.
 *
 * @author moritz
 *
 */
public interface ISubscriber {

    String getSubscribedEventType();

}
