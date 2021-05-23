package org.visab.api.controller;

import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.api.SessionWatchdog;
import org.visab.api.WebApi;
import org.visab.api.WebApiHelper;
import org.visab.api.model.SessionStatus;
import org.visab.util.AssignByGame;

import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.router.RouterNanoHTTPD.UriResource;

/**
 * The SessionController, used for opening and closing VISAB transmission
 * sessions through the SessionWatchdog. Can also give information on session
 * status and show currently active sessions.
 *
 * @author moritz
 *
 */
public class SessionController extends HTTPControllerBase {

    // Logger needs .class for each class to use for log traces
    private static Logger logger = LogManager.getLogger(SessionController.class);

    /**
     * Closes a transmission session.
     *
     * @param httpSession
     * @return
     */
    private Response closeSession(IHTTPSession httpSession) {
        var sessionId = WebApiHelper.extractSessionId(httpSession.getHeaders());

        if (sessionId == null)
            return getBadRequestResponse("Either no sessionid given or could not parse uuid!");

        if (!SessionWatchdog.isSessionActive(sessionId))
            return getOkResponse("Session was already closed!");

        WebApi.instance.getSessionWatchdog().closeSession(sessionId, false);

        return getOkResponse("Closed the session!");
    }

    /**
     * Gets the status of a given session.
     *
     * @param httpSession
     * @return
     */
    private Response getSessionStatus(IHTTPSession httpSession) {
        var parameters = httpSession.getParameters();

        UUID sessionId = null;
        if (parameters.containsKey("sessionid") && parameters.get("sessionid").size() > 0)
            sessionId = WebApiHelper.tryParseUUID(parameters.get("sessionid").get(0));

        if (!parameters.containsKey("sessionid"))
            return getBadRequestResponse("No sessionid given in url parameters!");

        if (sessionId == null)
            return getBadRequestResponse("Could not parse uuid!");

        SessionStatus sessionStatus;
        if (!SessionWatchdog.isSessionActive(sessionId))
            sessionStatus = new SessionStatus(false, sessionId, null);
        else
            sessionStatus = new SessionStatus(true, sessionId, SessionWatchdog.getGame(sessionId));

        return getJsonResponse(sessionStatus);
    }

    @Override
    public Response handleGet(UriResource uriResource, Map<String, String> urlParams, IHTTPSession httpSession) {
        var endpointAdress = uriResource.getUri().replace("session/", "");
        switch (endpointAdress) {
        case "open":
            return openSession(httpSession);

        case "close":
            return closeSession(httpSession);

        case "status":
            return getSessionStatus(httpSession);

        case "list":
            return getJsonResponse(SessionWatchdog.getActiveSessions());

        default:
            return getNotFoundResponse(uriResource);
        }
    }

    @Override
    public Response handlePost(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
        return getNotFoundResponse(uriResource, "Post request are not supported for session handeling!");
    }

    /**
     * Opens a new session.
     *
     * @param httpSession
     * @return
     */
    private Response openSession(IHTTPSession httpSession) {
        var sessionId = WebApiHelper.extractSessionId(httpSession.getHeaders());
        var game = WebApiHelper.extractGame(httpSession.getHeaders());

        if (sessionId == null)
            return getBadRequestResponse("Either no sessionid given or could not parse uuid!");

        if (game == "")
            return getBadRequestResponse("No game given!");

        if (!AssignByGame.gameIsSupported(game))
            return getBadRequestResponse("Game is not supported!");

        if (SessionWatchdog.isSessionActive(sessionId))
            return getBadRequestResponse("Session already active!");

        WebApi.instance.getSessionWatchdog().openSession(sessionId, game, httpSession.getRemoteIpAddress(),
                httpSession.getRemoteHostName());

        return getOkResponse("Session added.");
    }

}
