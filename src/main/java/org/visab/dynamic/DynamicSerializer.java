package org.visab.dynamic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.globalmodel.IImage;
import org.visab.globalmodel.IStatistics;
import org.visab.globalmodel.IVISABFile;
import org.visab.globalmodel.starter.DefaultFile;
import org.visab.globalmodel.starter.DefaultImage;
import org.visab.globalmodel.starter.DefaultStatistics;
import org.visab.util.JsonConvert;
import org.visab.workspace.Workspace;

/**
 * The DynamicSerializer used for deserializing json strings into java objects
 * of a classified class name. Used for deserializing statistics, images and
 * VISAB files.
 */
public final class DynamicSerializer {

    private static Logger logger = LogManager.getLogger(DynamicSerializer.class);

    /**
     * Deserialize a json string into a VISAB file.
     * 
     * @param json The json to deserialize
     * @param game The game for which to deserialize a file
     * @return An IVISABFile object if successful, throws exception if not
     *         successful
     */
    public static IVISABFile deserializeVISABFile(String json, String game) {
        var className = "";

        var mapping = Workspace.instance.getConfigManager().getMapping(game);
        if (mapping != null && mapping.getFile() != null)
            className = mapping.getFile();

        IVISABFile visabFile = null;
        if (className.isBlank()) {
            visabFile = (IVISABFile) JsonConvert.deserializeJson(json, DefaultFile.class);
        } else {
            visabFile = DynamicSerializer.<IVISABFile>tryDeserialize(className, json);
        }

        return visabFile;
    }

    /**
     * Deserialize a json string into a IImage.
     * 
     * @param json The json to deserialize
     * @param game The game for which to deserialize a image
     * @return An IImage object if successful, throws exception if not successful
     */
    public static IImage deserializeImage(String json, String game) {
        var className = "";

        var mapping = Workspace.instance.getConfigManager().getMapping(game);
        if (mapping != null && mapping.getImage() != null)
            className = mapping.getImage();

        IImage image = null;
        if (className.isBlank()) {
            image = new DefaultImage(game, json);
        } else {
            image = DynamicSerializer.<IImage>tryDeserialize(className, json);
        }

        return image;
    }

    /**
     * Deserialize a json string into a IStatistics.
     * 
     * @param json The json to deserialize
     * @param game The game for which to deserialize a file
     * @return An IStatistics object if successful, throws exception if not
     *         successful
     */
    public static IStatistics deserializeStatistics(String json, String game) {
        var className = "";

        var mapping = Workspace.instance.getConfigManager().getMapping(game);
        if (mapping != null && mapping.getStatistics() != null)
            className = mapping.getStatistics();

        IStatistics statistics = null;
        if (className.isBlank()) {
            statistics = new DefaultStatistics(game, json);
        } else {
            statistics = DynamicSerializer.<IStatistics>tryDeserialize(className, json);
        }

        return statistics;
    }

    /**
     * Attempts to deserialize a json string into an object of class T.
     * 
     * @param <T>       The type to deserialize into
     * @param className The fully classified class name of the type
     * @param json      The json to deserialize
     * @return An object of type T if successful, throws exception else
     */
    @SuppressWarnings("unchecked")
    private static <T> T tryDeserialize(String className, String json) {
        T instance = null;
        if (!className.isBlank()) {
            var _class = tryGetClass(className);

            if (_class != null)
                instance = (T) JsonConvert.deserializeJson(json, _class);
        }

        return instance;
    }

    /**
     * Tries to get a Class<?> object for a given class name.
     * 
     * @param className The fully classified class name
     * @return The Class<?> object if successful, null else
     */
    private static Class<?> tryGetClass(String className) {
        Class<?> _class = null;

        try {
            _class = Class.forName(className);
        } catch (ClassNotFoundException e) {
            logger.fatal("Failed to find class for name {0}", className);
            e.printStackTrace();
        }

        return _class;
    }

    public static void main(String[] args) {
        var dyna = new DynamicSerializer();
        var json = "{\"creationDate\" : [ 2021, 5, 10, 18, 13, 52, 770199300 ],\"game\" : \"CBRShooter\"}";

        var stats = dyna.deserializeStatistics(json, "CBRShooter");

        stats.getGame();
    }
}
