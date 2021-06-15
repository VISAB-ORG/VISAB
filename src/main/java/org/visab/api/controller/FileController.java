package org.visab.api.controller;

import java.util.Map;
import java.util.UUID;

import org.visab.api.WebApiHelper;
import org.visab.dynamic.DynamicSerializer;
import org.visab.globalmodel.BasicVISABFile;
import org.visab.processing.SessionListenerAdministration;
import org.visab.util.JsonConvert;
import org.visab.util.StringFormat;
import org.visab.workspace.Workspace;

import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.router.RouterNanoHTTPD.UriResource;

public class FileController extends HTTPControllerBase {

    @Override
    public Response handleGet(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
        var endpointAdress = uriResource.getUri().replace("file/", "");

        if (endpointAdress.equals("get"))
            return sendFile(session);
        else
            return getNotFoundResponse(uriResource);
    }

    private Response receiveFile(IHTTPSession session) {
        var json = WebApiHelper.extractJsonBody(session);
        if (json == "")
            return getBadRequestResponse("Failed receiving file from body. Did you not put it in the body?");

        var file = JsonConvert.deserializeJson(json, BasicVISABFile.class);
        if (file == null)
            return getBadRequestResponse("The file sent did not match the VISAB format.");

        var concreteFile = DynamicSerializer.deserializeVISABFile(json, file.getGame());
        if (concreteFile == null)
            return getBadRequestResponse(StringFormat.niceString(
                    "The sent file of game {0} could not be deserialized into the corresponding IVISABFile type.",
                    file.getGame()));

        var saved = Workspace.getInstance().getDatabaseManager().saveFile(concreteFile, file.getGame());

        return getOkResponse(saved ? "Sent file was saved!" : "File received, but failed to save!");
    }

    private Response sendFile(IHTTPSession session) {
        var parameters = session.getParameters();

        UUID sessionId = null;
        if (parameters.containsKey("sessionid") && parameters.get("sessionid").size() > 0)
            sessionId = WebApiHelper.tryParseUUID(parameters.get("sessionid").get(0));

        if (!parameters.containsKey("sessionid"))
            return getBadRequestResponse("No sessionid given in url parameters!");

        if (sessionId == null)
            return getBadRequestResponse("Could not parse uuid!");

        if (SessionListenerAdministration.getSessionListener(sessionId) != null)
            return getBadRequestResponse(
                    "The session for the given sessionId is stil active and therefore no file was saved yet. Try querying it again later.");

        var file = Workspace.getInstance().getDatabaseManager().loadSessionFile(sessionId);
        if (file == null)
            return getBadRequestResponse("During this VISAB runtime, no file was saved for the given sessionId.");

        return getJsonResponse(file);
    }

    @Override
    public Response handlePost(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
        var endpointAdress = uriResource.getUri().replace("file/", "");

        if (endpointAdress.equals("send"))
            return receiveFile(session);
        else
            return getNotFoundResponse(uriResource);
    }

}
