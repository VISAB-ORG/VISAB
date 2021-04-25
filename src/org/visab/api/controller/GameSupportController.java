package org.visab.api.controller;

import java.util.Map;

import org.nanohttpd.protocols.http.IHTTPSession;
import org.nanohttpd.protocols.http.response.Response;
import org.nanohttpd.router.RouterNanoHTTPD.UriResource;
import org.visab.util.Settings;

/**
 * Controller for getting the games supported by VISAB
 *
 * @author moritz
 *
 */
public class GameSupportController extends HTTPControllerBase {

    @Override
    public Response handleGet(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
	return getJsonResponse(Settings.ALLOWED_GAMES);
    }

    @Override
    public Response handlePost(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
	return getForbiddenResponse("Post request are not supported for getting supported games!");
    }

}
