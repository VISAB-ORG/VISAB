package api;

import java.io.IOException;

import eventbus.ApiEventBus;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;

public class WebApi extends RouterNanoHTTPD {

    private static ApiEventBus apiEventBus = new ApiEventBus();

    public static ApiEventBus getEventBus() {
	return apiEventBus;
    }

    public WebApi(int port) {
	super(port);
	addMappings();
    }

    @Override
    public void addMappings() {
	addRoute("/", IndexHandler.class);
	addRoute("/session", SessionHandler.class);
	addRoute("/session/open", SessionHandler.class);
    }

    @Override
    public void start() throws IOException {
	start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
    }

}
