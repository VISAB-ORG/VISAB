package org.visab.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Helper class for serializing objects and deserializing json files.
 * Essentially wraps around the ObjectMapper of the jackson library.
 */
public final class JSONConvert {

    /**
     * Fails deserialization on
     * 
     * 1. JSON contains more properties than there are setters in the POJO. 2. POJO
     * contains more setters, than there are properties in the JSON.
     */
    public static final ObjectMapper UnforgivingMapper = new ObjectMapper()
            .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
            .enable(SerializationFeature.INDENT_OUTPUT)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
            .configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, true)
            .registerModule(new JavaTimeModule());

    public static final ObjectMapper ForgivingMapper = new ObjectMapper()
            .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
            .enable(SerializationFeature.INDENT_OUTPUT)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false)
            .registerModule(new JavaTimeModule());

    /**
     * Deserializes a Json string into an object of given class type.
     *
     * @param <T>      The type of the class to deserialize into
     * @param json     The json to deserialize
     * @param outClass The class to deserialize into
     * @return The deserialized object, null if deserialization failed
     */
    public static final <T> T deserializeJson(String json, Class<T> outClass, ObjectMapper mapper) {
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
    public static final <T> T deserializeJson(String json, TypeReference<T> type, ObjectMapper mapper) {
        try {
            return mapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static final JsonNode deserializeJsonUnknown(String json) {
        try {
            return ForgivingMapper.readTree(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Serializes a given object into a JSON string.
     *
     * @param o the object to serialize
     * @return A json string representation of the object, empty string if
     *         serialization failed
     */
    public static final String serializeObject(Object o) {
        try {
            return ForgivingMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

}
