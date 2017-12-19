package ui.dialog;

import java.time.LocalDateTime;
import java.time.LocalTime;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import models.GTDListItem;
import util.validation.FutureDate;
import util.validation.TimeFormat;

public class GTDListOptions {

	private OptionsDialog dialog;
	private TextField ideaField;
	private DatePicker datePicker;
	private TextField timeField;
	private CheckBox showDeadline;
	private boolean deadlineVisible = false;
	
	/**
	 * Creates and shows the dialog window in a blocking fashion.
	 * @param item The list item to edit.
	 */
	public void show(GTDListItem item) {
		
		if(item == null) {
			System.out.println("Nothing to update...");
			return;
		}
		
		datePicker = new DatePicker();
		datePicker.setMaxWidth(110);
		datePicker.setVisible(false);
		timeField = new TextField();
		timeField.setPromptText("HH:MM");
		timeField.setMaxWidth(60);
		timeField.setVisible(false);
		
		showDeadline = new CheckBox("Time sensitive?");
		
		//if deadline is set, display current values
		if(item.getDeadline() != null) {
			showDeadline.setSelected(true);
			timeField.setText(item.getDeadline().getHour() + ":" + item.getDeadline().getMinute());
			deadlineVisible = true;
			timeField.setVisible(deadlineVisible);
			datePicker = new DatePicker(item.getDeadline().toLocalDate());
			datePicker.setVisible(deadlineVisible);
		}
		
		ideaField = new TextField(item.getText());
		
		dialog = new OptionsDialog();
		
		//show dialog, keep showing until valid deadline or no deadline is entered
		do {
			dialog.showAndWait();
			dialog.setTitle("Invalid deadline!!!");
		} while(showDeadline.isSelected() &&
				(!FutureDate.validate(datePicker.getValue()) ||
						!TimeFormat.validate(timeField.getText())) );
		
		if(!showDeadline.isSelected()) {
			item.setDeadline(null);
		} else {
			item.setDeadline(LocalDateTime.of(datePicker.getValue(),
					LocalTime.parse(timeField.getText())));
		}
		if(!ideaField.getText().equals("")) {
			item.setText(ideaField.getText());
		}
	}

	/**
	 * Dialog for displaying actions to take with list items.
	 */
	private class OptionsDialog extends Alert {
		
		public OptionsDialog() {
			super(Alert.AlertType.NONE);
			
			setTitle("Edit List Item");
			
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
			
			//set up layout
			GridPane gp = new GridPane();
			gp.setHgap(10);
			gp.setVgap(10);
			
			//controls
			getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.APPLY);
			
			//add elements to layout
			gp.add(new Label("Rename list item:"), 0, 0);
			gp.add(ideaField, 1, 0, 2, 1);
			
			gp.add(showDeadline, 0, 1);
			
			gp.add(deadlineLabel, 0, 2);
			gp.add(datePicker, 1, 2);
			gp.add(timeField, 2, 2);
			
			//add layout to pane
			getDialogPane().setContent(gp);
			
		}
		
	}
}
