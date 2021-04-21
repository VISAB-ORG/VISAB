package processing;

import java.util.UUID;

public class CBRShooterListener extends SessionListener<CBRShooterStatistics> {

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
