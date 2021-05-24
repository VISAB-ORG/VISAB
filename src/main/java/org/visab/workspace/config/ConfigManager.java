package org.visab.workspace.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.visab.util.VISABUtil;
import org.visab.workspace.ConfigRepository;
import org.visab.workspace.Workspace;
import org.visab.workspace.config.model.DynamicMapping;
import org.visab.workspace.config.model.MappingConfig;

/**
 * The ConfigManager that is used for loading and modifying settings and dynamic
 * mappings.
 * 
 * TODO: Allowed games has to be added to settings.
 */
public class ConfigManager {

    public static final String CONFIG_PATH = VISABUtil.combinePath(Workspace.WORKSPACE_PATH, "config");

    private ConfigRepository repo = new ConfigRepository(CONFIG_PATH);

    private DynamicMapping mapping;

    public ConfigManager() {
        // TODO: Load settings first, so that they can be used for mapping
        // initialization. Important in case we decide to make it customizable where to
        // save your mappings.
        loadDynamicMapping();
    }

    public List<MappingConfig> getMappings() {
        return this.mapping.getMappings();
    }

    /**
     * Replaces an existing mapping with a new Mapping
     * 
     * @param game       The game to replace the mapping of
     * @param newMapping The new mapping
     * @return True if successful
     */
    public boolean replaceMapping(String game, MappingConfig newMapping) {
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

    /**
     * Gets the mapping for a given game
     * 
     * @param game The game to get the mapping for
     * @return The mapping if mappings contained it, null else
     */
    public MappingConfig getMapping(String game) {
        Optional<MappingConfig> optional = getMappings().stream().filter(x -> x.getGame().equals(game)).findFirst();

        if (!optional.isEmpty())
            return optional.get();
        return null;
    }

    /**
     * Adds a mapping to the mappings
     * 
     * @param newMapping The mapping to add
     * @return True if successful
     */
    public boolean addMapping(MappingConfig newMapping) {
        if (getMapping(newMapping.getGame()) == null) {
            getMappings().add(newMapping);

            return true;
        }

        return false;
    }

    /**
     * Removes a mapping by game from the mappings
     * 
     * @param game The game to remove the mapping for
     * @return True if successful
     */
    public boolean removeMapping(String game) {
        return getMappings().removeIf(x -> x.getGame().equals(game));
    }

    /**
     * Removes a mappign from the mappings.
     * 
     * @return True if successful
     */
    public boolean removeMapping(MappingConfig mapping) {
        return getMappings().remove(mapping);
    }

    /**
     * Loads the mappings from the filesystem using the repository.
     * 
     * @return True if successful
     */
    private boolean loadDynamicMapping() {
        // TODO: Load from file!
        var cbrMapping = new MappingConfig();
        cbrMapping.setGame("CBRShooter");
        cbrMapping.setListener("org.visab.processing.cbrshooter.CBRShooterListener");
        cbrMapping.setStatistics("org.visab.globalmodel.cbrshooter.CBRShooterStatistics");
        cbrMapping.setFile("org.visab.globalmodel.cbrshooter.CBRShooterFile");
        cbrMapping.setImage("org.visab.globalmodel.cbrshooter.CBRShooterMapImage");

        var mapping = new DynamicMapping();
        mapping.getMappings().add(cbrMapping);

        /*
         * var mapping = repo.loadMapping(null); if (mapping == null) { // TOOD: Raise
         * some exception or do error handeling } else { this.mapping = mapping; }
         */

        this.mapping = mapping;

        return mapping != null;
    }

}
