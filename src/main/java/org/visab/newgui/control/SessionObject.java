package org.visab.newgui.control;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class SessionObject extends Application {

	private static final Image closeIcon = new Image("/img/closeIcon.png", 12, 12, false, false);
	private static final Image settlersLogo = new Image("/img/settlersLogo.png", 24, 24, false, false);
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		// TODO Auto-generated method stub
		GridPane innerGrid = new GridPane();
		innerGrid.setPadding(new Insets(10));
	    innerGrid.setHgap(5);
	    innerGrid.setVgap(5);
	    //Setting the style CSS 
	    innerGrid.getStyleClass().add("innerGrid");

		
		ImageView view = new ImageView(closeIcon);
		Button closeButton = new Button();
		//Setting the size of the button
	    closeButton.setPrefSize(10, 10);
	    //Setting a graphic to the button
	    closeButton.setGraphic(view);
	    closeButton.getStyleClass().add("delete-button");
		
		Label gameName = new Label("Name");
		ImageView gameIcon = new ImageView(settlersLogo);
		Label sessionID = new Label("SessionID");
		Label gameStatus = new Label("Status");
		Button openButton = new Button("Open Live View");
		
		innerGrid.add(closeButton,1,0);
		innerGrid.add(gameName,0,1);
		innerGrid.add(gameIcon,1,1);
		innerGrid.add(sessionID,0,2);
		innerGrid.add(gameStatus,0,3);
		innerGrid.add(openButton,0,4);
		
		Scene scene = new Scene(innerGrid);
		scene.getStylesheets().add("/template_style.css");
		primaryStage.setScene(scene);
	    primaryStage.setTitle("GridPane Beispiel");
	    primaryStage.show();
		
	}
}
