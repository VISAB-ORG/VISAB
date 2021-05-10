package org.visab.util;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.processing.IMapImage;
import org.visab.processing.ISessionListener;
import org.visab.processing.IStatistics;
import org.visab.processing.cbrshooter.CBRShooterFile;
import org.visab.processing.cbrshooter.CBRShooterListener;
import org.visab.processing.cbrshooter.model.CBRShooterStatistics;
import org.visab.repository.IVISABFile;

/**
 * Class responsible for game (-string) based class instantiation. When adding
 * new SessionListeners or Statistics types, this class has to be extended.
 * Methods in this class return the abstract interface of the instantiated
 * objects.
 *
 * TODO: Add default implementations
 * 
 * @author moritz
 *
 */
public final class AssignByGame {

    // Logger needs .class for each class to use for log traces
    private static Logger logger = LogManager.getLogger(AssignByGame.class);

    public static final List<String> ALLOWED_GAMES = new ArrayList<>() {
        {
            add(CBR_SHOOTER_STRING);
        }
    };

    public static final String CBR_SHOOTER_STRING = "CBRShooter";
    public static final String SETTLERS_OF_CATAN_STRING = "SettlersOfCatan";

    public static final boolean gameIsSupported(String game) {
        return ALLOWED_GAMES.contains(game);
    }

    /**
     * Creates a visab file object based on the given json data and the game.
     *
     * @param json The json data to fill the object with
     * @param game The game
     * @return The visab file object
     */
    public static final IVISABFile getDeserializedFile(String json, String game) {
        switch (game) {
        case CBR_SHOOTER_STRING:
            return JsonConvert.deserializeJson(json, CBRShooterFile.class);
        default:
            return null;
        }
    }

    /**
     * Creates an statistics object based on json data and the game.
     *
     * @param json The json data to fill the object with
     * @param game The game
     * @return The statistics object
     */
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

    /**
     * TODO: instantiate by game Creates an map image object based on json data and
     * the game.
     *
     * @param json The json data to fill the object with
     * @param game The game
     * @return The statistics object
     */
    public static final IMapImage getDeserializedMapImage(String json, String game) {
        switch (game) {
        default:
            return null;
        }
    }

    /**
     * Creates a SessionListener based for a given game
     *
     * @param game      The game for which to create a SessionListener
     * @param sessionId The sessionId for the Listener to listen to
     * @return The SessionListener object
     */
    public static final ISessionListener<? extends IStatistics> getListenerInstanceByGame(String game, UUID sessionId) {
        switch (game) {
        case CBR_SHOOTER_STRING:
            return new CBRShooterListener(sessionId);
        default:
            // TODO: Raise exception
            return null;
        }
    }
}
