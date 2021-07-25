package org.visab.newgui.sessionoverview.viewmodel;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.api.WebApi;
import org.visab.eventbus.GeneralEventBus;
import org.visab.eventbus.ISubscriber;
import org.visab.eventbus.event.VISABFileSavedEvent;
import org.visab.globalmodel.SessionStatus;
import org.visab.newgui.ViewModelBase;
import org.visab.newgui.control.CustomSessionObject;
import org.visab.workspace.Workspace;

import de.saxsys.mvvmfx.utils.commands.Command;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class NewSessionOverviewViewModel extends ViewModelBase {

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

    private SimpleStringProperty totalSessionsProperty = new SimpleStringProperty("0");
    private SimpleStringProperty activeSessionsProperty = new SimpleStringProperty("0");
    private SimpleStringProperty timeoutedSessionsProperty = new SimpleStringProperty("0");
    private SimpleStringProperty canceledSessionsProperty = new SimpleStringProperty("0");
    private SimpleStringProperty webApiAdressProperty = new SimpleStringProperty();

    // TODO: Dont know how to track this.
    private DoubleProperty requestPerSecond = new SimpleDoubleProperty(0.0);

    private ObjectProperty<SessionStatus> selectedSession = new SimpleObjectProperty<>();

    private Command closeSessionCommand;

    private Command createDummySessionsCommand;

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
     * Called after the instance was constructed by javafx/mvvmfx.
     */
    public void initialize() {
        try {
            this.webApiAdressProperty.set(Inet4Address.getLocalHost().getHostAddress() + ":"
                    + Workspace.getInstance().getConfigManager().getWebApiPort());
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public Command createDummySessionsCommand(ScrollPane scrollPane) {
        if (createDummySessionsCommand == null) {
            createDummySessionsCommand = runnableCommand(() -> {

                AnchorPane anchorPane = new AnchorPane();
                GridPane sessionObjectGrid = new GridPane();
                sessionObjectGrid.setPadding(new Insets(10));
                sessionObjectGrid.setHgap(5);
                sessionObjectGrid.setVgap(5);

                var activeSessionsCount = 0;
                var timeoutedSessionsCount = 0;
                var canceledSessionsCount = 0;

                // Used to calculate coordinates on which the session objects should be placed
                gridColSize = 3;
                var rowIterator = 0;
                var colIterator = 0;

                String status;

                for (int i = 0; i < 8; i++) {

                    if (i % 2 == 0) {
                        status = "active";
                        activeSessionsCount++;
                    } else if (i % 3 == 0) {
                        status = "timeouted";
                        timeoutedSessionsCount++;
                    } else {
                        status = "canceled";
                        canceledSessionsCount++;
                    }

                    var logoPath = Workspace.getInstance().getConfigManager().getLogoPathByGame("Settlers");

                    // Customized JavaFX Gridpane which displays relevant session information
                    SessionStatus dummyStatus = new SessionStatus(new UUID(0, 10), "DummyGame", true, LocalTime.now(),
                            LocalTime.now(), LocalTime.now(), 3, 1, 10, "localhost", "127.0.0.1", status);
                    CustomSessionObject sessionObject = new CustomSessionObject(dummyStatus, logoPath);

                    sessionObject.setBackgroundColorByStatus(status);

                    sessionObjectGrid.add(sessionObject, colIterator, rowIterator);

                    colIterator++;

                    // Once there the col size is reached, move on to the next row
                    if (colIterator == gridColSize) {
                        rowIterator++;
                        colIterator = 0;
                    }
                }

                anchorPane.getChildren().add(sessionObjectGrid);

                scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);

                scrollPane.setContent(anchorPane);

                this.totalSessionsProperty.set("8");
                this.activeSessionsProperty.set(String.valueOf(activeSessionsCount));
                this.timeoutedSessionsProperty.set(String.valueOf(timeoutedSessionsCount));
                this.canceledSessionsProperty.set(String.valueOf(canceledSessionsCount));

            });
        }

        return createDummySessionsCommand;
    }

    /**
     * Initializes the session information grid with all available sessions
     * regardless which status they have.
     * 
     * @param anchorPane the anchorpane where the grid pane should be added to.
     */
    public void getSortedSessionGrid(ScrollPane scrollPane) {
        AnchorPane anchorPane = new AnchorPane();
        GridPane sessionObjectGrid = new GridPane();
        sessionObjectGrid.setPadding(new Insets(10));
        sessionObjectGrid.setHgap(5);
        sessionObjectGrid.setVgap(5);

        // Used to calculate coordinates on which the session objects should be placed
        gridColSize = 3;
        var rowIterator = 0;
        var colIterator = 0;

        List<SessionStatus> sortedSessionstatuses = sortSessionStatuses(
                WebApi.getInstance().getSessionAdministration().getSessionStatuses());

        var activeSessionsCount = 0;
        var timeoutedSessionsCount = 0;
        var canceledSessionsCount = 0;

        for (SessionStatus sessionStatus : sortedSessionstatuses) {

            if (sessionStatus.getStatusType().equals("active")) {
                activeSessionsCount++;
            } else if (sessionStatus.getStatusType().equals("timeouted")) {
                timeoutedSessionsCount++;
            } else {
                canceledSessionsCount++;
            }

            var logoPath = Workspace.getInstance().getConfigManager().getLogoPathByGame(sessionStatus.getGame());

            // Customized JavaFX Gridpane which displays relevant session information
            CustomSessionObject sessionObject = new CustomSessionObject(sessionStatus, logoPath);

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
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setContent(anchorPane);

        this.totalSessionsProperty.set(String.valueOf(sortedSessionstatuses.size()));
        this.activeSessionsProperty.set(String.valueOf(activeSessionsCount));
        this.timeoutedSessionsProperty.set(String.valueOf(timeoutedSessionsCount));
        this.canceledSessionsProperty.set(String.valueOf(canceledSessionsCount));
    }

    private List<SessionStatus> sortSessionStatuses(List<SessionStatus> sessionStatusList) {
        Collections.sort(sessionStatusList, byLastRequest);
        return sessionStatusList;
    }

    Comparator<SessionStatus> byLastRequest = new Comparator<SessionStatus>() {
        public int compare(SessionStatus o1, SessionStatus o2) {
            return (-1) * o1.getLastRequest().compareTo(o2.getLastRequest());
        }
    };

    public SimpleStringProperty getTotalSessionsProperty() {
        return totalSessionsProperty;
    }

    public void setTotalSessionsProperty(SimpleStringProperty totalSessionsProperty) {
        this.totalSessionsProperty = totalSessionsProperty;
    }

    public SimpleStringProperty getActiveSessionsProperty() {
        return activeSessionsProperty;
    }

    public void setActiveSessionsProperty(SimpleStringProperty activeSessionsProperty) {
        this.activeSessionsProperty = activeSessionsProperty;
    }

    public SimpleStringProperty getTimeoutedSessionsProperty() {
        return timeoutedSessionsProperty;
    }

    public void setTimeoutedSessionsProperty(SimpleStringProperty timeoutedSessionsProperty) {
        this.timeoutedSessionsProperty = timeoutedSessionsProperty;
    }

    public SimpleStringProperty getCanceledSessionsProperty() {
        return canceledSessionsProperty;
    }

    public void setCanceledSessionsProperty(SimpleStringProperty canceledSessionsProperty) {
        this.canceledSessionsProperty = canceledSessionsProperty;
    }

    public SimpleStringProperty getWebApiAdressProperty() {
        return webApiAdressProperty;
    }

    public void setWebApiAdressProperty(SimpleStringProperty webApiAdressProperty) {
        this.webApiAdressProperty = webApiAdressProperty;
    }

}
