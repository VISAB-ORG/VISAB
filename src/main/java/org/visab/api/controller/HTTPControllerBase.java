package org.visab.api.controller;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.nanohttpd.protocols.http.IHTTPSession;
import org.nanohttpd.protocols.http.response.Response;
import org.nanohttpd.protocols.http.response.Status;
import org.nanohttpd.router.RouterNanoHTTPD.UriResource;
import org.nanohttpd.router.RouterNanoHTTPD.UriResponder;
import org.visab.util.JSONConvert;

/**
 * The HTTPControllerBass class that all concrete controllers should inherit
 * from.
 */
public abstract class HTTPControllerBase implements UriResponder {

    protected static final String JSON_MIME_TYPE = "application/json";

    /**
     * A log4j logger instance.
     */
    protected Logger logger = LogManager.getLogger(this.getClass());

    /**
     * Gets a generic Ok (Code 200) response.
     * 
     * @param message The message to put in the body
     * @return A HTTP response
     */
    public static final Response getOkResponse(String message) {
        return Response.newFixedLengthResponse(Status.OK, JSON_MIME_TYPE, message);
    }

    /**
     * Gets a generic BadRequest (Code 400) response.
     * 
     * @param error The error to put in the response body
     * @return A HTTP response
     */
    protected static final Response getBadRequestResponse(String error) {
        return Response.newFixedLengthResponse(Status.BAD_REQUEST, JSON_MIME_TYPE, error);
    }

    /**
     * Gets a generic Forbidden (Code 403) response.
     * 
     * @param error The error to put in the response body
     * @return A HTTP response
     */
    protected static final Response getForbiddenResponse(String error) {
        return Response.newFixedLengthResponse(Status.FORBIDDEN, JSON_MIME_TYPE, error);
    }

    /**
     * Gets a generic NotFound (Code 404) response for the given uri resource.
     * 
     * @param uriResource The uriResource that wasnt found
     * @return A HTTP response
     */
    protected static final Response getNotFoundResponse(UriResource uriResource) {
        var responseMessage = "Adress: " + uriResource.getUri() + " was not found.";

        return Response.newFixedLengthResponse(Status.NOT_FOUND, JSON_MIME_TYPE, responseMessage);
    }

    /**
     * Gets a generic NotFound (Code 404) response for the given uri resource.
     * 
     * @param uriResource       The uriResource that wasnt found
     * @param additionalMessage An additional message
     * @return A HTTP response
     */
    protected static final Response getNotFoundResponse(UriResource uriResource, String additionalMessage) {
        var responseMessage = "Adress: " + uriResource.getUri() + " was not found. Additional message: "
                + additionalMessage;

        return Response.newFixedLengthResponse(Status.NOT_FOUND, "text/html", responseMessage);
    }

    /**
     * Gets a generic json response.
     * 
     * @param o The object serialize as json and put into the response body
     * @return A HTTP response
     */
    protected static final Response getJsonResponse(Object o) {
        return Response.newFixedLengthResponse(Status.OK, JSON_MIME_TYPE, JSONConvert.serializeObject(o));
    }

    /**
     * Gets a generic json response.
     * 
     * @param json The json string to put into the response body
     * @return A HTTP response
     */
    protected static final Response getJsonResponse(String json) {
        return Response.newFixedLengthResponse(Status.OK, JSON_MIME_TYPE, json);
    }

    /**
     * Handler for HTTP GET requests.
     * 
     * @param uriResource The UriResource
     * @param urlParams   The url parameters
     * @param session     The HTTP session
     * @return A HTTP response
     */
    public abstract Response handleGet(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session);

    /**
     * Handler for HTTP POST requests.
     * 
     * @param uriResource The uri resource
     * @param urlParams   The url parameters
     * @param session     The HTTP session
     * @return A HTTP response
     */
    public abstract Response handlePost(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session);

    @Override
    public Response get(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
        return handleGet(uriResource, urlParams, session);
    }

    @Override
    public Response post(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
        return handlePost(uriResource, urlParams, session);
    }

    @Override
    public Response delete(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
        return getForbiddenResponse("Delete requests are forbidden!");
    }

    @Override
    public Response other(String method, UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
        return getForbiddenResponse("Other requests are forbidden!");
    }

    @Override
    public Response put(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
        return getForbiddenResponse("Put requests are forbidden!");
    }

}