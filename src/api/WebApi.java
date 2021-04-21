package api;

import java.io.IOException;

import org.nanohttpd.protocols.http.NanoHTTPD;
import org.nanohttpd.router.RouterNanoHTTPD;

import eventbus.ApiEventBus;

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
	addRoute("/test", HTTPHandlerBase.class);
	addRoute("/session", SessionHandler.class);
	addRoute("/session/open", SessionHandler.class);
	addRoute("/session/list", SessionHandler.class);
	addRoute("/session/status", SessionHandler.class);
	addRoute("/session/close", SessionHandler.class);
    }

    public void shutdown() {
	this.stop();
    }

    @Override
    public void start() throws IOException {
	start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
    }

}
