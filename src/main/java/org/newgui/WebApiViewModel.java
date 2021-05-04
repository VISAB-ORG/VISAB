package org.newgui;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import org.newgui.model.SessionTableRow;
import org.visab.api.WebApi;
import org.visab.eventbus.event.SessionClosedEvent;
import org.visab.eventbus.event.SessionOpenedEvent;
import org.visab.eventbus.event.StatisticsReceivedEvent;
import org.visab.eventbus.subscriber.SubscriberBase;

import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class WebApiViewModel implements ViewModel {

    private class SessionOpenedSubscriber extends SubscriberBase<SessionOpenedEvent> {

        public SessionOpenedSubscriber() {
            super(SessionOpenedEvent.class);
        }

        @Override
        public void invoke(SessionOpenedEvent event) {
            var newRow = new SessionTableRow(event.getSessionId(), event.getGame(), LocalTime.now(), LocalTime.now());

            sessions.add(newRow);
        }

    }

    private class StatisticsReceivedSubscriber extends SubscriberBase<StatisticsReceivedEvent> {

        public StatisticsReceivedSubscriber() {
            super(StatisticsReceivedEvent.class);
        }

        @Override
        public void invoke(StatisticsReceivedEvent event) {
            for (var row : sessions) {
                if (row.getSessionId().equals(event.getSessionId())) {
                    row.setLastReceived(LocalTime.now());
                    notifySessionRowUpdate(event.getSessionId());
                    break;
                }
            }
        }

    }

    private class SessionClosedSubscriber extends SubscriberBase<SessionClosedEvent> {

        public SessionClosedSubscriber() {
            super(SessionClosedEvent.class);
        }

        @Override
        public void invoke(SessionClosedEvent event) {
            for (var row : sessions) {
                if (row.getSessionId().equals(event.getSessionId())) {
                    row.setIsActive(false);
                    notifySessionRowUpdate(event.getSessionId());
                    break;
                }
            }
        }

    }

    public void initialize() {
        WebApi.getEventBus().subscribe(new SessionOpenedSubscriber());
        WebApi.getEventBus().subscribe(new StatisticsReceivedSubscriber());
        WebApi.getEventBus().subscribe(new SessionClosedSubscriber());
    }

    /**
     * Notifies the View, that a property of a SessionTableRow was updated
     */
    private void notifySessionRowUpdate(UUID sessionId) {
        for (int i = 0; i < sessions.size(); i++) {
            var row = sessions.get(i);
            if (row.getSessionId().equals(sessionId)) {
                sessions.set(i, row);
                break;
            }
        }
    }

    private ObservableList<SessionTableRow> sessions = FXCollections.observableArrayList();

    private ObjectProperty<SessionTableRow> selectedSessionRow = new SimpleObjectProperty<>();

    public ObservableList<SessionTableRow> sessionsProperty() {
        return sessions;
    }

    public ObjectProperty<SessionTableRow> selectedSessionRowProperty() {
        return selectedSessionRow;
    }

    public ObservableList<SessionTableRow> getSessions() {
        return sessions;
    }

}
