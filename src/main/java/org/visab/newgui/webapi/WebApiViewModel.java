package org.visab.newgui.webapi;

import java.time.LocalTime;

import org.visab.api.WebApi;
import org.visab.dynamic.DynamicViewTupleLoader;
import org.visab.eventbus.event.SessionClosedEvent;
import org.visab.eventbus.event.SessionOpenedEvent;
import org.visab.eventbus.event.StatisticsReceivedEvent;
import org.visab.eventbus.subscriber.SubscriberBase;
import org.visab.newgui.ViewModelBase;
import org.visab.newgui.statistics.LiveStatisticsViewModelBase;
import org.visab.newgui.statistics.StatisticsViewModelBase;
import org.visab.newgui.webapi.model.SessionInformation;
import org.visab.processing.ILiveViewable;
import org.visab.processing.SessionListenerAdministration;
import org.visab.workspace.Workspace;

import de.saxsys.mvvmfx.utils.commands.Command;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class WebApiViewModel extends ViewModelBase {

    private class SessionClosedSubscriber extends SubscriberBase<SessionClosedEvent> {

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

    private class SessionOpenedSubscriber extends SubscriberBase<SessionOpenedEvent> {

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

    private class StatisticsReceivedSubscriber extends SubscriberBase<StatisticsReceivedEvent> {

        public StatisticsReceivedSubscriber() {
            super(StatisticsReceivedEvent.class);
        }

        @Override
        public void notify(StatisticsReceivedEvent event) {
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
                if (selectedSession != null && selectedSession.get().getIsActive())
                    openLiveView();
                else if (selectedSession != null)
                    openView();
            });
        }

        return openLiveViewCommand;
    }

    private void openLiveView() {
        var sessionInfo = selectedSession.get();

        var listener = SessionListenerAdministration.getSessionListener(sessionInfo.getSessionId());
        if (listener != null) {
            var viewTupel = DynamicViewTupleLoader.loadStatisticsView(sessionInfo.getGame());
            var root = viewTupel.getView();

            var vm = (StatisticsViewModelBase<?>) viewTupel.getViewModel();
            if (vm.supportsLiveViewing() && listener instanceof ILiveViewable<?>) {
                var liveListener = (ILiveViewable<?>) listener;
                var liveVm = (LiveStatisticsViewModelBase<?, ?>) vm;

                liveVm.initialize(liveListener);

                openWindow(root, "TODO - LIVE");
            }
        }
    }

    private void openView() {
        var dbManager = Workspace.getInstance().getDatabaseManager();
        var sessionInfo = selectedSession.get();

        var fileName = dbManager.getSessionFileName(sessionInfo.getSessionId());
        if (!fileName.isBlank()) {
            var file = dbManager.loadFile(fileName, sessionInfo.getGame());
            if (file != null) {
                var viewTupel = DynamicViewTupleLoader.loadStatisticsView(sessionInfo.getGame());
                var root = viewTupel.getView();
                var vm = (StatisticsViewModelBase<?>) viewTupel.getViewModel();

                // Initialize the ViewModel with the loaded file
                vm.initialize(file);

                openWindow(root, "TODO - NOT LIVE");
            }
        }
    }

    public void initialize() {
        // TODO: Do we have to unsubscribe these?
        new SessionOpenedSubscriber();
        new StatisticsReceivedSubscriber();
        new SessionClosedSubscriber();
    }

    public ObjectProperty<SessionInformation> selectedSessionProperty() {
        return selectedSession;
    }

    public ObservableList<SessionInformation> getSessions() {
        return sessions;
    }

}
