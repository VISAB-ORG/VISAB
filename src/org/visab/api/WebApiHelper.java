package org.visab.api;

import java.util.Map;
import java.util.Map.Entry;
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

}
