package ui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class MainController {

    @FXML
    public TabPane tabPane;
    public Tab introTab;
    public Tab gtdTab;

    /**
     * Called when GUI loads.
     */
    @FXML
    protected void initialize() {
	System.out.println("in initialize (MainWindow)");

    }

}
