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
	addRoute("/test", TestHandler.class);
	addRoute("/session", SessionHandler.class);
    }

    public void shutdown() {
	this.stop();
    }

    @Override
    public void start() throws IOException {
	start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
    }

}
