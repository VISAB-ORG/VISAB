package org.visab.api.controller;

import java.util.Map;

import org.nanohttpd.protocols.http.IHTTPSession;
import org.nanohttpd.protocols.http.response.Response;
import org.nanohttpd.router.RouterNanoHTTPD.UriResource;

/**
 * The map controller, used for transmitting data for displaying unity maps in
 * VISABs GUI.
 *
 * @author moritz
 *
 */
public class MapController extends HTTPControllerBase {

    @Override
    public Response handleGet(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Response handlePost(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
	// TODO Auto-generated method stub
	return null;
    }

}