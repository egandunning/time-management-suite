package ui.controller;

import java.util.ArrayList;
import java.util.Arrays;

import data.persistence.Serializer;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Pair;
import ui.dialog.GTDNewIdea;

/**
 * Controller for the GTD tab.
 * @author Egan Dunning
 *
 */
public class GTDController {

	@FXML
	private Button newIdea;
	@FXML
	private ArrayList<Pair<String,VBox>> lists;
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
		
		//drag and drop listeners
		
		lists = new ArrayList<>();
		lists.addAll(Arrays.asList(
				new Pair<>("inList", inList),
				new Pair<>("nextActionsList", nextActionsList),
				new Pair<>("waitingForList", waitingForList),
				new Pair<>("projectsList", projectsList),
				new Pair<>("somedayList", somedayList)));
		
		//iterate through vbox layouts and add event listeners and persisted
		//ui elements
		for(Pair<String, VBox> list : lists) {
			
			//populate vboxes
			ObservableList<Node> storedLists = Serializer.getInstance()
					.readNodeList(list.getKey());
			
			if(storedLists != null) {

				//remove header text element
				storedLists.remove(0);
				
				//add stored list items to layouts
				list.getValue().getChildren().addAll(storedLists);
				for(Node node : list.getValue().getChildren()) {
					node.setOnDragDetected(new DragHandler<MouseEvent>((Text)node));
					node.setOnDragDone(new DragDoneHandler<MouseEvent>((Text)node));
				}
			}
			
			//drag and drop event listeners
			list.getValue().setOnDragOver(new DragOverHandler<DragEvent>());
			list.getValue().setOnDragDropped(
					new DragDroppedHandler<DragEvent>(list.getValue()));
			
			//list changed event listener
			list.getValue().getChildren()
					.addListener(new ListChangedHandler<>(list.getKey()));
		}
	}
	
	/**
	 * Event handler for saving list to file when 
	 */
	private class ListChangedHandler<T extends Node> implements ListChangeListener<T> {

		private String listName = "";
		
		public ListChangedHandler(String listName) {
			this.listName = listName;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public void onChanged(Change<? extends T> c) {
			//write updated file to date
			System.out.print(listName + " updated on disk... ");
			System.out.println(Serializer.getInstance()
					.writeNodeList((ObservableList<Node>) c.getList(), listName));
		}
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

		private VBox layout;
		
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
