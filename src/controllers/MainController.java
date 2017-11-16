package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;

public class MainController {

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
			//Load intro page
			GridPane introNode = FXMLLoader.load(getClass().getResource("/intro_page.fxml"));
			introTab.setContent(introNode);
			//done loading intro page
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
}
