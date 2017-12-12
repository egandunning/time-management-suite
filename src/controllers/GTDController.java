package controllers;

import dialogs.GTDNewIdea;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
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
	Text inListDrop;
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
		
		//drag and drop listeners
		inList.setOnDragOver(new DragOverHandler<DragEvent>());
		inList.setOnDragDropped(new DragDroppedHandler<DragEvent>(inList));
		
		nextActionsList.setOnDragOver(new DragOverHandler<DragEvent>());
		nextActionsList.setOnDragDropped(new DragDroppedHandler<DragEvent>(nextActionsList));
		
		waitingForList.setOnDragOver(new DragOverHandler<DragEvent>());
		waitingForList.setOnDragDropped(new DragDroppedHandler<DragEvent>(waitingForList));
		
		projectsList.setOnDragOver(new DragOverHandler<DragEvent>());
		projectsList.setOnDragDropped(new DragDroppedHandler<DragEvent>(projectsList));
		
		somedayList.setOnDragOver(new DragOverHandler<DragEvent>());
		somedayList.setOnDragDropped(new DragDroppedHandler<DragEvent>(somedayList));
	}
	/**
	 * Class for handling drag over events - needed for multiple layouts
	 * @param <T> the event type - should be DragEvent
	 */
	private class DragOverHandler<T extends Event> implements EventHandler<T> {
		@Override
		public void handle(Event event) {
			((DragEvent)event).acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }	
	}
	
	/**
	 * Class for handling drop events - needed to move items from one layout
	 * to another
	 * @param <T> the event type - should be DragEvent
	 */
	private class DragDroppedHandler<T extends Event> implements EventHandler<T> {

		VBox layout;
		
		public DragDroppedHandler(VBox layout) {
			this.layout = layout;
		}
		
		@Override
		public void handle(Event event) {
			layout.getChildren().add(new Text(((DragEvent)event).getDragboard().getString()));
			
			Text lastElement = (Text) layout.getChildren().get(layout.getChildren().size()-1);
			lastElement.setOnDragDetected(new DragHandler<MouseEvent>(lastElement));
			lastElement.setOnDragDone(new DragDoneHandler<MouseEvent>(lastElement));
			((DragEvent)event).setDropCompleted(true);
		}
	}
	
	/**
	 * Class for handling drag events from source node.
	 * @param <T> the event type - should be DragEvent
	 */
	private class DragHandler<T extends Event> implements EventHandler<T> {

		private Text text;
		
		public DragHandler(Text text) {
			this.text = text;
		}
		
		@Override
		public void handle(Event event) {
			Dragboard db = text.startDragAndDrop(TransferMode.MOVE);
			ClipboardContent content = new ClipboardContent();
			content.putString(text.getText());
			db.setContent(content);
			event.consume();
		}
	}
	
	/**
	 * Deletes source element from list when drag and drop is successful.
	 */
	private class DragDoneHandler<T extends Event> implements EventHandler<DragEvent> {

		private Text text;
		
		public DragDoneHandler(Text text) {
			this.text = text;
		}
		
		@Override
		public void handle(DragEvent event) {
			text.setText("");
			((VBox)text.getParent()).getChildren().remove(text);
		}
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
		
		text.setOnDragDetected(new DragHandler<MouseEvent>(text));
		
		text.setOnDragDone(new DragDoneHandler<MouseEvent>(text));
		
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
