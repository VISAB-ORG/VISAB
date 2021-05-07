package org.newgui.repository;

import java.io.*;
import java.util.*;
import java.text.*;
import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.geometry.*;
import javafx.event.*;
import javafx.collections.*;
import javafx.beans.property.*;
import javafx.util.*;
public class Test2 extends Application {

    SimpleDateFormat dateFormat = new SimpleDateFormat();
    NumberFormat numberFormat = NumberFormat.getIntegerInstance();

    Label label;
    TreeTableView<File> treeTableView;

    @Override
    public void start(Stage stage) throws Exception {
        label = new Label();
        treeTableView = createFileBrowserTreeTableView();

        BorderPane layout = new BorderPane();
        layout.setCenter(treeTableView);
        layout.setBottom(label);

        stage.setScene(new Scene(layout, 600, 400));
        stage.show();
    }

    private TreeTableView<File> createFileBrowserTreeTableView() {

        FileTreeItem root = new FileTreeItem(new File("/"));

        final TreeTableView<File> treeTableView = new TreeTableView<>();

        treeTableView.setShowRoot(true);
        treeTableView.setRoot(root);
        root.setExpanded(true);
        treeTableView.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);

        TreeTableColumn<File, FileTreeItem> nameColumn = new TreeTableColumn<>("Name");

        nameColumn.setCellValueFactory(
                cellData -> new ReadOnlyObjectWrapper<FileTreeItem>((FileTreeItem) cellData.getValue()));

        Image image1 = getImageResource("img/unknown-file-16x16.png");
        Image image2 = getImageResource("img/folder-open-16x16.png");
        Image image3 = getImageResource("img/folder-close-16x16.png");

        nameColumn.setCellFactory(column -> {
            TreeTableCell<File, FileTreeItem> cell = new TreeTableCell<File, FileTreeItem>() {

                ImageView imageView1 = new ImageView(image1);
                ImageView imageView2 = new ImageView(image2);
                ImageView imageView3 = new ImageView(image3);

                @Override
                protected void updateItem(FileTreeItem item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty || item.getValue() == null) {
                        setText(null);
                        setGraphic(null);
                        setStyle("");
                    } else {
                        File f = item.getValue();
                        String text = f.getParentFile() == null ? File.separator : f.getName();
                        setText(text);
                        String style = item.isHidden() && f.getParentFile() != null ? "-fx-accent"
                                : "-fx-text-base-color";
                        setStyle("-fx-text-fill: " + style);
                        if (item.isLeaf()) {
                            setGraphic(imageView1);
                        } else {
                            setGraphic(item.isExpanded() ? imageView2 : imageView3);
                        }
                    }
                }
            };
            return cell;
        });

        nameColumn.setPrefWidth(300);
        nameColumn.setSortable(false);
        treeTableView.getColumns().add(nameColumn);

        TreeTableColumn<File, String> sizeColumn = new TreeTableColumn<>("Size");

        sizeColumn.setCellValueFactory(cellData -> {
            FileTreeItem item = ((FileTreeItem) cellData.getValue());
            String s = item.isLeaf() ? numberFormat.format(item.length()) : "";
            return new ReadOnlyObjectWrapper<String>(s);
        });

        Callback<TreeTableColumn<File, String>, TreeTableCell<File, String>> sizeCellFactory = sizeColumn
                .getCellFactory();
        sizeColumn.setCellFactory(column -> {
            TreeTableCell<File, String> cell = sizeCellFactory.call(column);
            cell.setAlignment(Pos.CENTER_RIGHT);
            cell.setPadding(new Insets(0, 8, 0, 0));
            return cell;
        });

        sizeColumn.setPrefWidth(100);
        sizeColumn.setSortable(false);
        treeTableView.getColumns().add(sizeColumn);

        TreeTableColumn<File, String> lastModifiedColumn = new TreeTableColumn<>("Last Modified");
        lastModifiedColumn.setCellValueFactory(cellData -> {
            FileTreeItem item = (FileTreeItem) cellData.getValue();
            String s = dateFormat.format(new Date(item.lastModified()));
            return new ReadOnlyObjectWrapper<String>(s);
        });

        lastModifiedColumn.setPrefWidth(130);
        lastModifiedColumn.setSortable(false);
        treeTableView.getColumns().add(lastModifiedColumn);

        treeTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            label.setText(newValue != null ? newValue.getValue().getAbsolutePath() : "");
        });

        treeTableView.getSelectionModel().selectFirst();

        return treeTableView;
    }

    private Image getImageResource(String name) {
        Image img = null;
        try {
            img = new Image(getClass().getResourceAsStream(name));
        } catch (Exception e) {
        }
        return img;
    }

    private class FileTreeItem extends TreeItem<File> {
        private boolean expanded = false;
        private boolean directory;
        private boolean hidden;
        private long length;
        private long lastModified;

        FileTreeItem(File file) {
            super(file);
            EventHandler<TreeModificationEvent<File>> eventHandler = event -> changeExpand();
            addEventHandler(TreeItem.branchExpandedEvent(), eventHandler);
            addEventHandler(TreeItem.branchCollapsedEvent(), eventHandler);

            directory = getValue().isDirectory();
            hidden = getValue().isHidden();
            length = getValue().length();
            lastModified = getValue().lastModified();
        }

        private void changeExpand() {
            if (expanded != isExpanded()) {
                expanded = isExpanded();
                if (expanded) {
                    createChildren();
                } else {
                    getChildren().clear();
                }
                if (getChildren().size() == 0)
                    Event.fireEvent(this,
                            new TreeItem.TreeModificationEvent<>(TreeItem.valueChangedEvent(), this, getValue()));
            }
        }

        @Override
        public boolean isLeaf() {
            return !isDirectory();
        }

        public boolean isDirectory() {
            return directory;
        }

        public long lastModified() {
            return lastModified;
        }

        public long length() {
            return length;
        }

        public boolean isHidden() {
            return hidden;
        }

        private void createChildren() {
            if (isDirectory() && getValue() != null) {
                File[] files = getValue().listFiles();
                if (files != null && files.length > 0) {
                    getChildren().clear();
                    for (File childFile : files) {
                        getChildren().add(new FileTreeItem(childFile));
                    }
                    getChildren().sort((ti1, ti2) -> {
                        return ((FileTreeItem) ti1).isDirectory() == ((FileTreeItem) ti2).isDirectory()
                                ? ti1.getValue().getName().compareToIgnoreCase(ti2.getValue().getName())
                                : ((FileTreeItem) ti1).isDirectory() ? -1 : 1;
                    });
                }
            }
        }
    } // end class FileTreeItem

} // end class FileBrowser
