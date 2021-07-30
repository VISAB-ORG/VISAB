package org.visab.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.nanohttpd.protocols.http.IHTTPSession;
import org.nanohttpd.protocols.http.NanoHTTPD.ResponseException;

/**
 * A helper class containing various methods that are used by the HTTP
 * controllers.
 */
public final class WebAPIHelper {

    // Logger needs .class for each class to use for log traces
    private static final Logger logger = LogManager.getLogger(WebAPIHelper.class);

    /**
     * Extracts the game from the headers of a HTTP request.
     * 
     * @param headers The headers of the HTTP request
     * @return The game if key was found in headers, "" else
     */
    public static final String extractGame(Map<String, String> headers) {
        return headers.containsKey("game") ? headers.get("game") : "";
    }

    /**
     * Extracts the sessionId from the headers of a HTTP request.
     * 
     * @param headers The headers of the HTTP request
     * @return The sessionId if key was found in headers, null else
     */
    public static final UUID extractSessionId(Map<String, String> headers) {
        return headers.containsKey("sessionid") ? tryParseUUID(headers.get("sessionid")) : null;
    }

    /**
     * Extracts a json string from the body of a HTTP session.
     * 
     * @param session The session to get the body of
     * @return The json string if successful, "" else
     */
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

    /**
     * Parses a UUID from a given string.
     * 
     * @param UUIDString The string to parse
     * @return The UUID if successfully parsed, null else
     */
    public static final UUID tryParseUUID(String UUIDString) {
        try {
            return UUID.fromString(UUIDString);
        } catch (Exception e) {
            return null;
        }
    }

}
