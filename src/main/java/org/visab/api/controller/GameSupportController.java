package org.visab.api.controller;

import java.util.Map;

import org.nanohttpd.protocols.http.IHTTPSession;
import org.nanohttpd.protocols.http.response.Response;
import org.nanohttpd.router.RouterNanoHTTPD.UriResource;
import org.visab.workspace.Workspace;

/**
 * Controller for getting a list of the games supported by VISAB.
 */
public class GameSupportController extends HTTPControllerBase {

    @Override
    public Response handleGet(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
        return getJsonResponse(Workspace.getInstance().getConfigManager().getAllowedGames());
    }

    @Override
    public Response handlePost(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
        return getForbiddenResponse("Post request are not supported for getting supported games!");
    }

}
