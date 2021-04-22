package api.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.nanohttpd.protocols.http.IHTTPSession;
import org.nanohttpd.protocols.http.NanoHTTPD.ResponseException;
import org.nanohttpd.protocols.http.response.Response;
import org.nanohttpd.router.RouterNanoHTTPD.UriResource;

import api.WebApi;
import api.WebApiHelper;
import eventbus.IPublisher;
import eventbus.event.StatisticsReceivedEvent;
import processing.IStatistics;
import processing.statistics.CBRShooterStatistics;
import org.visab.util.VisABUtil;

public class StatisticsController extends HTTPControllerBase implements IPublisher<StatisticsReceivedEvent> {

    private final IStatistics getDeserializedStatistics(String json, String game) { // throws GameNotSupportedException
										    // {
	switch (game) {
	case "CBRShooter":
	    return WebApiHelper.deserializeObject(json, CBRShooterStatistics.class);
	default:
	    return null;
	// throw new GameNotSupportedException(String.format("Game {1,string} is not
	// supported by VISAB yet.", game));
	}
    }

    @Override
    public final Response handleGet(UriResource uriResource, Map<String, String> urlParams, IHTTPSession httpSession) {
	return getNotFoundResponse(uriResource, "Get request are not supported when sending statistics!");
    }

    @Override
    public Response handlePost(UriResource uriResource, Map<String, String> urlParams, IHTTPSession httpSession) {
	var sessionId = WebApiHelper.extractSessionId(httpSession.getHeaders());
	var game = WebApiHelper.extractGame(httpSession.getHeaders());

	if (sessionId == null)
	    return getBadRequestResponse("Either no sessionid given or could not parse uuid!");

	// TODO: Uncomment if not testing anymore
//	if (!SessionController.getActiveSessions().containsKey(sessionId))
//	    return getBadRequestResponse("Tried to send statistics without having an active session!");

	if (game == "")
	    return getBadRequestResponse("No game given in headers!");

	if (!VisABUtil.gameIsSupported(game))
	    return getBadRequestResponse("Game is not supported!");

	var json = "";
	try {
	    var writeInto = new HashMap<String, String>();
	    httpSession.parseBody(writeInto);
	    json = writeInto.get("postBody");
	} catch (IOException | ResponseException e) {
	    e.printStackTrace();
	    return getBadRequestResponse("Failed receiving json from body. Did you not put it in the body?");
	}

	var event = new StatisticsReceivedEvent(sessionId, game, getDeserializedStatistics(json, game));
	publish(event);

	return getOkResponse("Statistics received.");
    }

    @Override
    public final void publish(StatisticsReceivedEvent event) {
	WebApi.getEventBus().publish(event);
    }

}
