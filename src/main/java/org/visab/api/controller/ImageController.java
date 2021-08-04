package org.visab.api.controller;

import java.util.Map;

import org.visab.api.WebAPI;
import org.visab.api.WebAPIHelper;
import org.visab.workspace.Workspace;

import org.nanohttpd.protocols.http.IHTTPSession;
import org.nanohttpd.protocols.http.response.Response;
import org.nanohttpd.router.RouterNanoHTTPD.UriResource;

/**
 * Controller for reciving images from an active transmission session.
 */
public class ImageController extends HTTPControllerBase {

    @Override
    public Response handleGet(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
        return getNotFoundResponse(uriResource, "Get request are not supported when sending images!");
    }

    @Override
    public Response handlePost(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
        return receiveImage(session);
    }

    /**
     * Deserializes an IImageContainer object from the json body of the given HTTP
     * session.
     * 
     * @param httpSession The HTTP session whose body contains the IImageContainer json.
     * @return A HTTP response
     */
    private Response receiveImage(IHTTPSession httpSession) {
        var sessionId = WebAPIHelper.extractSessionId(httpSession.getHeaders());
        var game = WebAPIHelper.extractGame(httpSession.getHeaders());

        if (sessionId == null)
            return getBadRequestResponse("Either no sessionid given or could not parse uuid!");

        if (game == "")
            return getBadRequestResponse("No game given!");

        if (!Workspace.getInstance().getConfigManager().isGameAllowed(game))
            return getBadRequestResponse("Game is not supported!");

        if (!WebAPI.getInstance().getSessionAdministration().isSessionActive(sessionId))
            return getBadRequestResponse(WebAPI.SESSION_ALREADY_CLOSED_RESPONSE);

        var json = WebAPIHelper.extractJsonBody(httpSession);
        if (json == "")
            return getBadRequestResponse("Failed receiving json from body. Did you not put it in the body?");

        WebAPI.getInstance().getSessionAdministration().receiveImage(sessionId, game, json);

        return getOkResponse("Images received.");
    }
}
