package org.visab.dynamic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.globalmodel.IImageContainer;
import org.visab.globalmodel.IMetaInformation;
import org.visab.globalmodel.IStatistics;
import org.visab.globalmodel.IVISABFile;
import org.visab.globalmodel.starter.DefaultFile;
import org.visab.globalmodel.starter.DefaultImage;
import org.visab.globalmodel.starter.DefaultMetaInformation;
import org.visab.globalmodel.starter.DefaultStatistics;
import org.visab.util.JSONConvert;
import org.visab.util.NiceString;
import org.visab.workspace.Workspace;

/**
 * The DynamicSerializer used for deserializing json strings into java objects
 * with a classified class name. Used for deserializing into IStatistics,
 * IImageContainer, IMetaInformation and IVISABFile.
 */
public final class DynamicSerializer {

    private static final Logger logger = LogManager.getLogger(DynamicSerializer.class);

    /**
     * Deserializes a json string into a IMetaInformation instance.
     * 
     * @param json The json to deserialize
     * @return A IMetaInformation object is successful, null else
     */
    public static final IMetaInformation deserializeMetaInformation(String json) {
        var jsonObject = JSONConvert.deserializeJsonUnknown(json);

        var gameProperty = jsonObject.get("game");
        if (gameProperty == null)
            gameProperty = jsonObject.get("Game");

        if (gameProperty == null) {
            logger.error("Json had no field with name game. Cant deserialize it. Json:" + json);
            return null;
        }
        var game = gameProperty.asText();

        var className = "";

        var mapping = Workspace.getInstance().getConfigManager().getMapping(game);
        if (mapping != null && mapping.getMetaInformation() != null)
            className = mapping.getMetaInformation();

        IMetaInformation metaInformation = null;
        if (className.isBlank()) {
            metaInformation = new DefaultMetaInformation(game, json);
        } else {
            metaInformation = DynamicSerializer.<IMetaInformation>tryDeserialize(className, json);
        }

        return metaInformation;
    }

    /**
     * Deserialize a json string into a IStatistics instance.
     * 
     * @param json The json to deserialize
     * @param game The game for which to deserialize a file
     * @return An IStatistics object if successful, null else
     */
    public static final IStatistics deserializeStatistics(String json, String game) {
        var className = "";

        var mapping = Workspace.getInstance().getConfigManager().getMapping(game);
        if (mapping != null && mapping.getStatistics() != null)
            className = mapping.getStatistics();

        IStatistics statistics = null;
        if (className.isBlank()) {
            statistics = new DefaultStatistics(json);
        } else {
            statistics = DynamicSerializer.<IStatistics>tryDeserialize(className, json);
        }

        return statistics;
    }

    /**
     * Deserialize a json string into a IImage instance.
     * 
     * @param json The json to deserialize
     * @param game The game for which to deserialize a image
     * @return An IImage object if successful, null else
     */
    public static final IImageContainer deserializeImage(String json, String game) {
        var className = "";

        var mapping = Workspace.getInstance().getConfigManager().getMapping(game);
        if (mapping != null && mapping.getImage() != null)
            className = mapping.getImage();

        IImageContainer image = null;
        if (className.isBlank()) {
            image = new DefaultImage(json);
        } else {
            image = DynamicSerializer.<IImageContainer>tryDeserialize(className, json);
        }

        return image;
    }

    /**
     * Deserialize a json string into a IVISABFile instance.
     * 
     * @param json The json to deserialize
     * @param game The game for which to deserialize a file
     * @return An IVISABFile object if successful, null else
     */
    public static final IVISABFile deserializeVISABFile(String json, String game) {
        var className = "";

        var mapping = Workspace.getInstance().getConfigManager().getMapping(game);
        if (mapping != null && mapping.getFile() != null)
            className = mapping.getFile();

        if (className.isBlank())
            return JSONConvert.deserializeJson(json, DefaultFile.class, JSONConvert.UnforgivingMapper);
        else
            return DynamicSerializer.<IVISABFile>tryDeserialize(className, json);
    }

    /**
     * Attempts to deserialize a json string into an object of class T.
     * 
     * @param <T>       The type to deserialize into
     * @param className The fully classified class name of the type
     * @param json      The json to deserialize
     * @return An object of type T if successful, null else
     */
    @SuppressWarnings("unchecked")
    private static final <T> T tryDeserialize(String className, String json) {
        T instance = null;
        if (className != null && !className.isBlank()) {
            var _class = DynamicHelper.tryGetClass(className);
            if (_class == null) {
                logger.error("Failed to resolve class for " + className);
            } else {
                try {
                    var concreteClass = (Class<T>) _class;
                    instance = JSONConvert.deserializeJson(json, concreteClass, JSONConvert.UnforgivingMapper);
                } catch (Exception e) {
                    logger.error(NiceString.make("Failed to cast {0} to Class<T>.", className));
                }
            }
        }

        return instance;
    }

}
