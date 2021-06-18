package org.visab.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.ResponseException;

/**
 * A helper class containing various methods that are used by controllers.
 *
 * @author moritz
 *
 */
public final class WebApiHelper {

    // Logger needs .class for each class to use for log traces
    private static Logger logger = LogManager.getLogger(WebApiHelper.class);

    /**
     * Is returned if session specific data like statistics or images were sent, but
     * the session was already closed.
     */
    public static final String SESSION_ALREADY_CLOSED_RESPONSE = "SESSION_ALREADY_CLOSED";

    /**
     * Extracts the game from the headers of a Http request.
     * 
     * @param headers The headers
     * @return The game if key was found in headers, "" else
     */
    public static final String extractGame(Map<String, String> headers) {
        return headers.containsKey("game") ? headers.get("game") : "";
    }

    /**
     * Extracts a json string from the body of a Http session.
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
     * Extracts the sessionId from the headers of a Http request.
     * 
     * @param headers The headers
     * @return The sessionId if key was found in headers, null else
     */
    public static final UUID extractSessionId(Map<String, String> headers) {
        return headers.containsKey("sessionid") ? tryParseUUID(headers.get("sessionid")) : null;
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
