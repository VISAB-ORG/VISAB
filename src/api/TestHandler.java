package api;

import java.util.Map;

import org.nanohttpd.protocols.http.IHTTPSession;
import org.nanohttpd.protocols.http.response.Response;
import org.nanohttpd.protocols.http.response.Status;
import org.nanohttpd.router.RouterNanoHTTPD.UriResource;
import org.nanohttpd.router.RouterNanoHTTPD.UriResponder;

public class TestHandler implements UriResponder {

    private final Response errorResponse = Response.newFixedLengthResponse(Status.FORBIDDEN, "application/json",
	    "HTTP METHOD FORBIDDEN BY VISAB");

    private final String MIME_TYPE = Constant.MIME_TYPE;

    @Override
    public Response delete(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
	return errorResponse;
    }

    @Override
    public Response get(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
	var query = session.getParameters();
	System.out.println(query.get("test"));
	return Response.newFixedLengthResponse(Status.OK, MIME_TYPE, "GET REQUEST");
    }

    @Override
    public Response other(String method, UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
	return errorResponse;
    }

    @Override
    public Response post(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Response put(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
	return errorResponse;
    }

}