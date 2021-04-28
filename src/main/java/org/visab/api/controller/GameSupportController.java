package org.visab.api.controller;

import java.util.Map;

import org.visab.util.Settings;

import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.router.RouterNanoHTTPD.UriResource;

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
