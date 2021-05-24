package org.visab.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * TODO: Remove completely Class responsible for game (-string) based class
 * instantiation. When adding new SessionListeners or Statistics types, this
 * class has to be extended. Methods in this class return the abstract interface
 * of the instantiated objects.
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

}
