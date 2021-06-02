package org.visab.api.controller;

import java.util.Map;

import org.visab.util.JsonConvert;
import org.visab.util.SystemSettings;
import org.visab.util.UserSettings;

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

    /**
     * Gets a generic BadRequest (Code 400) Respose.
     * 
     * @param error The error to put in the body
     * @return The Http response
     */
    protected static final Response getBadRequestResponse(String error) {
        return NanoHTTPD.newFixedLengthResponse(Status.BAD_REQUEST, "application/json",
                "400: BadRequest with error: [" + error + "].");
    }

    /**
     * Gets a generic Forbidden (Code 403) Respose.
     * 
     * @param error The error to put in the body
     * @return The Http response
     */
    protected static final Response getForbiddenResponse(String error) {
        return NanoHTTPD.newFixedLengthResponse(Status.FORBIDDEN, "application/json",
                "403: BadRequest with error: [" + error + "].");
    }

    /**
     * Gets a generic JSON Response.
     * 
     * @param o The object to sent as json
     * @return The Http response
     */
    protected static final Response getJsonResponse(Object o) {
        return NanoHTTPD.newFixedLengthResponse(Status.OK, SystemSettings.JSON_MIME_TYPE, JsonConvert.serializeObject(o));
    }

    /**
     * Gets a generic JSON Response.
     * 
     * @param json The json to sent
     * @return The Http response
     */
    protected static final Response getJsonResponse(String json) {
        return NanoHTTPD.newFixedLengthResponse(Status.OK, SystemSettings.JSON_MIME_TYPE, json);
    }

    /**
     * TODO: This should be using settings instead. As of now it is also wrong.
     * 
     * Gets a generic NotFound (Code 404) Respose.
     * 
     * @param uriResource The uriResource that wasnt found
     * @return The Http response
     */
    protected static final Response getNotFoundResponse(UriResource uriResource) {
        var responseMessage = "404: Adress: " + UserSettings.getWebApiHostName() + "/" + uriResource.getUri()
                + " was not found.";

        return NanoHTTPD.newFixedLengthResponse(Status.NOT_FOUND, "text/html", responseMessage);
    }

    /**
     * TODO: This should be using settings instead. As of now it is also wrong.
     * 
     * Gets a generic NotFound (Code 404) Respose.
     * 
     * @param uriResource       The uriResource that wasnt found
     * @param additionalMessage An additional message
     * @return The Http response
     */
    protected static final Response getNotFoundResponse(UriResource uriResource, String additionalMessage) {
        var responseMessage = "Adress: " + UserSettings.getWebApiHostName() + "/" + uriResource.getUri()
                + " was not found." + "Additional info: [" + additionalMessage + "]";

        return NanoHTTPD.newFixedLengthResponse(Status.NOT_FOUND, "text/html", responseMessage);
    }

    /**
     * Gets a generic Ok (Code 200) Respose.
     * 
     * @param message The message to put in the body
     * @return The Http response
     */
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

    /**
     * Handler for Http GET requests.
     * 
     * @param uriResource The UriResource
     * @param urlParams   The url parameters
     * @param session     The Http session
     * @return A Http response
     */
    public abstract Response handleGet(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session);

    /**
     * Handler for Http POST requests.
     * 
     * @param uriResource The UriResource
     * @param urlParams   The url parameters
     * @param session     The Http session
     * @return A Http response
     */
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