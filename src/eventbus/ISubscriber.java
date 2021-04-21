package eventbus;

public interface ISubscriber<TEvent> {

    public String getSubscribedEventType();

    public void invoke(TEvent event);

}
