package org.visab.util;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.generalmodelchangeme.IStatistics;
import org.visab.generalmodelchangeme.IVISABFile;
import org.visab.generalmodelchangeme.cbrshooter.CBRShooterFile;
import org.visab.generalmodelchangeme.cbrshooter.CBRShooterMapImage;
import org.visab.generalmodelchangeme.cbrshooter.CBRShooterStatistics;
import org.visab.generalmodelchangeme.starter.DefaultFile;
import org.visab.generalmodelchangeme.starter.DefaultStatistics;
import org.visab.processing.IImage;
import org.visab.processing.ISessionListener;
import org.visab.processing.cbrshooter.CBRShooterListener;
import org.visab.processing.starter.DefaultSessionListener;

/**
 * Class responsible for game (-string) based class instantiation. When adding
 * new SessionListeners or Statistics types, this class has to be extended.
 * Methods in this class return the abstract interface of the instantiated
 * objects.
 *
 * TODO: Add default implementations TODO: Try catch return null on deployment
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
