package api;

import java.util.UUID;

public interface IEvent {

	public String getGame();
	
	public UUID getSessionId();
}
