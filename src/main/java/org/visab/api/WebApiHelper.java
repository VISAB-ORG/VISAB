package org.visab.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.ResponseException;

import java.util.UUID;

/**
 * A helper class containing various methods that are used by controllers.
 *
 * @author moritz
 *
 */
public final class WebApiHelper {

    public static final String extractGame(Map<String, String> headers) {
        return headers.containsKey("game") ? headers.get("game") : "";
    }

    public static final UUID extractSessionId(Map<String, String> headers) {
        return headers.containsKey("sessionid") ? tryParseUUID(headers.get("sessionid")) : null;
    }

    public static final Entry<UUID, String> extractSessionIdAndGame(Map<String, String> headers) {
        return Map.entry(extractSessionId(headers), extractGame(headers));
    }

    public static final UUID tryParseUUID(String UUIDString) {
        try {
            return UUID.fromString(UUIDString);
        } catch (Exception e) {
            return null;
        }
    }

    public static final String extractJsonBody(IHTTPSession session) {
        var json = "";
        try {
            var writeInto = new HashMap<String, String>();
            session.parseBody(writeInto);
            json = writeInto.get("postData");
        } catch (IOException | ResponseException e) {
            e.printStackTrace();
            json = "";
        }

        return json;
    }

}
