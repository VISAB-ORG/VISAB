package org.visab.api.controller;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.nanohttpd.protocols.http.IHTTPSession;
import org.nanohttpd.protocols.http.response.Response;
import org.nanohttpd.router.RouterNanoHTTPD.UriResource;
import org.visab.api.WebApi;
import org.visab.api.WebApiHelper;
import org.visab.workspace.Workspace;

/**
 * The statistics controller, used for transmitting statistics data.
 *
 * @author moritz
 *
 */
public class StatisticsController extends HTTPControllerBase {

    // Logger needs .class for each class to use for log traces
    private static Logger logger = LogManager.getLogger(StatisticsController.class);

    @Override
    public final Response handleGet(UriResource uriResource, Map<String, String> urlParams, IHTTPSession httpSession) {
        return getNotFoundResponse(uriResource, "Get request are not supported when sending statistics!");
    }

    @Override
    public Response handlePost(UriResource uriResource, Map<String, String> urlParams, IHTTPSession httpSession) {
        return receiveStatistics(httpSession);
    }

    /**
     * Handler for reciving statistics.
     * 
     * @param httpSession The Http session
     * @return A Http response
     */
    private Response receiveStatistics(IHTTPSession httpSession) {
        var sessionId = WebApiHelper.extractSessionId(httpSession.getHeaders());
        var game = WebApiHelper.extractGame(httpSession.getHeaders());

        if (sessionId == null)
            return getBadRequestResponse("Either no sessionid given or could not parse uuid!");

        if (!WebApi.getInstance().getSessionAdministration().isSessionActive(sessionId))
            return getBadRequestResponse("Session was already closed!" + WebApiHelper.SESSION_ALREADY_CLOSED_RESPONSE);

        if (game == "")
            return getBadRequestResponse("No game given in headers!");

        if (!Workspace.getInstance().getConfigManager().isGameSupported(game))
            return getBadRequestResponse("Game is not supported!");

        var json = WebApiHelper.extractJsonBody(httpSession);
        if (json == "")
            return getBadRequestResponse("Failed receiving json from body. Did you not put it in the body?");

        WebApi.getInstance().getSessionAdministration().receiveStatistics(sessionId, game, json);

        return getOkResponse("Statistics received.");
    }

}
