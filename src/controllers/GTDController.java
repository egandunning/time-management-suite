package controllers;

import dialogs.GTDNewIdea;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Controller for the GTD tab.
 * @author Egan Dunning
 *
 */
public class GTDController {

	@FXML
	private Button newIdea;
	
	@FXML
	protected void initialize() {
		System.out.println("In initialize (GTD)");
	}
	
	@FXML
	protected void newIdeaAction() {
		System.out.println("new idea clicked");
		GTDNewIdea dialog = new GTDNewIdea();
		dialog.showAndWait();
	}

}
