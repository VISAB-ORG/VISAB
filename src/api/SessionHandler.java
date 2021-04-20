package api;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD.Response.IStatus;
import fi.iki.elonen.router.RouterNanoHTTPD.DefaultHandler;

import java.util.UUID;

public class SessionHandler extends DefaultHandler {

	private static Map activeSessions = new HashMap<UUID, String>();
	
	public static Map GetActiveSessions() {
		return activeSessions;
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

	@Override
	public String getMimeType() {
		// TODO Auto-generated method stub
		return null;
	}

}
