package org.visab.api;

import java.io.IOException;

import org.visab.api.controller.GameSupportController;
import org.visab.api.controller.MapController;
import org.visab.api.controller.SessionController;
import org.visab.api.controller.StatisticsController;
import org.visab.eventbus.ApiEventBus;
import org.visab.processing.SessionListenerAdministration;

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
    private static SessionWatchdog watchdog = new SessionWatchdog();

    public static ApiEventBus getEventBus() {
        return apiEventBus;
    }

    public static SessionWatchdog getSessionWatchdog() {
        return watchdog;
    }

    public WebApi(int port) {
        super(port);
        addMappings();

        // Add the SessionListenerFactory as subscriber of eventbus.
        SessionListenerAdministration.initializeFactory();
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
        addRoute("send/map", MapController.class);
        addRoute("games", GameSupportController.class);
    }

    public void shutdown() {
        watchdog.stopTimeoutLoop();
        this.stop();
    }

    @Override
    public void start() throws IOException {
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        watchdog.StartTimeoutLoop();
    }

}
