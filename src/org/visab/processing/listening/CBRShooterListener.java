package org.visab.processing.listening;

import java.util.UUID;

import org.visab.processing.model.CBRShooterStatistics;

public class CBRShooterListener extends SessionListenerBase<CBRShooterStatistics> {

    public CBRShooterListener(String game, UUID sessionId) {
	super(game, sessionId);
    }

    @Override
    public void onSessionClosed() {
	// TODO Auto-generated method stub
    }

    @Override
    public void onSessionStarted() {
	// TODO Auto-generated method stub
    }

    @Override
    public void processStatistics(CBRShooterStatistics statistics) {
	// TODO Auto-generated method stub
    }

}
