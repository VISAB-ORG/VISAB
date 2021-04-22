package api;

import java.io.IOException;

import org.nanohttpd.protocols.http.NanoHTTPD;
import org.nanohttpd.router.RouterNanoHTTPD;

import api.controller.MapController;
import api.controller.SessionController;
import api.controller.StatisticsController;
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
	addRoute("/ping", IndexHandler.class);
	// addRoute("/session", SessionController.class);
	addRoute("/session/open", SessionController.class);
	addRoute("/session/list", SessionController.class);
	addRoute("/session/status", SessionController.class);
	addRoute("/session/close", SessionController.class);
	addRoute("send/statistics", StatisticsController.class);
	addRoute("send/map", MapController.class);
    }

    public void shutdown() {
	this.stop();
    }

    @Override
    public void start() throws IOException {
	start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
    }

}
