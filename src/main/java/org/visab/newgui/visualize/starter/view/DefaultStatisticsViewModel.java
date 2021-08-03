package org.visab.newgui.visualize.starter.view;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.visab.globalmodel.starter.DefaultFile;
import org.visab.newgui.visualize.VisualizeViewModelBase;
import org.visab.newgui.visualize.VisualizeScope;
import org.visab.util.JSONConvert;
import org.visab.workspace.DatabaseRepository;

import de.saxsys.mvvmfx.InjectScope;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

public class DefaultStatisticsViewModel extends VisualizeViewModelBase<DefaultFile> {

    @InjectScope
    VisualizeScope scope;

    public void initialize() {
        if (scope.isLive()) {
            throw new RuntimeException();
        }

        super.initialize(scope.getFile());

        var allStatistics = file.getStatistics();
        for (int i = 0; i < allStatistics.size(); i++) {
            var statistics = allStatistics.get(i);
            var node = JSONConvert.deserializeJsonUnknown(statistics);
            var treeItem = createTreeItem(i + " statistics", node);
            statisticsList.add(treeItem);
        }
    }

    public String getGame() {
        return file.getGame();
    }

    public String getFileFormatVersion() {
        return file.getFileFormatVersion();
    }

    public LocalDateTime getCreationDate() {
        return file.getCreationDate();
    }

    private ObservableList<TreeItem<String>> statisticsList = FXCollections.observableArrayList();

    public ObservableList<TreeItem<String>> getStatistics() {
        return statisticsList;
    }

    private TreeItem<String> createTreeItem(String name, JsonNode node) {
        TreeItem<String> item = new TreeItem<>();
        if (node.isObject()) {
            item.setValue(name);

            node.fieldNames().forEachRemaining(fieldName -> {
                var childNode = node.get(fieldName);
                var child = createTreeItem(fieldName, childNode);
                item.getChildren().add(child);
            });
        } else if (node.isArray()) {
            item.setValue(name);
            for (int i = 0; i < node.size(); i++) {
                TreeItem<String> child = createTreeItem(String.valueOf(i), node.get(i));
                item.getChildren().add(child);
            }
        } else {
            item.setValue(name + " : " + node.asText());
        }

        return item;
    }

}
