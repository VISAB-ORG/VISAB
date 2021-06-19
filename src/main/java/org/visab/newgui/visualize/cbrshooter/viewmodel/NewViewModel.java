package org.visab.newgui.visualize.cbrshooter.viewmodel;

import org.visab.globalmodel.cbrshooter.CBRShooterFile;
import org.visab.newgui.visualize.StatisticsViewModelBase;
import org.visab.newgui.visualize.cbrshooter.model.ComparisonRowBase;
import org.visab.newgui.visualize.cbrshooter.model.KillsComparisonRow;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class NewViewModel extends StatisticsViewModelBase<CBRShooterFile> {

    private ObservableList<String> playerNames = FXCollections.observableArrayList();

    private ObservableList<ComparisonRowBase<?>> comparisonStatistics = FXCollections.observableArrayList();

    public NewViewModel() {
        comparisonStatistics.add(new KillsComparisonRow());
    }

    public ObservableList<String> getPlayerNames() {
        return playerNames;
    }

    public ObservableList<ComparisonRowBase<?>> getComparisonStatistics() {
        return comparisonStatistics;
    }

    @Override
    public void afterInitialize(CBRShooterFile file) {
        // Set all the values for the comparison view
        for (var row : comparisonStatistics) {
            row.updateValues(file);
        }
    }

}
