package eventbus;

public interface ISubscriberWithEvent<TEvent> {

    void invoke(TEvent event);

}
