package org.newgui;

import java.time.LocalTime;
import java.util.UUID;

import org.newgui.model.SessionTableRow;
import org.visab.api.WebApi;
import org.visab.eventbus.event.SessionClosedEvent;
import org.visab.eventbus.event.SessionOpenedEvent;
import org.visab.eventbus.event.StatisticsReceivedEvent;
import org.visab.eventbus.subscriber.SubscriberBase;

import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class WebApiViewModel implements ViewModel {

    private class SessionOpenedSubscriber extends SubscriberBase<SessionOpenedEvent> {

        public SessionOpenedSubscriber() {
            super("SessionOpenedEvent");
        }

        @Override
        public void invoke(SessionOpenedEvent event) {
            var newRow = new SessionTableRow(event.getSessionId(), event.getGame(), LocalTime.now(),
                    LocalTime.of(0, 0));

            sessions.add(newRow);
        }

    }

    private class StatisticsReceivedSubscriber extends SubscriberBase<StatisticsReceivedEvent> {

        public StatisticsReceivedSubscriber() {
            super("StatisticsReceivedEvent");
        }

        @Override
        public void invoke(StatisticsReceivedEvent event) {
            for (var row : sessions) {
                if (row.getSessionId() == event.getSessionId()) {
                    row.setLastReceived(LocalTime.now());
                    break;
                }
            }
        }

    }

    private class SessionClosedSubscriber extends SubscriberBase<SessionClosedEvent> {

        public SessionClosedSubscriber() {
            super("SessionClosedEvent");
        }

        @Override
        public void invoke(SessionClosedEvent event) {
            for (var row : sessions) {
                if (row.getSessionId() == event.getSessionId()) {
                    row.setIsActive(false);
                    break;
                }
            }
        }

    }

    public WebApiViewModel() {
        WebApi.getEventBus().subscribe(new SessionOpenedSubscriber());
        WebApi.getEventBus().subscribe(new StatisticsReceivedSubscriber());
        WebApi.getEventBus().subscribe(new SessionClosedSubscriber());
        sessions.add(new SessionTableRow(UUID.randomUUID(), "xd", LocalTime.now(), LocalTime.now()));
    }

    private ObservableList<SessionTableRow> sessions = FXCollections.observableArrayList();
    private ObjectProperty<SessionTableRow> selectedItem;

    public ObservableList<SessionTableRow> sessionsProperty() {
        return sessions;
    }

    public ObjectProperty<SessionTableRow> selectedItemProperty() {
        return selectedItem;
    }

    private StringProperty sampleMessage = new SimpleStringProperty("Im a sample message");

    public StringProperty sampleMessageProperty() {
        return sampleMessage;
    }

    public String getSampleMessage() {
        return sampleMessage.get();
    }

    public void setSampleMessage(String message) {
        sampleMessage.set(message);
    }

}
