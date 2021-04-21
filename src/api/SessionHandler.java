package api;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.nanohttpd.protocols.http.IHTTPSession;
import org.nanohttpd.protocols.http.response.Response;
import org.nanohttpd.router.RouterNanoHTTPD.UriResource;

import api.model.SessionStatus;
import eventbus.event.SessionClosedEvent;
import eventbus.event.SessionOpenedEvent;
import eventbus.publisher.PublisherBase;

public class SessionHandler extends HTTPHandlerBase {

    private class CloseSessionPublisher extends PublisherBase<SessionClosedEvent> {
	private Response closeSession(IHTTPSession httpSession) {
	    var headers = httpSession.getHeaders();

	    UUID sessionId = null;
	    if (headers.containsKey("sessionId"))
		sessionId = tryParseUUID(headers.get("sessionId"));

	    if (sessionId == null)
		return getBadRequestResponse("Either no sessionId given or could not parse uuid!");

	    if (!activeSessions.containsKey(sessionId))
		return getBadRequestResponse("No Session with given sessionId in activeSessions!");

	    activeSessions.remove(sessionId);
	    // Publish the closed session to the EventBus
	    publish(new SessionClosedEvent(sessionId));

	    return getOkResponse("Closed the session!");
	}
    }

    private class OpenSessionPublisher extends PublisherBase<SessionOpenedEvent> {
	private Response openSession(IHTTPSession httpSession) {
	    var headers = httpSession.getHeaders();

	    UUID sessionId = null;
	    var game = "";

	    if (headers.containsKey("sessionId"))
		sessionId = tryParseUUID(headers.get("sessionId"));

	    if (headers.containsKey("game"))
		game = headers.get("game");

	    if (sessionId == null)
		return getBadRequestResponse("Either no sessionId given or could not parse uuid!");

	    if (game == "")
		return getBadRequestResponse("No game given!");

	    if (!Constant.ALLOWED_GAMES.contains(game))
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

    public static Map<UUID, String> getActiveSessions() {
	return activeSessions;
    }

    private static UUID tryParseUUID(String UUIDString) {
	try {
	    return UUID.fromString(UUIDString);
	} catch (Exception e) {
	    return null;
	}
    }

    private CloseSessionPublisher closeSessionPublisher = new CloseSessionPublisher();

    private OpenSessionPublisher openSessionPublisher = new OpenSessionPublisher();

    private Response getSessionStatus(Map<String, String> urlParams) {
	UUID sessionId = null;

	if (urlParams.containsKey("sessionId"))
	    sessionId = tryParseUUID(urlParams.get("sessionId"));

	if (!urlParams.containsKey("sessionId"))
	    return getBadRequestResponse("No sessionId given in url parameters!");

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
    public Response processGet(UriResource uriResource, Map<String, String> urlParams, IHTTPSession httpSession) {
	var endpointAdress = uriResource.getUri().replace("session/", "");
	switch (endpointAdress) {
	case "open":
	    return openSessionPublisher.openSession(httpSession);

	case "close":
	    return closeSessionPublisher.closeSession(httpSession);

	case "status":
	    return getSessionStatus(urlParams);

	case "list":
	    return getJsonResponse(activeSessions);

	default:
	    return getNotFoundResponse(uriResource);
	}
    }

    @Override
    public Response processPost(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
	return getNotFoundResponse(uriResource, "Post request are not supported for session handeling!");
    }

}
