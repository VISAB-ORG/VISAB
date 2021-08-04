package org.visab.newgui.sessionoverview.viewmodel;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;

import org.visab.api.SessionAdministration;
import org.visab.api.WebAPI;
import org.visab.globalmodel.SessionStatus;
import org.visab.newgui.GenericScope;
import org.visab.newgui.ViewModelBase;
import org.visab.workspace.Workspace;

import de.saxsys.mvvmfx.InjectScope;
import de.saxsys.mvvmfx.utils.commands.Command;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SessionOverviewViewModel extends ViewModelBase {

    @InjectScope
    GenericScope scope;

    private IntegerProperty totalSessionsProperty = new SimpleIntegerProperty(0);
    private IntegerProperty activeSessionsProperty = new SimpleIntegerProperty(0);
    private IntegerProperty timeoutedSessionsProperty = new SimpleIntegerProperty(0);
    private IntegerProperty canceledSessionsProperty = new SimpleIntegerProperty(0);
    private StringProperty webApiAdressProperty = new SimpleStringProperty();

    private Command clearInactiveSessionsCommand;

    /**
     * Called after the instance was constructed by javafx/mvvmfx.
     */
    public void initialize() {
        try {
            this.webApiAdressProperty.set(Inet4Address.getLocalHost().getHostAddress() + ":"
                    + Workspace.getInstance().getConfigManager().getWebApiPort());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
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
        activeSessionsProperty.set(sessionAdministration.getActiveSessionStatuses().size());
        timeoutedSessionsProperty.set(sessionAdministration.getTimeoutedSessionStatuses().size());
        canceledSessionsProperty.set(sessionAdministration.getCanceledSessionStatuses().size());
        totalSessionsProperty.set(sessionAdministration.getSessionStatuses().size());
        return sortSessionStatuses(sessionAdministration.getSessionStatuses());
    }

    public GenericScope getScope() {
        return scope;
    }

    public IntegerProperty totalSessionsProperty() {
        return this.totalSessionsProperty;
    }

    public IntegerProperty activeSessionsProperty() {
        return this.activeSessionsProperty;
    }

    public IntegerProperty timeoutedSessionsProperty() {
        return this.timeoutedSessionsProperty;
    }

    public IntegerProperty canceledSessionsProperty() {
        return this.canceledSessionsProperty;
    }

    public StringProperty webApiAdressProperty() {
        return this.webApiAdressProperty;
    }
}
