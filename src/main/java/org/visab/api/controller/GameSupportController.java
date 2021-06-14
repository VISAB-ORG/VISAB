package org.visab.api.controller;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.workspace.Workspace;

import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.router.RouterNanoHTTPD.UriResource;

/**
 * Api Controller for getting the games supported by VISAB.
 *
 * @author moritz
 *
 */
public class GameSupportController extends HTTPControllerBase {
    // Logger needs .class for each class to use for log traces
    private static Logger logger = LogManager.getLogger(GameSupportController.class);

    @Override
    public Response handleGet(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
        return getJsonResponse(Workspace.getInstance().getConfigManager().getAllowedGames());
    }

    @Override
    public Response handlePost(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
        return getForbiddenResponse("Post request are not supported for getting supported games!");
    }

}
