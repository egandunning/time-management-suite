package ui.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import data.persistence.Serializer;
import javafx.collections.ListChangeListener;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import models.GTDListItem;
import ui.dialog.GTDNewIdea;
import ui.element.GTDText;
import util.ListItemElement;

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
	
	//DataFormat for reading GTD list items from drag n drop clipboard
	private static final DataFormat GTD_LIST_ITEM = new DataFormat("GTDListItem");
	
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
				new Pair<>("somedayList", somedayList) ));
		
		//iterate through vbox layouts and add event listeners and persisted
		//ui elements
		for(Pair<String,VBox> list : lists) {
			
			List<GTDText> storedLists = Serializer.getInstance()
					.readGTDList(list.getKey());
			
			if(storedLists != null) {
				
				//add stored list items to layouts
				for(GTDText item : storedLists) {
					list.getValue().getChildren().add(item);
					item.setOnDragDetected(new DragHandler<MouseEvent>(item));
					item.setOnDragDone(new DragDoneHandler<MouseEvent>(item));
				}
			}
			
			//drag and drop event listeners
			list.getValue().setOnDragOver(new DragOverHandler<DragEvent>());
			list.getValue().setOnDragDropped(
					new DragDroppedHandler<DragEvent>(list.getValue()));
			
			//list changed event listener
			list.getValue().getChildren()
					.addListener(new ListChangedHandler<Node>(list.getKey()));
		}
	}
	
	/**
	 * Event handler for saving list to file when list is updated
	 */
	private class ListChangedHandler<T extends Node> implements ListChangeListener<T> {

		private String listName = "";
		
		public ListChangedHandler(String listName) {
			this.listName = listName;
		}
		
		@Override
		public void onChanged(Change<? extends T> c) {
			//write updated file to date
			System.out.print(listName + " updated on disk... ");
			
			//Create list of items to write to file
			ArrayList<GTDText> serializableList = new ArrayList<>();
			for(Node n : c.getList()) {
				
				//add node to list of list items to serialize
				if(n instanceof GTDText) {
					serializableList.add((GTDText)n);
				}
			}
			
			//write to file and print status
			System.out.println(Serializer.getInstance()
					.writeGTDList(serializableList, listName));
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
			layout.getChildren().add(ListItemElement.generate((GTDListItem) (((DragEvent)event).getDragboard().getContent(GTD_LIST_ITEM))));
			
			GTDText lastElement = (GTDText) layout.getChildren().get(layout.getChildren().size()-1);
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

		private GTDText text;
		
		public DragHandler(GTDText text) {
			this.text = text;
		}
		
		@Override
		public void handle(Event event) {
			Dragboard db = text.startDragAndDrop(TransferMode.MOVE);
			ClipboardContent content = new ClipboardContent();
			content.put(GTD_LIST_ITEM, text.getItem());
			db.setContent(content);
			event.consume();
		}
	}
	
	/**
	 * Deletes source element from list when drag and drop is successful.
	 */
	private class DragDoneHandler<T extends Event> implements EventHandler<DragEvent> {

		private GTDText text;
		
		public DragDoneHandler(GTDText text) {
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
		Pair<GTDText, String> ideaAndList = dialog.getIdeaAndList();		
		
		//user didn't enter any values 
		if(ideaAndList == null) {
			return;
		}
		
		GTDText idea = ideaAndList.getKey();
		String list = ideaAndList.getValue();

		addIdeaToList(idea, list);		
	}
	
	/**
	 * Add an idea to a list. List types are defined in data.GTDLists.java
	 * @param idea the idea to record
	 * @param list the list to record idea in
	 */
	private void addIdeaToList(GTDText idea, String list) {
		
		System.out.println("idea: " + idea + " list: " + list);
		
		idea.setOnDragDetected(new DragHandler<MouseEvent>(idea));
		
		idea.setOnDragDone(new DragDoneHandler<MouseEvent>(idea));
		
		switch(list) {
		case "in":
			inList.getChildren().add(idea);
			break;
		case "next actions":
			nextActionsList.getChildren().add(idea);
			break;
		case "waiting for":
			waitingForList.getChildren().add(idea);
			break;
		case "projects":
			projectsList.getChildren().add(idea);
			break;
		case "some day/maybe":
			somedayList.getChildren().add(idea);
			break;
		}
	}
}
