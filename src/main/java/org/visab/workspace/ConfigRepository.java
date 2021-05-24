package org.visab.repository;

import org.visab.workspace.config.model.DynamicMapping;

public class ConfigRepository extends RepositoryBase {

    public ConfigRepository(String configPath) {
        super(configPath);
    }

    public DynamicMapping loadMapping(String name) {
        return null;
    }

}
