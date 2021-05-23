package org.visab.api.controller;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.api.SessionWatchdog;
import org.visab.api.WebApi;
import org.visab.api.WebApiHelper;
import org.visab.dynamic.DynamicSerializer;
import org.visab.eventbus.IPublisher;
import org.visab.eventbus.event.StatisticsReceivedEvent;
import org.visab.util.AssignByGame;

import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.router.RouterNanoHTTPD.UriResource;

/**
 * The statistics controller, used for transmitting statistics data. Publishes a
 * StatisticsReceivedEvent if the received sessionId belongs to an active
 * sessions.
 *
 * @author moritz
 *
 */
public class StatisticsController extends HTTPControllerBase implements IPublisher<StatisticsReceivedEvent> {

    // Logger needs .class for each class to use for log traces
    private static Logger logger = LogManager.getLogger(StatisticsController.class);

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

        if (!SessionWatchdog.isSessionActive(sessionId))
            return getBadRequestResponse("Session was already closed!");

        if (game == "")
            return getBadRequestResponse("No game given in headers!");

        if (!AssignByGame.gameIsSupported(game))
            return getBadRequestResponse("Game is not supported!");

        var json = WebApiHelper.extractJsonBody(httpSession);
        if (json == "")
            return getBadRequestResponse("Failed receiving json from body. Did you not put it in the body?");

        var event = new StatisticsReceivedEvent(sessionId, game,
                DynamicSerializer.deserializeStatistics(json, game));
        publish(event);

        return getOkResponse("Statistics received.");
    }

    @Override
    public final void publish(StatisticsReceivedEvent event) {
        WebApi.instance.getEventBus().publish(event);
    }

}
