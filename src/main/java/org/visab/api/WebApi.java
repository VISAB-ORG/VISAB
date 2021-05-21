package org.visab.api;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.api.controller.GameSupportController;
import org.visab.api.controller.MapController;
import org.visab.api.controller.SessionController;
import org.visab.api.controller.StatisticsController;
import org.visab.eventbus.ApiEventBus;
import org.visab.processing.SessionListenerAdministration;
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

    // Logger needs .class for each class to use for log traces
    private static Logger logger = LogManager.getLogger(WebApi.class);

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
        SessionListenerFactory.instance.stopFactory();
        watchdog.stopTimeoutLoop();
        this.stop();
    }

    @Override
    public void start() throws IOException {
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        SessionListenerFactory.instance.startFactory();
        watchdog.StartTimeoutLoop();
    }

}
