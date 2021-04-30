package org.visab.api;

import java.io.IOException;

import org.visab.api.controller.GameSupportController;
import org.visab.api.controller.MapController;
import org.visab.api.controller.SessionController;
import org.visab.api.controller.StatisticsController;
import org.visab.eventbus.ApiEventBus;
import org.visab.processing.SessionListenerFactory;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;

/**
 * The main WebApi that hosts a small HTTP server. This is the entry point for
 * all HTTP communication.
 *
 * @author moritz
 *
 */
public class WebApi extends RouterNanoHTTPD {

    private static ApiEventBus apiEventBus = new ApiEventBus();
    private static SessionListenerFactory listenerFactory;
    private static SessionWatchdog watchdog;

    public static ApiEventBus getEventBus() {
        return apiEventBus;
    }

    public WebApi(int port) {
        super(port);
        addMappings();

        // Just initialize it anywhere and it will be a subscriber in the eventbus.
        listenerFactory = new SessionListenerFactory();

        // Start the session administration. This also starts the session timeout loop
        watchdog = new SessionWatchdog();
    }

    /**
     * Add the controllers to the endpoints
     */
    @Override
    public void addMappings() {
        addRoute("/", IndexHandler.class);
        addRoute("/ping", IndexHandler.class);
        addRoute("/session/open", SessionController.class);
        addRoute("/session/list", SessionController.class);
        addRoute("/session/status", SessionController.class);
        addRoute("/session/close", SessionController.class);
        addRoute("send/statistics", StatisticsController.class);
        addRoute("send/map/image", MapController.class);
        addRoute("send/map/information", MapController.class);
        addRoute("games", GameSupportController.class);
    }

    public void shutdown() {
        watchdog.stopTimeoutLoop();
        this.stop();
    }

    @Override
    public void start() throws IOException {
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
    }

}
