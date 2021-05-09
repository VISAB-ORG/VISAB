package org.newgui.repository;

import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.util.Callback;

/**
 * Copied from https://lestard.eu/2015/treetable_datamodel/
 */
public class RecursiveTreeItem<T> extends TreeItem<T> {

    private Callback<T, ObservableList<T>> childrenFactory;

    private Callback<T, Node> graphicsFactory;

    public RecursiveTreeItem(Callback<T, ObservableList<T>> childrenFactory) {
        this(null, childrenFactory);
    }

    public RecursiveTreeItem(final T value, Callback<T, Node> graphicsFactory,
            Callback<T, ObservableList<T>> childrenFactory) {
        super(value, graphicsFactory.call(value));

        this.graphicsFactory = graphicsFactory;
        this.childrenFactory = childrenFactory;

        if (value != null) {
            addChildrenListener(value);
        }

        valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                addChildrenListener(newValue);
            }
        });

        this.setExpanded(true);
    }

    public RecursiveTreeItem(final T value, Callback<T, ObservableList<T>> childrenFactory) {
        this(value, (item) -> null, childrenFactory);
    }

    private void addChildrenListener(T value) {
        final ObservableList<T> children = childrenFactory.call(value);

        children.forEach(child -> RecursiveTreeItem.this.getChildren()
                .add(new RecursiveTreeItem<>(child, this.graphicsFactory, childrenFactory)));

        children.addListener((ListChangeListener<T>) change -> {
            while (change.next()) {

                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(t -> RecursiveTreeItem.this.getChildren()
                            .add(new RecursiveTreeItem<>(t, this.graphicsFactory, childrenFactory)));
                }

                if (change.wasRemoved()) {
                    change.getRemoved().forEach(t -> {
                        final List<TreeItem<T>> itemsToRemove = RecursiveTreeItem.this.getChildren().stream()
                                .filter(treeItem -> treeItem.getValue().equals(t)).collect(Collectors.toList());

                        RecursiveTreeItem.this.getChildren().removeAll(itemsToRemove);
                    });
                }

            }
        });
    }
}