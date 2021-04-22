package org.visab.api;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * A helper class containing various methods that are used by controllers.
 *
 * @author moritz
 *
 */
public final class WebApiHelper {

    public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static final <T> T deserializeObject(String json, Class<T> _class) {
	return gson.fromJson(json, _class);
    }

    public static final String extractGame(Map<String, String> headers) {
	return headers.containsKey("game") ? headers.get("game") : "";
    }

    public static final UUID extractSessionId(Map<String, String> headers) {
	return headers.containsKey("sessionid") ? tryParseUUID(headers.get("sessionid")) : null;
    }

    public static final Entry<UUID, String> extractSessionIdAndGame(Map<String, String> headers) {
	return Map.entry(extractSessionId(headers), extractGame(headers));
    }

    public static final String serializeObject(Object o) {
	return gson.toJson(o);
    }

    public static final UUID tryParseUUID(String UUIDString) {
	try {
	    return UUID.fromString(UUIDString);
	} catch (Exception e) {
	    return null;
	}
    }

}
