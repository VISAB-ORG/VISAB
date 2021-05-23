package org.visab.api;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.api.controller.GameSupportController;
import org.visab.api.controller.MapController;
import org.visab.api.controller.SessionController;
import org.visab.api.controller.StatisticsController;
import org.visab.processing.SessionListenerFactory;
import org.visab.util.Settings;

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

    /**
     * Singelton instance
     */
    private static WebApi instance;

    /**
     * Gets the singelton instance
     * 
     * @return The instance
     */
    public static WebApi getInstance() {
        if (instance == null)
            instance = new WebApi();

        return instance;
    }

    private WebApi() {
        super(Settings.API_PORT);
        addMappings();
    }

    private SessionWatchdog watchdog = new SessionWatchdog();
    private SessionListenerFactory listenerFactory = new SessionListenerFactory();

    public SessionWatchdog getSessionWatchdog() {
        return this.watchdog;
    }

    public SessionListenerFactory getListenerFactory() {
        return this.listenerFactory;
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

    /**
     * Shutdown the WebApi, SessionListenerFactory and SessionWatchdog
     */
    public void shutdown() {
        listenerFactory.stopFactory();
        watchdog.stopTimeoutLoop();
        this.stop();
        logger.info("Stopped WebApi & SessionListenerFactory & Session TimeoutLoop.");
    }

    /**
     * Starts the WebApi, SessionListenerFactory, SessionWatchdog
     */
    @Override
    public void start() throws IOException {
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        listenerFactory.startFactory();
        watchdog.StartTimeoutLoop();
        logger.info("Started WebApi & SessionListenerFactory & Session TimeoutLoop.");
    }

}
