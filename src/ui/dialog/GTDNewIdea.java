package ui.dialog;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import data.GTDLists;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import models.GTDListItem;
import ui.element.GTDText;
import util.ListItemElement;

public class GTDNewIdea {

	private GTDText idea;
	private String listType;
	private ComboBox<String> list;
	
	private IdeaDialog dialog;
	private TextField ideaField;
	private DatePicker datePicker;
	private TextField timeField;
	private CheckBox showDeadline;

	public GTDNewIdea() {
		
		list = new ComboBox<>(FXCollections.
				observableList(GTDLists.getInstance().getLists()));
		
		datePicker = new DatePicker();
		datePicker.setMaxWidth(110);
		datePicker.setVisible(false);
		timeField = new TextField();
		timeField.setPromptText("HH:MM");
		timeField.setMaxWidth(60);
		timeField.setVisible(false);
		
		showDeadline = new CheckBox("Time sensitive?");
		
		ideaField = new TextField();		
		dialog = new IdeaDialog();
		
	}
	
	/**
	 * Show dialog and return the idea text and list type.
	 * @return a Pair of strings containing the idea and the list
	 * to put the idea in, or null if the user presses cancel.
	 */
	public Pair<GTDText, String> getIdeaAndList() {
		Optional<ButtonType> choice = dialog.showAndWait();
		
		//create new list item from info entered by user
		GTDListItem item = new GTDListItem();
		item.setText(ideaField.getText());
		
		//if the user entered a deadline, set deadline field
		if(showDeadline.isSelected() && datePicker.getValue() != null) {
			item.setDeadline(LocalDateTime.of(datePicker.getValue(),
					LocalTime.parse(timeField.getText())));
		}
		
		idea = ListItemElement.generate(item);
		listType = list.getValue();
		if(choice.isPresent() && choice.get().equals(ButtonType.OK) && idea != null && listType != null) {
			return new Pair<GTDText, String>(idea, listType);
		}
		return null;
	}
	
	/**
	 * Dialog to get the user's idea and which list to put it in.
	 */
	private class IdeaDialog extends Alert {
		
		boolean deadlineVisible = false;
		
		public IdeaDialog() {
			super(Alert.AlertType.NONE);
			setTitle("Record New Idea");

			Label deadlineLabel = new Label("The deadline:");
			deadlineLabel.setVisible(deadlineVisible);
			
			//checkbox for showing/hiding deadline
			showDeadline.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					deadlineVisible = !deadlineVisible;
					datePicker.setVisible(deadlineVisible);
					timeField.setVisible(deadlineVisible);
					deadlineLabel.setVisible(deadlineVisible);
				}
			});
			
			//validate time field
			timeField.focusedProperty().addListener(new ChangeListener<Boolean>() {

				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
					if(!newValue) {
						//TODO: validation logic
					}
				}
			});
			
			// set up layout
			GridPane gp = new GridPane();
			gp.setHgap(10);
			gp.setVgap(10);

			//controls
			getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);

			// add elements to layout
			gp.add(new Label("Pick a list to add to:"), 0, 0);
			//column 1, row 0, span 2 columns
			gp.add(list, 1, 0, 2, 1);

			gp.add(new Label("The idea:"), 0, 1);
			gp.add(ideaField, 1, 1, 2, 1);
			
			gp.add(showDeadline, 0, 2);
			
			gp.add(deadlineLabel, 0, 3);
			gp.add(datePicker, 1, 3);
			gp.add(timeField, 2, 3);
			
			// add layout to pane
			getDialogPane().setContent(gp);
		}
	}
}
