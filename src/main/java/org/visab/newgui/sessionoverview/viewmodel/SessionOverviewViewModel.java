package org.visab.newgui.sessionoverview.viewmodel;

import java.time.LocalTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.api.SessionAdministration;
import org.visab.api.WebAPI;
import org.visab.eventbus.APIEventBus;
import org.visab.eventbus.GeneralEventBus;
import org.visab.eventbus.IAPIEvent;
import org.visab.eventbus.ISubscriber;
import org.visab.eventbus.event.SessionClosedEvent;
import org.visab.eventbus.event.SessionOpenedEvent;
import org.visab.eventbus.event.VISABFileSavedEvent;
import org.visab.globalmodel.SessionStatus;
import org.visab.newgui.DynamicViewLoader;
import org.visab.newgui.ViewModelBase;
import org.visab.processing.ILiveViewable;
import org.visab.processing.SessionListenerAdministration;
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

public class SessionOverviewViewModel extends ViewModelBase implements ISubscriber<IAPIEvent> {

    private class FileSavedSubscriber implements ISubscriber<VISABFileSavedEvent> {

        public FileSavedSubscriber() {
            GeneralEventBus.getInstance().subscribe(this);
        }

        @Override
        public String getSubscribedEventType() {
            return VISABFileSavedEvent.class.getName();
        }

        @Override
        public void notify(VISABFileSavedEvent event) {
            if (event.isSavedByListener())
                // TODO: add it to the listener directly or sth.
                // This has to be decided based on the view
                savedFiles.add(event.getFileName());
        }
    }

    private ObservableList<String> savedFiles = FXCollections.observableArrayList();

    private Logger logger = LogManager.getLogger(SessionOverviewViewModel.class);

    private IntegerProperty activeTransmissionSessions = new SimpleIntegerProperty(0);

    // TODO: Dont know how to track this.
    private DoubleProperty requestPerSecond = new SimpleDoubleProperty(0.0);

    private ObservableList<SessionStatus> sessionList = FXCollections.observableArrayList();

    private ObjectProperty<SessionStatus> selectedSession = new SimpleObjectProperty<>();

    private Command closeSessionCommand;

    public Command closeSessionCommand() {
        if (closeSessionCommand == null) {
            closeSessionCommand = runnableCommand(() -> {
                if (selectedSession.get() != null && selectedSession.get().isActive())
                    WebAPI.getInstance().getSessionAdministration().closeSession(selectedSession.get().getSessionId());
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

                    var listener = SessionListenerAdministration.getSessionListener(sessionInfo.getSessionId());
                    if (listener != null && !(listener instanceof ILiveViewable<?>)) {
                        dialogHelper.showError(
                                "Underlying session listener does not support live viewing!\n It does not implement ILiveViewable<?>.");
                    } else {
                        DynamicViewLoader.loadVisualizer(sessionInfo.getGame(), sessionInfo.getSessionId());
                    }
                }
            });
        }

        return openLiveViewCommand;
    }

    public ObjectProperty<SessionStatus> selectedSessionProperty() {
        return selectedSession;
    }

    public SessionOverviewViewModel() {
        APIEventBus.getInstance().subscribe(this);

        // Load in all existing session status from watchdog.
        for (var status : WebAPI.getInstance().getSessionAdministration().getSessionStatuses())
            sessionList.add(status);

        // Set active session count
        activeTransmissionSessions
                .set(WebAPI.getInstance().getSessionAdministration().getActiveSessionStatuses().size());
    }

    public ObservableList<SessionStatus> getSessionList() {
        return sessionList;
    }

    @Override
    public String getSubscribedEventType() {
        return IAPIEvent.class.getName();
    }

    private LocalTime lastRequestTime;

    @Override
    public void notify(IAPIEvent event) {
        var status = event.getStatus();

        if (event instanceof SessionOpenedEvent) {
            activeTransmissionSessions.set(activeTransmissionSessions.get() + 1);
            sessionList.add(status);
        } else if (event instanceof SessionClosedEvent) {
            activeTransmissionSessions.set(activeTransmissionSessions.get() - 1);
        }

        // TODO: Is not needed if we use the same status object.
        var existing = StreamUtil.firstOrNull(sessionList, x -> x.getSessionId().equals(status.getSessionId()));
        if (existing != null) {
            existing.setIsActive(status.isActive());
            existing.setLastRequest(status.getLastRequest());
            existing.setReceivedImages(status.getReceivedImages());
            existing.setReceivedStatistics(status.getReceivedStatistics());
            existing.setSessionClosed(status.getSessionClosed());
            existing.setTotalRequests(status.getTotalRequests());
        } else {
            logger.error("Received request with non existant session status outside of session opened event.");
        }

    }

}
