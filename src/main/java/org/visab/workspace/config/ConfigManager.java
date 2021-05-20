package org.visab.workspace.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.visab.repository.ConfigRepository;
import org.visab.workspace.config.model.DynamicMapping;
import org.visab.workspace.config.model.MappingConfig;

public class ConfigManager {

    private ConfigRepository repo;

    private DynamicMapping mapping;

    public ConfigManager() {
        // TODO: Load settings first, so that they can be used for mapping
        // initialization. Important in case we decide to make it customizable where to
        // save your mappings.
        loadMapping();
    }

    public List<MappingConfig> getMappings() {
        return this.mapping.getMappings();
    }

    public boolean changeMapping(String game, MappingConfig newMapping) {
        var mappingCopy = new ArrayList<MappingConfig>(mapping.getMappings());
        for (int i = 0; i < mappingCopy.size(); i++) {
            var mapping = mappingCopy.get(i);

            if (mapping.getGame().equals(newMapping.getGame())) {
                getMappings().set(i, newMapping);

                return true;
            }
        }

        return false;
    }

    public MappingConfig getMapping(String game) {
        Optional<MappingConfig> optional = getMappings().stream().filter(x -> x.getGame().equals(game)).findFirst();

        if (!optional.isEmpty())
            return optional.get();
        return null;
    }

    public boolean addMapping(MappingConfig newMapping) {
        if (getMapping(newMapping.getGame()) == null) {
            getMappings().add(newMapping);

            return true;
        }

        return false;
    }

    public boolean removeMapping(String game) {
        return getMappings().removeIf(x -> x.getGame().equals(game));
    }

    public boolean removeMapping(MappingConfig mapping) {
        return getMappings().remove(mapping);
    }

    private boolean loadMapping() {
        var mapping = repo.loadMapping(null);
        if (mapping == null) {
            // TOOD: Raise some exception or do error handeling
        } else {
            this.mapping = mapping;
        }

        return mapping != null;
    }

}
