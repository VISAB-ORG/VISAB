package org.visab.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Class for serializing objects and deserializing json files.
 *
 * @author moritz
 *
 */
public final class JsonConvert {

    // Logger needs .class for each class to use for log traces
    private static Logger logger = LogManager.getLogger(JsonConvert.class);

    public static final ObjectMapper mapper = new ObjectMapper()
            .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
            .enable(SerializationFeature.INDENT_OUTPUT)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).registerModule(new JavaTimeModule());

    /**
     * Deserializes a Json string into an object of given class.
     *
     * @param <T>      The type of the class to deserialize into
     * @param json     The json to deserialize
     * @param outClass The class to deserialize into
     * @return The deserialized object, null if deserialization failed
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
     * Deserializes a Json string into an object of the given type. This method can
     * be used if you want to deserialize into a generic type. E.g. List<String>.
     * 
     * @param <T>  The type to deserialize into
     * @param json The json to deserialize
     * @param type The type reference to the generic type.
     * @return The deserialized object, null if deserialization failed
     */
    public static final <T> T deserializeJson(String json, TypeReference<T> type) {
        try {
            return mapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Serializes a given object into a Json string.
     *
     * @param o the object to serialize
     * @return A json string representation of the object, empty string if serialization
     *         failed
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
