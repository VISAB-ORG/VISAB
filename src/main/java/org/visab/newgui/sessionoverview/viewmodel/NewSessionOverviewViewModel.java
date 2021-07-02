package org.visab.newgui.sessionoverview.viewmodel;

import java.time.LocalTime;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.api.WebApi;
import org.visab.eventbus.ApiEventBus;
import org.visab.eventbus.GeneralEventBus;
import org.visab.eventbus.IApiEvent;
import org.visab.eventbus.ISubscriber;
import org.visab.eventbus.event.SessionClosedEvent;
import org.visab.eventbus.event.SessionOpenedEvent;
import org.visab.eventbus.event.VISABFileSavedEvent;
import org.visab.globalmodel.SessionStatus;
import org.visab.newgui.DynamicViewLoader;
import org.visab.newgui.ViewModelBase;
import org.visab.newgui.control.CustomSessionObject;
import org.visab.util.StreamUtil;
import org.visab.workspace.Workspace;

import de.saxsys.mvvmfx.utils.commands.Command;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class NewSessionOverviewViewModel extends ViewModelBase implements ISubscriber<IApiEvent> {

    private int gridColSize;

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

    private Logger logger = LogManager.getLogger(NewSessionOverviewViewModel.class);

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
                    WebApi.getInstance().getSessionAdministration().closeSession(selectedSession.get().getSessionId());
            });
        }

        return closeSessionCommand;
    }

    /**
     * Initializes the session information grid with all available sessions
     * regardless which status they have.
     * 
     * @param anchorPane the anchorpane where the grid pane should be added to.
     */
    public void initializeSessionGrid(AnchorPane anchorPane) {
        GridPane sessionObjectGrid = new GridPane();
        sessionObjectGrid.setPadding(new Insets(10));
        sessionObjectGrid.setHgap(5);
        sessionObjectGrid.setVgap(5);

        // Used to calculate coordinates on which the session objects should be placed
        gridColSize = 3;
        var rowIterator = 0;
        var colIterator = 0;

        for (UUID sessionId : WebApi.getInstance().getSessionAdministration().getSessionIds()) {
            SessionStatus sessionStatus = WebApi.getInstance().getSessionAdministration().getStatus(sessionId);

            var logoPath = Workspace.getInstance().getConfigManager().getLogoPathByGame(sessionStatus.getGame());

            // Customized JavaFX Gridpane which displays relevant session information
            CustomSessionObject sessionObject = new CustomSessionObject(sessionStatus.getGame(), logoPath, sessionId,
                    sessionStatus.getHostName(), sessionStatus.getIp(), sessionStatus.getSessionOpened().toString(),
                    sessionStatus.getStatusType());

            sessionObject.setBackgroundColorByStatus(sessionStatus.getStatusType());

            sessionObjectGrid.add(sessionObject, colIterator, rowIterator);

            colIterator++;

            // Once there the col size is reached, move on to the next row
            if (colIterator == gridColSize) {
                rowIterator++;
                colIterator = 0;
            }

        }

        anchorPane.getChildren().add(sessionObjectGrid);
    }

    private Command openLiveViewCommand;

    public Command openLiveViewCommand() {
        if (openLiveViewCommand == null) {
            openLiveViewCommand = runnableCommand(() -> {
                if (selectedSession != null) {
                    var sessionInfo = selectedSession.get();

                    DynamicViewLoader.loadVisualizer(sessionInfo.getGame(), sessionInfo.getSessionId());
                }
            });
        }

        return openLiveViewCommand;
    }

    public ObjectProperty<SessionStatus> selectedSessionProperty() {
        return selectedSession;
    }

    public NewSessionOverviewViewModel() {
        ApiEventBus.getInstance().subscribe(this);

        // Load in all existing session status from watchdog.
        for (var status : WebApi.getInstance().getSessionAdministration().getSessionStatuses())
            sessionList.add(status);

        // Set active session count
        activeTransmissionSessions
                .set(WebApi.getInstance().getSessionAdministration().getActiveSessionStatuses().size());
    }

    public ObservableList<SessionStatus> getSessionList() {
        return sessionList;
    }

    @Override
    public String getSubscribedEventType() {
        return IApiEvent.class.getName();
    }

    private LocalTime lastRequestTime;

    @Override
    public void notify(IApiEvent event) {
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
