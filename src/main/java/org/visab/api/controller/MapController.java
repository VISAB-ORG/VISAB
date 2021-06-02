package org.visab.api.controller;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.api.WebApi;
import org.visab.api.WebApiHelper;
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

    // Logger needs .class for each class to use for log traces
    private static Logger logger = LogManager.getLogger(MapController.class);

    @Override
    public Response handleGet(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
        return getNotFoundResponse(uriResource,
                "Get request are not supported when sending map images / map information!");
    }

    @Override
    public Response handlePost(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
        return receiveImage(session);
    }

    /**
     * Handler for reciving images.
     * 
     * @param httpSession The Http session
     * @return A Http response
     */
    private Response receiveImage(IHTTPSession httpSession) {
        var sessionId = WebApiHelper.extractSessionId(httpSession.getHeaders());
        var game = WebApiHelper.extractGame(httpSession.getHeaders());

        if (sessionId == null)
            return getBadRequestResponse("Either no sessionid given or could not parse uuid!");

        if (game == "")
            return getBadRequestResponse("No game given!");

        if (!AssignByGame.gameIsSupported(game))
            return getBadRequestResponse("Game is not supported!");

        if (!WebApi.getInstance().getTempThingy().isSessionActive(sessionId))
            return getBadRequestResponse("Session was closed!");

        var json = WebApiHelper.extractJsonBody(httpSession);
        if (json == "")
            return getBadRequestResponse("Failed receiving json from body. Did you not put it in the body?");

        WebApi.getInstance().getTempThingy().receiveImage(sessionId, game, json);

        return getOkResponse("Received Unity map images.");
    }
}
