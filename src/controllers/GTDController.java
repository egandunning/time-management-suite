package controllers;

import dialogs.GTDNewIdea;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Pair;

/**
 * Controller for the GTD tab.
 * @author Egan Dunning
 *
 */
public class GTDController {

	@FXML
	private Button newIdea;
	@FXML
	private VBox inList;
	@FXML
	private VBox nextActionsList;
	@FXML
	private VBox waitingForList;
	@FXML
	private VBox projectsList;
	@FXML
	private VBox somedayList;
	
	@FXML
	protected void initialize() {
		System.out.println("In initialize (GTD)");
	}
	
	@FXML
	protected void newIdeaAction() {
		System.out.println("new idea clicked");
		GTDNewIdea dialog = new GTDNewIdea();
		Pair<String, String> ideaAndList = dialog.getIdeaAndList();		
		
		String idea = ideaAndList.getKey();
		String list = ideaAndList.getValue();
		
		if(idea == null || list == null) {
			System.out.println("user pressed cancel");
		}
		
		addIdeaToList(idea, list);		
	}
	
	/**
	 * Add an idea to a list. List types are defined in data.GTDLists.java
	 * @param idea the idea to record
	 * @param list the list to record idea in
	 */
	private void addIdeaToList(String idea, String list) {
		
		System.out.println("idea: " + idea + " list: " + list);
		
		Text text = new Text(idea);
		
		switch(list) {
		case "in":
			inList.getChildren().add(text);
			break;
		case "next actions":
			nextActionsList.getChildren().add(text);
			break;
		case "waiting for":
			waitingForList.getChildren().add(text);
			break;
		case "projects":
			projectsList.getChildren().add(text);
			break;
		case "some day/maybe":
			somedayList.getChildren().add(text);
			break;
		}
	}

}
