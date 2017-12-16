package ui.dialog;

import java.util.Optional;

import data.GTDLists;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

public class GTDNewIdea {

	private String idea;
	private String listType;
	private ComboBox<String> list;
	
	private IdeaDialog dialog;
	private TextField ideaField;

	public GTDNewIdea() {
		
		list = new ComboBox<>(FXCollections.
				observableList(GTDLists.getInstance().getLists()));
		
		ideaField = new TextField();
		
		dialog = new IdeaDialog();
	}
	
	/**
	 * Show dialog and return the idea text and list type.
	 * @return a Pair of strings containing the idea and the list
	 * to put the idea in, or null if the user presses cancel.
	 */
	public Pair<String, String> getIdeaAndList() {
		Optional<ButtonType> choice = dialog.showAndWait();
		idea = ideaField.getText();
		listType = list.getValue();
		if(choice.isPresent() && idea != null && listType != null) {
			return new Pair<String, String>(idea, listType);
		}
		return null;
	}
	
	/**
	 * Dialog to get the user's idea and which list to put it in.
	 */
	private class IdeaDialog extends Alert {
		public IdeaDialog() {
			super(Alert.AlertType.NONE);
			setTitle("Record New Idea");

			// set up layout
			GridPane gp = new GridPane();
			gp.setHgap(10);
			gp.setVgap(10);

			//controls
			getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);

			// add elements to layout
			gp.add(new Label("Pick a list to add to:"), 0, 0);
			gp.add(list, 1, 0);

			gp.add(new Label("The idea:"), 0, 1);
			gp.add(ideaField, 1, 1);
			
			// add layout to pane
			getDialogPane().setContent(gp);
		}
	}
}
