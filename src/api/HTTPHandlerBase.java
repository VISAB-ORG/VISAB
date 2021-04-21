package api;

import java.util.Map;

import org.nanohttpd.protocols.http.IHTTPSession;
import org.nanohttpd.protocols.http.response.Response;
import org.nanohttpd.protocols.http.response.Status;
import org.nanohttpd.router.RouterNanoHTTPD.UriResource;
import org.nanohttpd.router.RouterNanoHTTPD.UriResponder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class HTTPHandlerBase implements UriResponder {

    protected static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static Response getBadRequestResponse(String error) {
	return Response.newFixedLengthResponse(Status.BAD_REQUEST, "application/json",
		"400: BadRequest with error:[" + error + "]");
    }

    public static Response getForbiddenResponse(String error) {
	return Response.newFixedLengthResponse(Status.BAD_REQUEST, "application/json",
		"400: BadRequest with error:[" + error + "]");
    }

    protected static Response getJsonResponse(String json) {
	return Response.newFixedLengthResponse(Status.OK, Constant.JSON_MIME_TYPE, json);
    }

    protected static <T> Response getJsonResponse(T object) {
	var json = gson.toJson(object);

	return Response.newFixedLengthResponse(Status.OK, Constant.JSON_MIME_TYPE, json);
    }

    protected static Response getNotFoundResponse(UriResource uriResource) {
	var responseMessage = "Adress: [" + Constant.WEB_API_BASE_URL + Constant.API_PORT + "/" + uriResource.getUri()
		+ "] was not found.";

	return Response.newFixedLengthResponse(Status.NOT_FOUND, "text/html", responseMessage);
    }

    protected static Response getNotFoundResponse(UriResource uriResource, String additionalMessage) {
	var responseMessage = "Adress: [" + Constant.WEB_API_BASE_URL + Constant.API_PORT + "/" + uriResource.getUri()
		+ "] was not found." + "Additional info: [" + additionalMessage + "]";

	return Response.newFixedLengthResponse(Status.NOT_FOUND, "text/html", responseMessage);
    }

    public static Response getOkResponse(String message) {
	return Response.newFixedLengthResponse(Status.OK, "text/html",
		"200: Request Ok with message:[" + message + "]");
    }

    @Override
    public Response delete(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
	return getForbiddenResponse("Delete requests are forbidden!");
    }

    @Override
    public Response get(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
	return processGet(uriResource, urlParams, session);
    }

    @Override
    public Response other(String method, UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
	return getForbiddenResponse("Other requests are forbidden!");
    }

    @Override
    public Response post(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
	return processPost(uriResource, urlParams, session);
    }

    public abstract Response processGet(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session);

    public abstract Response processPost(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session);

    @Override
    public Response put(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
	return getForbiddenResponse("Put requests are forbidden!");
    }

}