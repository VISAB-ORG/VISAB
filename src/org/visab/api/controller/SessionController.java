package org.visab.api.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.nanohttpd.protocols.http.IHTTPSession;
import org.nanohttpd.protocols.http.response.Response;
import org.nanohttpd.router.RouterNanoHTTPD.UriResource;
import org.visab.api.WebApiHelper;
import org.visab.api.model.SessionStatus;
import org.visab.eventbus.event.SessionClosedEvent;
import org.visab.eventbus.event.SessionOpenedEvent;
import org.visab.eventbus.publisher.PublisherBase;
import org.visab.util.VISABUtil;

/**
 * The SessionController, used for opening and closing VISAB transmission
 * sessions. Can also give information on session status and show currently
 * active sessions.
 *
 * @author moritz
 *
 */
public class SessionController extends HTTPControllerBase {

    /**
     * The CloseSessionPublisher, used for processing requests to close the session.
     * This has to be a nested class, due to implementing two generic
     * Interfaces/Classes not being allowed.
     *
     * @author moritz
     *
     */
    private class CloseSessionPublisher extends PublisherBase<SessionClosedEvent> {
	private Response closeSession(IHTTPSession httpSession) {
	    var sessionId = WebApiHelper.extractSessionId(httpSession.getHeaders());

	    if (sessionId == null)
		return getBadRequestResponse("Either no sessionid given or could not parse uuid!");

	    if (!activeSessions.containsKey(sessionId))
		return getBadRequestResponse("No Session with given sessionid in activeSessions!");

	    activeSessions.remove(sessionId);
	    // Publish the closed session to the EventBus
	    publish(new SessionClosedEvent(sessionId));

	    return getOkResponse("Closed the session!");
	}
    }

    /**
     * The OpenSessionPublisher, used for processing requests to open a session.
     * This has to be a nested class, due to implementing two generic
     * Interfaces/Classes not being allowed.
     *
     * @author moritz
     *
     */
    private class OpenSessionPublisher extends PublisherBase<SessionOpenedEvent> {
	private Response openSession(IHTTPSession httpSession) {
	    var sessionId = WebApiHelper.extractSessionId(httpSession.getHeaders());
	    var game = WebApiHelper.extractGame(httpSession.getHeaders());

	    if (sessionId == null)
		return getBadRequestResponse("Either no sessionid given or could not parse uuid!");

	    if (game == "")
		return getBadRequestResponse("No game given!");

	    if (!VISABUtil.gameIsSupported(game))
		return getBadRequestResponse("Game is not supported!");

	    if (activeSessions.containsKey(sessionId))
		return getBadRequestResponse("Session already active!");

	    activeSessions.put(sessionId, game);
	    // Publish the new session to the EventBus
	    publish(new SessionOpenedEvent(sessionId, game));

	    return getOkResponse("Session added.");
	}
    }

    private static Map<UUID, String> activeSessions = new HashMap<>();

    /**
     * Getter for the currently active transmission sessions
     *
     * @return A Map of the currently active transmission sessions and their
     *         respective games
     */
    public static Map<UUID, String> getActiveSessions() {
	return activeSessions;
    }

    private CloseSessionPublisher closeSessionPublisher = new CloseSessionPublisher();

    private OpenSessionPublisher openSessionPublisher = new OpenSessionPublisher();

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
	if (!activeSessions.containsKey(sessionId))
	    sessionStatus = new SessionStatus(false, sessionId, null);
	else
	    sessionStatus = new SessionStatus(true, sessionId, activeSessions.get(sessionId));

	return getJsonResponse(sessionStatus);
    }

    @Override
    public Response handleGet(UriResource uriResource, Map<String, String> urlParams, IHTTPSession httpSession) {
	var endpointAdress = uriResource.getUri().replace("session/", "");
	switch (endpointAdress) {
	case "open":
	    return openSessionPublisher.openSession(httpSession);

	case "close":
	    return closeSessionPublisher.closeSession(httpSession);

	case "status":
	    return getSessionStatus(httpSession);

	case "list":
	    return getJsonResponse(activeSessions);

	default:
	    return getNotFoundResponse(uriResource);
	}
    }

    @Override
    public Response handlePost(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
	return getNotFoundResponse(uriResource, "Post request are not supported for session handeling!");
    }

}
