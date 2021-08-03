package org.visab.api;

import java.io.IOException;
import java.net.Inet4Address;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.api.controller.FileController;
import org.visab.api.controller.GameSupportController;
import org.visab.api.controller.ImageController;
import org.visab.api.controller.SessionController;
import org.visab.api.controller.StatisticsController;
import org.visab.processing.SessionListenerFactory;
import org.visab.util.NiceString;
import org.visab.workspace.Workspace;

import org.nanohttpd.protocols.http.NanoHTTPD;
import org.nanohttpd.router.RouterNanoHTTPD;

/**
 * The HTTP-Protocol based WebAPI that can be used by game clients. Most
 * importantly used for reciving images and statistics of games.
 */
public class WebAPI extends RouterNanoHTTPD {

    /**
     * Should be returned if session specific data like statistics or images were sent, but
     * the session was already closed.
     */
    public static final String SESSION_ALREADY_CLOSED_RESPONSE = "SESSION_ALREADY_CLOSED";

    // Logger needs .class for each class to use for log traces
    private static Logger logger = LogManager.getLogger(WebAPI.class);

    private SessionListenerFactory listenerFactory = new SessionListenerFactory();
    private SessionAdministration sessionAdministration = new SessionAdministration();
    private SessionWatchdog watchdog;

    private static WebAPI instance;

    /**
     * Gets the singelton instance
     * 
     * @return The instance
     */
    public static WebAPI getInstance() {
        if (instance == null)
            instance = new WebAPI();

        return instance;
    }

    private WebAPI() {
        super(Workspace.getInstance().getConfigManager().getWebApiPort());
        addMappings();
    }

    /**
     * Adds the controllers for the endpoints.
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

    /**
     * The SessionAdministration of the WebApi instance.
     */
    public SessionAdministration getSessionAdministration() {
        return this.sessionAdministration;
    }

    /**
     * Shutsdown the WebApi, SessionListenerFactory and SessionWatchdog.
     */
    public void shutdown() {
        logger.info("Stopping WebApi.");
        listenerFactory.stopFactory();
        watchdog.stopTimeoutLoop();

        // Close all active sessions
        for (var status : sessionAdministration.getActiveSessionStatuses()) {
            sessionAdministration.closeSession(status.getSessionId());
        }

        this.stop();
    }

    /**
     * Restarts the WebApi, SessionListenerFactory and SessionWatchdog.
     */
    public void restart() {
        shutdown();

        // Try catch here to avoid multiple throws declarations along the call hierarchy
        try {
            start();
        } catch (IOException e) {
            logger.error("Exception occured at WebApi startup: " + e);
            e.printStackTrace();
        }
    }

    /**
     * Starts the WebApi, SessionListenerFactory, SessionWatchdog.
     */
    @Override
    public void start() throws IOException {
        logger.info(NiceString.make("Starting WebApi on {0}:{1}.", Inet4Address.getLocalHost().getHostAddress(),
                Workspace.getInstance().getConfigManager().getWebApiPort()));
        watchdog = new SessionWatchdog(sessionAdministration.getSessionStatuses());

        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        listenerFactory.startFactory();
        watchdog.StartTimeoutLoop();
    }

}
