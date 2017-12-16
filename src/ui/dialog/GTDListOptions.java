package ui.dialog;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class GTDListOptions {

	private OptionsDialog dialog;
	private TextField ideaField;
	
	/**
	 * Creates and shows the dialog window in a blocking fashion.
	 * @param item The list item to edit.
	 */
	public void show(String item) {
		
		dialog = new OptionsDialog();
		ideaField = new TextField(item);
		dialog.showAndWait();
	}

	/**
	 * Dialog for displaying actions to take with list items.
	 */
	private class OptionsDialog extends Alert {
		
		public OptionsDialog() {
			super(Alert.AlertType.NONE);
			
			setTitle("Edit List Item");
			
			//set up layout
			GridPane gp = new GridPane();
			gp.setHgap(10);
			gp.setVgap(10);
			
			//controls
			getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.APPLY);
			
			//add elements to layout
			gp.add(new Label("Rename list item:"), 0, 0);
			gp.add(ideaField, 1, 0);
			
			//add layout to pane
			getDialogPane().setContent(gp);
			
		}
		
	}
}
