package api;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.nanohttpd.protocols.http.response.IStatus;
import org.nanohttpd.router.RouterNanoHTTPD.DefaultHandler;

public class SessionHandler extends DefaultHandler {

    private static Map<UUID, String> activeSessions = new HashMap<>();

    public static Map<UUID, String> GetActiveSessions() {
	return activeSessions;
    }

    @Override
    public String getMimeType() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public IStatus getStatus() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public String getText() {
	// TODO Auto-generated method stub
	return null;
    }

}
