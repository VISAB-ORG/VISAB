package org.visab.api.controller;

import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.api.WebApi;
import org.visab.api.WebApiHelper;
import org.visab.dynamic.DynamicSerializer;
import org.visab.util.StringFormat;
import org.visab.workspace.Workspace;

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
     * Handler for closing a transmission session.
     * 
     * @param httpSession The Http session
     * @return A Http response
     */
    private Response closeSession(IHTTPSession httpSession) {
        var sessionId = WebApiHelper.extractSessionId(httpSession.getHeaders());

        var responseMessage = "Closed session successfully: ";
        if (sessionId == null) {
            responseMessage = "Either no sessionid given or could not parse uuid, cannot close session!";
            logger.error(responseMessage);
            return getBadRequestResponse("Either no sessionid given or could not parse uuid!");
        }

        if (!WebApi.getInstance().getSessionAdministration().isSessionActive(sessionId)) {
            responseMessage = "Session: " + sessionId + " is already closed!";
            logger.error(responseMessage);
            return getOkResponse(responseMessage);
        }

        WebApi.getInstance().getSessionAdministration().closeSession(sessionId);

        logger.info(responseMessage + sessionId);
        return getOkResponse(responseMessage + sessionId);
    }

    /**
     * Handler for getting transmission session status.
     * 
     * @param httpSession The Http session
     * @return A Http response
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

        var sessionStatus = WebApi.getInstance().getSessionAdministration().getStatus(sessionId);
        if (sessionStatus == null)
            return getJsonResponse("");
        else
            return getJsonResponse(sessionStatus);
    }

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
            return getJsonResponse(WebApi.getInstance().getSessionAdministration().getActiveSessionStatuses());

        default:
            return getNotFoundResponse(uriResource);
        }
    }

    @Override
    public Response handlePost(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
        return getNotFoundResponse(uriResource, "Post request are not supported for session handeling!");
    }

    /**
     * Handler for opening a new tranmission session.
     * 
     * @param httpSession The Http session
     * @return A Http response
     */
    private Response openSession(IHTTPSession httpSession) {
        logger.info("Trying to open a session for the VISAB WebApi.");

        var json = WebApiHelper.extractJsonBody(httpSession);
        if (json == "")
            return getBadRequestResponse("Failed receiving json from body. Did you not put it in the body?");

        var metaInformation = DynamicSerializer.deserializeMetaInformation(json);
        if (metaInformation == null || metaInformation.getGame() == null || metaInformation.getGame().isBlank()) {
            var responseMessage = StringFormat.niceString("Meta information {0} was invalid. Wont start session.",
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

        var success = WebApi.getInstance().getSessionAdministration().openSession(newSessionId, metaInformation,
                httpSession.getRemoteIpAddress(), httpSession.getRemoteHostName());

        logger.info(StringFormat.niceString("Opened session with ID '{0}'"));
        return getJsonResponse(newSessionId);
    }

}
