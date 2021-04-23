package org.visab.util;

import java.util.UUID;

import org.visab.api.WebApiHelper;
import org.visab.processing.ISessionListener;
import org.visab.processing.IStatistics;
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

    public static final IStatistics getDeserializedStatistics(String json, String game) { // throws
											  // GameNotSupportedException
	switch (game) {
	case "CBRShooter":
	    return WebApiHelper.deserializeObject(json, CBRShooterStatistics.class);
	default:
	    return null;
	// throw new GameNotSupportedException(String.format("Game {1,string} is not
	// supported by VISAB yet.", game));
	}
    }

    public static final ISessionListener getListenerInstanceByGame(String game, UUID sessionId) {
	// TODO: Load the allowed game names at VISAB start from YAML file
	switch (game) {
	case "CBRShooter":
	    return new CBRShooterListener(game, sessionId);
	default:
	    // TODO: Raise exception
	    return null;
	}
    }
}
