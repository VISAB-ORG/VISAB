package org.visab.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Class for serializing and deserializing objects / json files.
 *
 * @author moritz
 *
 */
public final class JsonConvert {

    // Logger needs .class for each class to use for log traces
    private static Logger logger = LogManager.getLogger(JsonConvert.class);

    public static final ObjectMapper mapper = new ObjectMapper()
            .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
            .enable(SerializationFeature.INDENT_OUTPUT);

    /**
     * Deserializes a Json string into an object of given class.
     *
     * @param <T>      The type of the class to deserialize into
     * @param json     The json to deserialize
     * @param outClass The class to deserialize into
     * @return The deserialized object, null deserialization failed
     */
    public static final <T> T deserializeJson(String json, Class<T> outClass) {
        try {
            return mapper.readValue(json, outClass);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Serializes a given object into a Json string.
     *
     * @param o the object to serialize
     * @return A json representation of the object, empty string if serialization
     *         fails
     */
    public static final String serializeObject(Object o) {
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

}
