package eventbus;

public interface ISubscriber<TEvent> {
	
	public void invoke(TEvent event);
}
