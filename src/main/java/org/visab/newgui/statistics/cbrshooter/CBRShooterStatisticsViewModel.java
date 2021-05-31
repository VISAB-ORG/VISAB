package org.visab.newgui.statistics.cbrshooter;

import java.util.ArrayList;
import java.util.List;
import org.visab.globalmodel.cbrshooter.CBRShooterFile;
import org.visab.globalmodel.cbrshooter.CBRShooterStatistics;
import org.visab.newgui.statistics.LiveStatisticsViewModelBase;
import org.visab.newgui.statistics.cbrshooter.model.CBRShooterStatisticsRow;
import org.visab.newgui.statistics.cbrshooter.model.PlayerPlanTime;
import org.visab.newgui.statistics.cbrshooter.model.Vector2;
import org.visab.util.StreamUtil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart.Data;

public class CBRShooterStatisticsViewModel extends LiveStatisticsViewModelBase<CBRShooterFile, CBRShooterStatistics> {

    private ObservableList<CBRShooterStatisticsRow> overviewStatistics = FXCollections.observableArrayList();

    private List<PlayerPlanTime> planTimes = new ArrayList<>();

    private ObservableList<Data> planUsageCBR = FXCollections.observableArrayList();

    private ObservableList<Data> planUsageScript = FXCollections.observableArrayList();

    public ObservableList<Data> getPlanUsageCBR() {
        return planUsageCBR;
    }

    public ObservableList<Data> getPlanUsageScript() {
        return planUsageScript;
    }

    @Override
    public void notifyStatisticsAdded(CBRShooterStatistics newStatistics) {
        // Updates the pie charts for plan usage
        updatePlanUsage(newStatistics);
        overviewStatistics.add(mapToRow(newStatistics));
    }

    private void updatePlanUsage(CBRShooterStatistics newStatistics) {
        for (var player : newStatistics.getPlayers()) {
            var plan = player.getPlan();

            if (plan.trim().equals(""))
                plan = "No plan";

            // Update our plan occurances
            PlayerPlanTime planTime = StreamUtil.firstOrNull(planTimes,
                    x -> x.getPlayerName().equals(player.getName()));

            if (planTime == null) {
                planTime = new PlayerPlanTime(player.getName(), player.getIsCBR());
                planTimes.add(planTime);
            }
            planTime.incrementOccurance(plan, newStatistics.getRound(), newStatistics.getRoundTime());

            // Update our pie charts
            if (player.getIsCBR())
                updatePieChart(planUsageCBR, plan, planTime.getTotalTime(plan));
            else
                updatePieChart(planUsageScript, plan, planTime.getTotalTime(plan));
        }
    }

    private void updatePieChart(ObservableList<Data> planUsageList, String plan, double totalTime) {
        var data = StreamUtil.firstOrNull(planUsageList, x -> x.getName().equals(plan));

        if (data == null)
            planUsageList.add(new Data(plan, totalTime));
        else
            data.setPieValue(totalTime);
    }

    private CBRShooterStatisticsRow mapToRow(CBRShooterStatistics statistics) {
        var position = statistics.getPlayers().get(0).getPosition();
        return new CBRShooterStatisticsRow(new Vector2(position.getX(), position.getY()));
    }

    public ObservableList<CBRShooterStatisticsRow> getOverviewStatistics() {
        return overviewStatistics;
    }

    @Override
    public void notifySessionClosed() {
        liveSessionActiveProperty.set(false);
        // TODO: Render some future "who won" graphs an such
    }

    @Override
    public void afterInitialize(CBRShooterFile file) {
        for (var statistics : file.getStatistics()) {
            notifyStatisticsAdded(statistics);
        }
    }

}
