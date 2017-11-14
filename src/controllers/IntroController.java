package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;

public class IntroController {

	@FXML
	public TabPane tabPane;
	public Tab introTab;
	
	/**
	 * Called when GUI loads. Loads FXML file and inserts into the intro tab
	 */
	@FXML
	protected void initialize() {
		System.out.println("in initialize");
		try {
			GridPane introNode = FXMLLoader.load(getClass().getResource("/intro_page.fxml"));
			introTab.setContent(introNode);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
}
