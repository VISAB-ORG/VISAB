package org.visab.api;

import org.visab.eventbus.IEvent;
import org.visab.eventbus.event.ImageReceivedEvent;
import org.visab.eventbus.event.SessionClosedEvent;
import org.visab.eventbus.event.SessionOpenedEvent;
import org.visab.eventbus.event.StatisticsReceivedEvent;
import org.visab.eventbus.publisher.PublisherBase;

/**
 * The ApiEventPublisher is a multi publisher for all Api related events.
 * Through him, all events should be published.
 */
public class ApiEventPublisher {

    private class SessionOpenedPublisher extends PublisherBase<SessionOpenedEvent> {
    }

    private class SessionClosedPublisher extends PublisherBase<SessionClosedEvent> {
    }

    private class StatisticsReceivedPublisher extends PublisherBase<StatisticsReceivedEvent> {
    }

    private class ImageReceivedPublisher extends PublisherBase<ImageReceivedEvent> {
    }

    protected SessionOpenedPublisher sessionOpenedPublisher = new SessionOpenedPublisher();
    protected SessionClosedPublisher sessionClosedPublisher = new SessionClosedPublisher();
    protected StatisticsReceivedPublisher statisticsReceivedPublisher = new StatisticsReceivedPublisher();
    protected ImageReceivedPublisher imageReceivedPublisher = new ImageReceivedPublisher();

    /**
     * Publishes the given event to the ApiEventBus instance.
     * 
     * @param <T>   The type of the event
     * @param event The event to publish
     */
    public <T extends IEvent> void publish(T event) {
        if (event instanceof SessionOpenedEvent)
            sessionOpenedPublisher.publish((SessionOpenedEvent) event);
        else if (event instanceof SessionClosedEvent)
            sessionClosedPublisher.publish((SessionClosedEvent) event);
        else if (event instanceof StatisticsReceivedEvent)
            statisticsReceivedPublisher.publish((StatisticsReceivedEvent) event);
        else if (event instanceof ImageReceivedEvent)
            imageReceivedPublisher.publish((ImageReceivedEvent) event);
        else
            throw new RuntimeException("Received unknown event type: " + event.getClass().getSimpleName() + "");
    }

}
