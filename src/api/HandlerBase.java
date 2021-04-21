package api;

import java.util.Map;

import org.nanohttpd.protocols.http.IHTTPSession;
import org.nanohttpd.protocols.http.response.Response;
import org.nanohttpd.protocols.http.response.Status;
import org.nanohttpd.router.RouterNanoHTTPD.UriResource;
import org.nanohttpd.router.RouterNanoHTTPD.UriResponder;

public abstract class HandlerBase implements UriResponder {

    protected static Response forbiddenResponse = Response.newFixedLengthResponse(Status.FORBIDDEN, "text/html",
	    "HTTP Method forbidden by visab");

    public static Response getBadRequestResponse(String error) {
	return Response.newFixedLengthResponse(Status.BAD_REQUEST, "application/json",
		"400: BadRequest with error:[" + error + "]");
    }

    protected static Response getNotFoundResponse(UriResource uriResource) {
	var responseMessage = "Adress: [" + Constant.WEB_API_BASE_URL + Constant.API_PORT + "/" + uriResource.getUri()
		+ "] was not found.";
	return Response.newFixedLengthResponse(Status.NOT_FOUND, "text/html", responseMessage);
    }

    public static Response getOkResponse(String message) {
	return Response.newFixedLengthResponse(Status.OK, "text/html",
		"200: Request Ok with message:[" + message + "]");
    }

    protected static <T> Response getSerializedJsonResponse(T object) {

    }

    @Override
    public Response delete(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
	return forbiddenResponse;
    }

    @Override
    public Response get(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
	return processGet(uriResource, urlParams, session);
    }

    @Override
    public Response other(String method, UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
	return forbiddenResponse;
    }

    @Override
    public Response post(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
	return processPost(uriResource, urlParams, session);
    }

    public abstract Response processGet(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session);

    public abstract Response processPost(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session);

    @Override
    public Response put(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
	return forbiddenResponse;
    }

}