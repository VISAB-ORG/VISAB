package eventbus;

public interface IPublisher<TEvent> {

    void Publish(TEvent event);
}
