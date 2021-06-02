package org.visab.newgui.webapi;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.api.WebApi;
import org.visab.eventbus.ApiEventBus;
import org.visab.eventbus.IApiEvent;
import org.visab.eventbus.ISubscriber;
import org.visab.eventbus.event.SessionClosedEvent;
import org.visab.eventbus.event.SessionOpenedEvent;
import org.visab.globalmodel.TransmissionSessionStatus;
import org.visab.newgui.DynamicViewLoader;
import org.visab.newgui.ViewModelBase;
import org.visab.newgui.webapi.model.SessionStatus;
import org.visab.util.StreamUtil;

import de.saxsys.mvvmfx.utils.commands.Command;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class WebApiViewModel extends ViewModelBase implements ISubscriber<IApiEvent> {

    private Logger logger = LogManager.getLogger(WebApiViewModel.class);

    private IntegerProperty activeTransmissionSessions = new SimpleIntegerProperty(0);

    private DoubleProperty requestPerSecond = new SimpleDoubleProperty(0.0);

    private ObservableList<SessionStatus> sessionList = FXCollections.observableArrayList();

    private ObjectProperty<SessionStatus> selectedSession = new SimpleObjectProperty<>();

    private Command closeSessionCommand;

    public Command closeSessionCommand() {
        if (closeSessionCommand == null) {
            closeSessionCommand = runnableCommand(() -> {
                if (selectedSession.get() != null && selectedSession.get().isActive())
                    WebApi.getInstance().getSessionWatchdog().closeSession(selectedSession.get().getSessionId(), false);
            });
        }

        return closeSessionCommand;
    }

    private Command openLiveViewCommand;

    public Command openLiveViewCommand() {
        if (openLiveViewCommand == null) {
            openLiveViewCommand = runnableCommand(() -> {
                if (selectedSession != null) {
                    var sessionInfo = selectedSession.get();

                    DynamicViewLoader.loadAndShowStatisticsViewLive(sessionInfo.getGame(), sessionInfo.getSessionId());
                }
            });
        }

        return openLiveViewCommand;
    }

    public ObjectProperty<SessionStatus> selectedSessionProperty() {
        return selectedSession;
    }

    public WebApiViewModel() {
        ApiEventBus.getInstance().subscribe(this);

        // Load in all existing session status from watchdog.
        for (var status : WebApi.getInstance().getSessionWatchdog().getAllSessionStatus())
            sessionList.add(mapToSessionStatus(status));

        // Set active session count
        activeTransmissionSessions.set(WebApi.getInstance().getSessionWatchdog().getActiveSessions().size());
    }

    public ObservableList<SessionStatus> getSessionList() {
        return sessionList;
    }

    @Override
    public String getSubscribedEventType() {
        return IApiEvent.class.getName();
    }

    @Override
    public void notify(IApiEvent event) {
        var status = event.getStatus();

        if (event instanceof SessionOpenedEvent) {
            activeTransmissionSessions.set(activeTransmissionSessions.get() + 1);
            var mapped = mapToSessionStatus(status);
            sessionList.add(mapped);
        } else if (event instanceof SessionClosedEvent) {
            activeTransmissionSessions.set(activeTransmissionSessions.get() - 1);
        }

        var existing = StreamUtil.firstOrNull(sessionList, x -> x.getSessionId().equals(status.getSessionId()));
        if (existing != null) {
            existing.setIsActive(status.getIsActive());
            existing.setLastRequest(status.getLastRequest());
            existing.setReceivedImages(status.getReceivedImages());
            existing.setReceivedStatistics(status.getReceivedStatistics());
            existing.setSessionClosed(status.getSessionClosed());
            existing.setTotalRequests(status.getTotalRequests());
        } else {
            logger.error("Received request non existant session status outside of session opened event.");
        }

    }

    /**
     * Mapps a TransmissionSessionStatus to a SessionStatus object.
     * 
     * @param status The status to map
     * @return The SessionStatus object
     */
    private SessionStatus mapToSessionStatus(TransmissionSessionStatus status) {
        var mappedStatus = new SessionStatus(status.getSessionId(), status.getGame(), status.getIsActive(),
                status.getLastRequest(), status.getSessionOpened(), status.getSessionClosed(),
                status.getReceivedStatistics(), status.getReceivedImages(), status.getTotalRequests(),
                status.getHostName(), status.getIp());

        return mappedStatus;
    }
}
