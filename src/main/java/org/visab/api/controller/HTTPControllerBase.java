package org.visab.api.controller;

import java.util.Map;

import org.visab.util.JsonConvert;
import org.visab.util.Settings;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.NanoHTTPD.Response.Status;
import fi.iki.elonen.router.RouterNanoHTTPD.UriResource;
import fi.iki.elonen.router.RouterNanoHTTPD.UriResponder;

/**
 * The base controller that all new controllers should inherit from.
 *
 * @author moritz
 *
 */
public abstract class HTTPControllerBase implements UriResponder {

    protected static final Response getBadRequestResponse(String error) {
	return NanoHTTPD.newFixedLengthResponse(Status.BAD_REQUEST, "application/json",
		"400: BadRequest with error:[" + error + "]");
    }

    protected static final Response getForbiddenResponse(String error) {
	return NanoHTTPD.newFixedLengthResponse(Status.BAD_REQUEST, "application/json",
		"400: BadRequest with error:[" + error + "]");
    }

    protected static final Response getJsonResponse(Object o) {
	return NanoHTTPD.newFixedLengthResponse(Status.OK, Settings.JSON_MIME_TYPE, JsonConvert.serializeObject(o));
    }

    protected static final Response getJsonResponse(String json) {
	return NanoHTTPD.newFixedLengthResponse(Status.OK, Settings.JSON_MIME_TYPE, json);
    }

    protected static final Response getNotFoundResponse(UriResource uriResource) {
	var responseMessage = "Adress: [" + Settings.WEB_API_BASE_ADDRESS + Settings.API_PORT + "/"
		+ uriResource.getUri() + "] was not found.";

	return NanoHTTPD.newFixedLengthResponse(Status.NOT_FOUND, "text/html", responseMessage);
    }

    protected static final Response getNotFoundResponse(UriResource uriResource, String additionalMessage) {
	var responseMessage = "Adress: [" + Settings.WEB_API_BASE_ADDRESS + Settings.API_PORT + "/"
		+ uriResource.getUri() + "] was not found." + "Additional info: [" + additionalMessage + "]";

	return NanoHTTPD.newFixedLengthResponse(Status.NOT_FOUND, "text/html", responseMessage);
    }

    public static final Response getOkResponse(String message) {
	return NanoHTTPD.newFixedLengthResponse(Status.OK, "text/html",
		"200: Request Ok with message:[" + message + "]");
    }

    @Override
    public Response delete(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
	return getForbiddenResponse("Delete requests are forbidden!");
    }

    @Override
    public Response get(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
	return handleGet(uriResource, urlParams, session);
    }

    public abstract Response handleGet(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session);

    public abstract Response handlePost(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session);

    @Override
    public Response other(String method, UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
	return getForbiddenResponse("Other requests are forbidden!");
    }

    @Override
    public Response post(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
	return handlePost(uriResource, urlParams, session);
    }

    @Override
    public Response put(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
	return getForbiddenResponse("Put requests are forbidden!");
    }

}