package eventbus;

/**
 * The IPublisher interface, that all publishers have to implement.
 *
 * @author moritz
 *
 * @param <TEvent> The event that will be published.
 */
public interface IPublisher<TEvent> {

    void publish(TEvent event);
}
