package org.visab.dynamic;

import java.util.HashMap;
import java.util.Map;

import org.visab.generalmodelchangeme.IStatistics;
import org.visab.generalmodelchangeme.IVISABFile;
import org.visab.generalmodelchangeme.starter.DefaultFile;
import org.visab.generalmodelchangeme.starter.DefaultImage;
import org.visab.generalmodelchangeme.starter.DefaultStatistics;
import org.visab.processing.IImage;
import org.visab.util.JsonConvert;

public class DynamicSerializer {

    // TODO: This is loaded from the workspace (saved under ViewMapping or a
    // different name)
    public Map<String, String> statisticsMap = new HashMap<>();
    public Map<String, String> visabFileMap = new HashMap<>();
    public Map<String, String> imageMap = new HashMap<>();

    public DynamicSerializer() {
    }

    public IVISABFile deserializeVISABFile(String json, String game) {
        var className = visabFileMap.getOrDefault(game, "");

        IVISABFile visabFile = null;
        if (className.isBlank()) {
            visabFile = (IVISABFile) JsonConvert.deserializeJson(json, DefaultFile.class);
        } else {
            visabFile = this.<IVISABFile>tryDeserialize(className, json);
        }

        return visabFile;
    }

    public IImage deserializeImage(String json, String game) {
        var className = imageMap.getOrDefault(game, "");

        IImage image = null;
        if (className.isBlank()) {
            image = new DefaultImage(game, json);
        } else {
            image = this.<IImage>tryDeserialize(className, json);
        }

        return image;
    }

    public IStatistics deserializeStatistics(String json, String game) {
        var className = statisticsMap.getOrDefault(game, "");

        IStatistics statistics = null;
        if (className.isBlank()) {
            statistics = new DefaultStatistics(game, json);
        } else {
            statistics = this.<IStatistics>tryDeserialize(className, json);
        }

        return statistics;
    }

    @SuppressWarnings("unchecked")
    private <T> T tryDeserialize(String className, String json) {
        T instance = null;
        if (!className.isBlank()) {
            var _class = tryGetClass(className);

            if (_class != null)
                instance = (T) JsonConvert.deserializeJson(json, _class);
        }

        return instance;
    }

    private static Class<?> tryGetClass(String className) {
        Class<?> _class = null;

        try {
            _class = Class.forName(className);
        } catch (ClassNotFoundException e) {
            // TODO: Log this
            e.printStackTrace();
        }

        return _class;
    }

    public static void main(String[] args) {
        var dyna = new DynamicSerializer();
        dyna.statisticsMap.put("CBRShooter", "org.visab.generalmodelchangeme.cbrshooter.CBRShooterStatistics");

        var json = "{\"creationDate\" : [ 2021, 5, 10, 18, 13, 52, 770199300 ],\"game\" : \"CBRShooter\"}";

        var stats = dyna.deserializeStatistics(json, "CBRShooter");

        stats.getGame();
    }
}
