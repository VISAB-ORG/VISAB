package api;

import java.io.IOException;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;

public class WebApi extends RouterNanoHTTPD {

	public WebApi(int port) {
		super(port);
		addMappings();
	}
	
	public void start() throws IOException {
		start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
	}

	 public void addMappings() {
		addRoute("/", IndexHandler.class);
		addRoute("/session", SessionHandler.class);
		addRoute("/session/open", SessionHandler.class);
	}
	

	
	
}
