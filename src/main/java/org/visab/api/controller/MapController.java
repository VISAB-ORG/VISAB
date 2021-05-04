package org.visab.api.controller;

import java.util.Map;

import org.visab.api.SessionWatchdog;
import org.visab.api.WebApiHelper;
import org.visab.eventbus.event.MapImageReceivedEvent;
import org.visab.eventbus.event.MapInformationReceivedEvent;
import org.visab.eventbus.publisher.PublisherBase;
import org.visab.util.AssignByGame;

import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.router.RouterNanoHTTPD.UriResource;

/**
 * The map controller, used for transmitting data for displaying unity maps in
 * VISABs GUI.
 *
 * @author moritz
 *
 */
public class MapController extends HTTPControllerBase {

    /**
     * The map image publisher, that handles requests for sending map images.
     */
    private class MapImagePublisher extends PublisherBase<MapImageReceivedEvent> {
        private Response receiveMapImage(IHTTPSession httpSession) {
            var sessionId = WebApiHelper.extractSessionId(httpSession.getHeaders());
            var game = WebApiHelper.extractGame(httpSession.getHeaders());

            if (sessionId == null)
                return getBadRequestResponse("Either no sessionid given or could not parse uuid!");

            if (game == "")
                return getBadRequestResponse("No game given!");

            if (!AssignByGame.gameIsSupported(game))
                return getBadRequestResponse("Game is not supported!");

            if (!SessionWatchdog.isSessionActive(sessionId))
                return getBadRequestResponse("Session was closed!");

            var json = WebApiHelper.extractJsonBody(httpSession);
            if (json == "")
                return getBadRequestResponse("Failed receiving json from body. Did you not put it in the body?");

            var event = new MapImageReceivedEvent(sessionId, game, AssignByGame.getDeserializedMapImage(json, game));
            publish(event);

            return getOkResponse("Received Unity map images.");
        }
    }

    /**
     * The MapInformationPublisher, that handles requests for sending map
     * information.
     */
    private class MapInformationPublisher extends PublisherBase<MapInformationReceivedEvent> {
        private Response receiveMapInformation(IHTTPSession httpSession) {
            var sessionId = WebApiHelper.extractSessionId(httpSession.getHeaders());
            var game = WebApiHelper.extractGame(httpSession.getHeaders());

            if (sessionId == null)
                return getBadRequestResponse("Either no sessionid given or could not parse uuid!");

            if (game == "")
                return getBadRequestResponse("No game given!");

            if (!AssignByGame.gameIsSupported(game))
                return getBadRequestResponse("Game is not supported!");

            if (!SessionWatchdog.isSessionActive(sessionId))
                return getBadRequestResponse("Session was closed!");

            var json = WebApiHelper.extractJsonBody(httpSession);
            if (json == "")
                return getBadRequestResponse("Failed receiving json from body. Did you not put it in the body?");

            var event = new MapInformationReceivedEvent(sessionId, game,
                    AssignByGame.getDeserializedMapInformation(json, game));
            publish(event);

            return getOkResponse("Received Unity map information.");
        }
    }

    private MapImagePublisher mapImagePublisher = new MapImagePublisher();

    private MapInformationPublisher mapInfoPublisher = new MapInformationPublisher();

    @Override
    public Response handleGet(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
        return getNotFoundResponse(uriResource,
                "Get request are not supported when sending map images / map information!");
    }

    @Override
    public Response handlePost(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
        var endpointAdress = uriResource.getUri().replace("map/", "");
        switch (endpointAdress) {
        case "image":
            return mapImagePublisher.receiveMapImage(session);

        case "information":
            return mapInfoPublisher.receiveMapInformation(session);

        default:
            return getNotFoundResponse(uriResource);
        }
    }
}
