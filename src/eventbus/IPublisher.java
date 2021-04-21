package eventbus;

public interface IPublisher<TEvent> {

    void publish(TEvent event);
}
