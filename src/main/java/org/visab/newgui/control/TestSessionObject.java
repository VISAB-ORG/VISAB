package org.visab.newgui.control;

import org.visab.newgui.AppMain;

import javafx.application.Application;

public class TestSessionObject {
	
	public static void main(String[] args) {

		new Thread(() -> Application.launch(SessionObject.class)).start();
	}

}
