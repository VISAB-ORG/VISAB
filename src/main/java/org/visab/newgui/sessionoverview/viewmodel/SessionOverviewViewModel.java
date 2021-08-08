package org.visab.newgui.sessionoverview.viewmodel;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;

import org.visab.api.SessionAdministration;
import org.visab.api.WebAPI;
import org.visab.globalmodel.SessionStatus;
import org.visab.newgui.GeneralScope;
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
    GeneralScope scope;

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
            clearInactiveSessionsCommand = makeCommand(() -> {
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
     * Intermediate method to retrieve session statuses from the Web Api. Also
     * directly adjusts the session count properties.
     * 
     * @return The sorted session statuses descending by time of last request.
     */
    public List<SessionStatus> querySessionStatusesSorted() {
        SessionAdministration sessionAdministration = WebAPI.getInstance().getSessionAdministration();
        var statuses = sessionAdministration.getSessionStatuses();

        activeSessionsProperty.set(sessionAdministration.getActiveSessionStatuses().size());
        timeoutedSessionsProperty.set(sessionAdministration.getTimeoutedSessionStatuses().size());
        canceledSessionsProperty.set(sessionAdministration.getCanceledSessionStatuses().size());
        totalSessionsProperty.set(statuses.size());

        Collections.sort(statuses, (o1, o2) -> (-1) * o1.getLastRequest().compareTo(o2.getLastRequest()));

        return statuses;
    }

    public GeneralScope getScope() {
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
