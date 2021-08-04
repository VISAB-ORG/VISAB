package org.visab.newgui.sessionoverview.viewmodel;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.api.SessionAdministration;
import org.visab.api.WebAPI;
import org.visab.globalmodel.SessionStatus;
import org.visab.newgui.GenericScope;
import org.visab.newgui.ViewModelBase;
import org.visab.workspace.Workspace;

import de.saxsys.mvvmfx.InjectScope;
import de.saxsys.mvvmfx.utils.commands.Command;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class SessionOverviewViewModel extends ViewModelBase {

    @InjectScope
    GenericScope scope;

    private Logger logger = LogManager.getLogger(SessionOverviewViewModel.class);

    private SimpleStringProperty totalSessionsProperty = new SimpleStringProperty("0");
    private SimpleStringProperty activeSessionsProperty = new SimpleStringProperty("0");
    private SimpleStringProperty timeoutedSessionsProperty = new SimpleStringProperty("0");
    private SimpleStringProperty canceledSessionsProperty = new SimpleStringProperty("0");
    private SimpleStringProperty webApiAdressProperty = new SimpleStringProperty();

    // TODO: Dont know how to track this.
    private DoubleProperty requestPerSecond = new SimpleDoubleProperty(0.0);

    private ObjectProperty<SessionStatus> selectedSession = new SimpleObjectProperty<>();

    private Command closeSessionCommand;
    private Command clearInactiveSessionsCommand;

    public Command closeSessionCommand() {
        if (closeSessionCommand == null) {
            closeSessionCommand = runnableCommand(() -> {
                if (selectedSession.get() != null && selectedSession.get().isActive())
                    WebAPI.getInstance().getSessionAdministration().closeSession(selectedSession.get().getSessionId());
            });
        }

        return closeSessionCommand;
    }

    public Command clearInactiveSessionsCommand() {
        if (clearInactiveSessionsCommand == null) {
            clearInactiveSessionsCommand = runnableCommand(() -> {
                for (SessionStatus status : WebAPI.getInstance().getSessionAdministration().getSessionStatuses()) {
                    if (!status.getStatusType().equals("active")) {
                        WebAPI.getInstance().getSessionAdministration().removeByUUID(status.getSessionId());
                    }
                }
            });
        }

        return clearInactiveSessionsCommand;
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

    /**
     * This method takes in a list of session statuses and sorts them by their
     * respective last request time to make sure the most recent sessions are always
     * at the start.
     * 
     * @param sessionStatusList the list that needs to be sorted.
     * @return the sorted list
     */
    private List<SessionStatus> sortSessionStatuses(List<SessionStatus> sessionStatusList) {
        Collections.sort(sessionStatusList, (o1, o2) -> (-1) * o1.getLastRequest().compareTo(o2.getLastRequest()));
        return sessionStatusList;
    }

    /**
     * Intermediate method to retrieve session statuses from the Web Api. Also
     * directly adjusts the session count properties.
     * 
     * @return the (sorted) session statuses.
     */
    public List<SessionStatus> querySessionStatusesSorted() {
        SessionAdministration sessionAdministration = WebAPI.getInstance().getSessionAdministration();
        activeSessionsProperty.set(String.valueOf(sessionAdministration.getActiveSessionStatuses().size()));
        timeoutedSessionsProperty.set(String.valueOf(sessionAdministration.getTimeoutedSessionStatuses().size()));
        canceledSessionsProperty.set(String.valueOf(sessionAdministration.getCanceledSessionStatuses().size()));
        totalSessionsProperty.set(String.valueOf(sessionAdministration.getSessionStatuses().size()));
        return sortSessionStatuses(sessionAdministration.getSessionStatuses());
    }

    public SimpleStringProperty getTotalSessionsProperty() {
        return totalSessionsProperty;
    }

    public SimpleStringProperty getActiveSessionsProperty() {
        return activeSessionsProperty;
    }

    public SimpleStringProperty getTimeoutedSessionsProperty() {
        return timeoutedSessionsProperty;
    }

    public SimpleStringProperty getCanceledSessionsProperty() {
        return canceledSessionsProperty;
    }

    public SimpleStringProperty getWebApiAdressProperty() {
        return webApiAdressProperty;
    }

    public GenericScope getScope() {
        return scope;
    }
}
