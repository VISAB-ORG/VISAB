package org.visab.api.controller;

import java.util.Map;

import org.visab.eventbus.publisher.PublisherBase;

import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.router.RouterNanoHTTPD.UriResource;

/**
 * The map controller, used for transmitting data for displaying unity maps in
 * VISABs GUI.
 *
 * @author moritz
 *
 */
public class MapController extends HTTPControllerBase {

    private class MapImagePublisher extends PublisherBase<MapImageReceivedEvent> {
        
    }

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
