package org.visab.api;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.api.controller.FileController;
import org.visab.api.controller.GameSupportController;
import org.visab.api.controller.ImageController;
import org.visab.api.controller.SessionController;
import org.visab.api.controller.StatisticsController;
import org.visab.api.model.SessionWatchdog;
import org.visab.processing.SessionListenerFactory;
import org.visab.workspace.Workspace;

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

    private SessionAdministration sessionAdministration = new SessionAdministration();

    private SessionWatchdog watchdog;

    private WebApi() {
        super(Workspace.getInstance().getConfigManager().getWebApiPort());
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
        addRoute("send/image", ImageController.class);
        addRoute("games", GameSupportController.class);
        addRoute("file/get", FileController.class);
        addRoute("file/send", FileController.class);
    }

    public SessionAdministration getSessionAdministration() {
        return this.sessionAdministration;
    }

    /**
     * Shutsdown the WebApi, SessionListenerFactory and SessionWatchdog
     */
    public void shutdown() {
        logger.info("Stopping WebApi.");
        listenerFactory.stopFactory();
        watchdog.stopTimeoutLoop();
        this.stop();
    }

    /**
     * Restarts the WebApi, SessionListenerFactory and SessionWatchdog
     */
    public void restart() {
        shutdown();

        // Try catch here to avoid multiple throws declarations along the call hierarchy
        try {
            start();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.error("Exception occured at WebApi startup: " + e);
            e.printStackTrace();
        }
    }

    /**
     * Restarts SessionWatchdog
     */
    public void restartSessionWatchdog() {
        watchdog.stopTimeoutLoop();
        watchdog.StartTimeoutLoop();
    }

    /**
     * Starts the WebApi, SessionListenerFactory, SessionWatchdog
     */
    @Override
    public void start() throws IOException {
        logger.info("Starting WebApi.");
        watchdog = new SessionWatchdog(sessionAdministration.getSessionStatuses());

        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        listenerFactory.startFactory();
        watchdog.StartTimeoutLoop();
    }

}
