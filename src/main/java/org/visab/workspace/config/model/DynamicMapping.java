package org.visab.workspace.config.model;

import java.util.ArrayList;
import java.util.List;

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
