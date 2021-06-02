package org.visab.api;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.api.controller.GameSupportController;
import org.visab.api.controller.MapController;
import org.visab.api.controller.SessionController;
import org.visab.api.controller.StatisticsController;
import org.visab.api.model.NewSessionWatchdog;
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

    /**
     * Singelton instance
     */
    private static WebApi instance;

    // Logger needs .class for each class to use for log traces
    private static Logger logger = LogManager.getLogger(WebApi.class);

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

    private SessionListenerFactory listenerFactory = new SessionListenerFactory();

    private TempThingy temp = new TempThingy();

    private NewSessionWatchdog watchdog;

    private WebApi() {
        super(Settings.API_PORT);
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

    public SessionListenerFactory getListenerFactory() {
        return this.listenerFactory;
    }

    public TempThingy getTempThingy() {
        return this.temp;
    }

    /**
     * Shutsdown the WebApi, SessionListenerFactory and SessionWatchdog
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
        watchdog = new NewSessionWatchdog(temp.getSessionStatuses());

        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        listenerFactory.startFactory();
        watchdog.StartTimeoutLoop();
        logger.info("Started WebApi & SessionListenerFactory & Session TimeoutLoop.");
    }

}
