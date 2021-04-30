package org.visab.util;

import java.util.UUID;

import org.visab.processing.ISessionListener;
import org.visab.processing.IStatistics;
import org.visab.processing.IVISABFile;
import org.visab.processing.VISABFileBase;
import org.visab.processing.cbrshooter.CBRShooterFile;
import org.visab.processing.cbrshooter.CBRShooterListener;
import org.visab.processing.cbrshooter.model.CBRShooterStatistics;

/**
 * Class where game (-string) specific instance creations are done. When adding
 * new SessionListeners or Statistics types, this class has to be extended.
 *
 * @author moritz
 *
 */
public final class AssignByGame {

    public static final String CBR_SHOOTER_STRING = "CBRShooter";
    public static final String SETTLERS_OF_CATAN_STRING = "SettlersOfCatan";

    public static final IStatistics getDeserializedStatistics(String json, String game) { // throws
        // GameNotSupportedException
        switch (game) {
            case CBR_SHOOTER_STRING:
                return JsonConvert.deserializeJson(json, CBRShooterStatistics.class);
            default:
                return null;
            // throw new GameNotSupportedException(String.format("Game {1,string} is not
            // supported by VISAB yet.", game));
        }
    }

    public static final ISessionListener getListenerInstanceByGame(String game, UUID sessionId) {
        // TODO: Load the allowed game names at VISAB start from YAML file
        switch (game) {
            case CBR_SHOOTER_STRING:
                return new CBRShooterListener(sessionId);
            default:
                // TODO: Raise exception
                return null;
        }
    }

    public static final IVISABFile getDeserializedFile(String json, String game) {
        switch (game) {
            case CBR_SHOOTER_STRING:
                return JsonConvert.deserializeJson(json, CBRShooterFile.class);
            default:
                return null;
        }
    }
}
