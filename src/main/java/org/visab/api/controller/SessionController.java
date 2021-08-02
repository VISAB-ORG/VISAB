package org.visab.api.controller;

import java.util.Map;
import java.util.UUID;

import org.nanohttpd.protocols.http.IHTTPSession;
import org.nanohttpd.protocols.http.response.Response;
import org.nanohttpd.router.RouterNanoHTTPD.UriResource;
import org.visab.api.WebAPI;
import org.visab.api.WebAPIHelper;
import org.visab.dynamic.DynamicSerializer;
import org.visab.util.NiceString;
import org.visab.workspace.Workspace;

/**
 * Controller for opening and closing VISAB transmission sessions and getting
 * the status for a transmission session.
 */
public class SessionController extends HTTPControllerBase {

    @Override
    public Response handleGet(UriResource uriResource, Map<String, String> urlParams, IHTTPSession httpSession) {
        var endpointAdress = uriResource.getUri().replace("session/", "");

        // Decide handlers based on uri
        switch (endpointAdress) {
        case "open":
            return openSession(httpSession);

        case "close":
            return closeSession(httpSession);

        case "status":
            return getSessionStatus(httpSession);

        case "list":
            return getJsonResponse(WebAPI.getInstance().getSessionAdministration().getActiveSessionStatuses());

        default:
            return getNotFoundResponse(uriResource);
        }
    }

    @Override
    public Response handlePost(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
        var endpointAdress = uriResource.getUri().replace("session/", "");

        if (endpointAdress.equals("open"))
            return openSession(session);
        else
            return getNotFoundResponse(uriResource);
    }

    /**
     * Opens a transmission session by deserializing a IMetaInformation object from
     * the json body.
     * 
     * @param httpSession The HTTP session whose body contains the IMetaInformation
     *                    json.
     * @return A HTTP response
     */
    private Response openSession(IHTTPSession httpSession) {
        logger.info("Trying to open a session for the VISAB WebApi.");

        var json = WebAPIHelper.extractJsonBody(httpSession);
        if (json == "")
            return getBadRequestResponse("Failed receiving json from body. Did you not put it in the body?");

        var metaInformation = DynamicSerializer.deserializeMetaInformation(json);
        if (metaInformation == null || metaInformation.getGame() == null || metaInformation.getGame().isBlank()) {
            var responseMessage = NiceString.make("Meta information {0} was invalid. Wont start session.",
                    metaInformation);
            logger.info(responseMessage);
            return getBadRequestResponse(responseMessage);
        }

        if (!Workspace.getInstance().getConfigManager().isGameSupported(metaInformation.getGame())) {
            var responseMessage = "Given game name '" + metaInformation.getGame()
                    + "' is not supported, cannot open session.";
            logger.error(responseMessage);
            return getBadRequestResponse(responseMessage);
        }

        // Generate new UUID to use for identifying the session
        var newSessionId = UUID.randomUUID();

        WebAPI.getInstance().getSessionAdministration().openSession(newSessionId, metaInformation,
                httpSession.getRemoteIpAddress());

        logger.info(NiceString.make("Opened session with ID '{0}'", newSessionId));
        return getJsonResponse(newSessionId);
    }

    /**
     * Closes a transmission session by using the SessionAdministration.
     * 
     * @param httpSession The HTTP session whose header contains the sessionid of
     *                    the transmission session.
     * @return A HTTP response
     */
    private Response closeSession(IHTTPSession httpSession) {
        var sessionId = WebAPIHelper.extractSessionId(httpSession.getHeaders());

        var responseMessage = "Closed session successfully: ";
        if (sessionId == null) {
            responseMessage = "Either no sessionid given or could not parse uuid, cannot close session!";
            logger.error(responseMessage);
            return getBadRequestResponse("Either no sessionid given or could not parse uuid!");
        }

        if (!WebAPI.getInstance().getSessionAdministration().isSessionActive(sessionId)) {
            responseMessage = "Session: " + sessionId + " is already closed!";
            logger.error(responseMessage);
            return getOkResponse(responseMessage);
        }

        WebAPI.getInstance().getSessionAdministration().closeSession(sessionId);

        logger.info(responseMessage + sessionId);
        return getOkResponse(responseMessage + sessionId);
    }

    /**
     * Gets the status of a transmission session.
     * 
     * @param httpSession The HTTP session whose url parameters contain the
     *                    sessionid of the transmission session.
     * @return A HTTP response
     */
    private Response getSessionStatus(IHTTPSession httpSession) {
        var parameters = httpSession.getParameters();

        UUID sessionId = null;
        if (parameters.containsKey("sessionid") && parameters.get("sessionid").size() > 0)
            sessionId = WebAPIHelper.tryParseUUID(parameters.get("sessionid").get(0));

        if (!parameters.containsKey("sessionid"))
            return getBadRequestResponse("No sessionid given in url parameters!");

        if (sessionId == null)
            return getBadRequestResponse("Could not parse uuid!");

        var sessionStatus = WebAPI.getInstance().getSessionAdministration().getStatus(sessionId);
        if (sessionStatus == null)
            return getJsonResponse("");
        else
            return getJsonResponse(sessionStatus);
    }

}
