package org.visab.workspace.config.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Pojo representing the DynamicMappings. Using these mappings, classes can be
 * deserialized or initialized by their fully classified class name.
 */
public class DynamicMapping {

    private List<MappingConfig> mappings = new ArrayList<>();

    /**
     * For deserialization
     */
    public DynamicMapping() {
    }

    public List<MappingConfig> getMappings() {
        return mappings;
    }

}
