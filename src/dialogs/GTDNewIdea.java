package dialogs;

import data.GTDLists;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class GTDNewIdea extends Alert {

	public GTDNewIdea() {
		super(Alert.AlertType.CONFIRMATION);
		setTitle("Record New Idea");
		setHeaderText(""); //get that ugly header outta here!
		setGraphic(new Label()); //same for the icon!
		GridPane gp = new GridPane();
		gp.setHgap(10);
		gp.setVgap(10);
		
		ComboBox<String> list = new ComboBox<>(
				FXCollections.observableList(
						GTDLists.getInstance().getLists()));
		
		gp.add(new Label("Pick a list to add to:"), 0, 0);
		gp.add(list, 1, 0);
		
		gp.add(new Label("The idea:"), 0, 1);
		gp.add(new TextField(), 1, 1);
		
		getDialogPane().setContent(gp);
	}

}
