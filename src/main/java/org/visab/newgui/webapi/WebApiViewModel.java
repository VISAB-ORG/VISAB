package org.visab.newgui.webapi;

import java.time.LocalTime;

import org.visab.api.WebApi;
import org.visab.eventbus.ApiEventBus;
import org.visab.eventbus.IApiEvent;
import org.visab.eventbus.ISubscriber;
import org.visab.eventbus.event.SessionClosedEvent;
import org.visab.eventbus.event.SessionOpenedEvent;
import org.visab.eventbus.event.StatisticsReceivedEvent;
import org.visab.eventbus.subscriber.ApiSubscriberBase;
import org.visab.newgui.DynamicViewLoader;
import org.visab.newgui.ViewModelBase;
import org.visab.newgui.webapi.model.SessionInformation;

import de.saxsys.mvvmfx.utils.commands.Command;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class WebApiViewModel extends ViewModelBase implements ISubscriber<IApiEvent> {

    private class SessionClosedSubscriber extends ApiSubscriberBase<SessionClosedEvent> {

        public SessionClosedSubscriber() {
            super(SessionClosedEvent.class);
        }

        @Override
        public void notify(SessionClosedEvent event) {
            for (var row : sessions) {
                if (row.getSessionId().equals(event.getSessionId())) {
                    row.setIsActive(false);
                    break;
                }
            }
        }

    }

    private class SessionOpenedSubscriber extends ApiSubscriberBase<SessionOpenedEvent> {

        public SessionOpenedSubscriber() {
            super(SessionOpenedEvent.class);
        }

        @Override
        public void notify(SessionOpenedEvent event) {
            var newRow = new SessionInformation(event.getSessionId(), event.getGame(), LocalTime.now(), LocalTime.now(),
                    event.getRemoteCallerIp(), event.getRemoteCallerHostName());

            sessions.add(newRow);
        }

    }

    private class StatisticsReceivedSubscriber extends ApiSubscriberBase<StatisticsReceivedEvent> {

        public StatisticsReceivedSubscriber() {
            super(StatisticsReceivedEvent.class);
        }

        @Override
        public void notify(StatisticsReceivedEvent event) {
            var status = WebApi.getInstance().getSessionWatchdog().getStatus(event.getSessionId());

            for (var row : sessions) {
                if (row.getSessionId().equals(event.getSessionId())) {
                    row.setLastReceived(LocalTime.now());
                    break;
                }
            }
        }
    }

    private ObjectProperty<SessionInformation> selectedSession = new SimpleObjectProperty<>();

    private ObservableList<SessionInformation> sessions = FXCollections.observableArrayList();

    private Command closeSessionCommand;

    public Command closeSessionCommand() {
        if (closeSessionCommand == null) {
            closeSessionCommand = runnableCommand(() -> {
                if (selectedSession.get() != null && selectedSession.get().getIsActive())
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

    public ObjectProperty<SessionInformation> selectedSessionProperty() {
        return selectedSession;
    }

    public WebApiViewModel() {
        ApiEventBus.getInstance().subscribe(this);
    }

    public ObservableList<SessionInformation> getSessions() {
        return sessions;
    }

    @Override
    public String getSubscribedEventType() {
        return IApiEvent.class.getName();
    }

    @Override
    public void notify(IApiEvent event) {
        var status = event.getStatus();
        System.out.println(status);
    }

}
