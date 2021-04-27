package org.visab.api.controller;

import java.time.Duration;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.nanohttpd.protocols.http.IHTTPSession;
import org.nanohttpd.protocols.http.response.Response;
import org.nanohttpd.router.RouterNanoHTTPD.UriResource;
import org.visab.api.TransmissionSessionAdministration;
import org.visab.api.WebApi;
import org.visab.api.WebApiHelper;
import org.visab.api.model.SessionStatus;
import org.visab.eventbus.event.SessionClosedEvent;
import org.visab.eventbus.event.SessionOpenedEvent;
import org.visab.eventbus.event.StatisticsReceivedEvent;
import org.visab.eventbus.publisher.PublisherBase;
import org.visab.eventbus.subscriber.SubscriberBase;
import org.visab.util.Settings;
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

			if (!TransmissionSessionAdministration.isSessionActive(sessionId))
				return getOkResponse("Session was already closed!");

			TransmissionSessionAdministration.removeSession(sessionId);
			// Publish the closed session to the EventBus
			publishEvent(sessionId);

			return getOkResponse("Closed the session!");
		}

		private void publishEvent(UUID sessionId) {
			publish(new SessionClosedEvent(sessionId, false));
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

			if (TransmissionSessionAdministration.isSessionActive(sessionId))
				return getBadRequestResponse("Session already active!");

			TransmissionSessionAdministration.addSession(sessionId, game);
			// Publish the new session to the EventBus
			publish(new SessionOpenedEvent(sessionId, game));

			return getOkResponse("Session added.");
		}
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
		if (!TransmissionSessionAdministration.isSessionActive(sessionId))
			sessionStatus = new SessionStatus(false, sessionId, null);
		else
			sessionStatus = new SessionStatus(true, sessionId, TransmissionSessionAdministration.getGame(sessionId));

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
			return getJsonResponse(TransmissionSessionAdministration.getActiveSessions());

		default:
			return getNotFoundResponse(uriResource);
		}
	}

	@Override
	public Response handlePost(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
		return getNotFoundResponse(uriResource, "Post request are not supported for session handeling!");
	}

}
