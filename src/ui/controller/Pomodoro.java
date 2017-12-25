package ui.controller;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Pomodoro {

	private static final int ONE_MINUTE = 600;
	
	@FXML private Button squash;
	@FXML private ImageView tomato;
	@FXML private Text timeDisplay;
	@FXML private TextField taskCompletedField;
	@FXML private Button taskCompletedButton;
	@FXML private Slider volumeSelect;
	@FXML private Spinner<Integer> tomatoMinutes;
	@FXML private Text finishedTomatoesTitle;
	@FXML private VBox finishedTomatoes;
	
	
	//This timer is static so that the main method can cancel the running
	//timer when the user exits the program.
	private static Timer tomatoTimer;
	private int tomatoTime;
		
	@FXML
	protected void initialize() {
		
		timeDisplay.setFont(new Font(25));
		finishedTomatoesTitle.setFont(new Font(20));

		//start tomato when clicked
		tomato.setOnMouseClicked(new StartTomatoHandler<MouseEvent>());
		
		//visual feedback for hovering over/clicking on tomato
		tomato.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				//rotate one degree
				tomato.setRotate(1);
			}
		});
		tomato.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				//rotate back one degree
				tomato.setRotate(-1);
			}
		});
		tomato.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				tomato.setScaleX(1.05);
				tomato.setScaleY(.95);
			}
		});
		tomato.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				tomato.setScaleX(.95);
				tomato.setScaleY(1.05);
			}
		});
	}
	
	/**
	 * Allow user to squash(cancel) a tomato.
	 * @param event
	 */
	@FXML
	protected void squashTomato(Event event) {
		timeDisplay.setText("SQUASHED!");
		cancelTimer();
	}

	/**
	 * Event handler for starting a new tomato. Handles updating timer as
	 * time passes.
	 * @author Egan Dunning
	 *
	 * @param <T> The MouseEvent
	 */
	class StartTomatoHandler<T extends MouseEvent> implements EventHandler<T> {
		@Override
		public void handle(T event) {
			tomatoTime = tomatoMinutes.getValue();
			String s = (tomatoTime > 1)?"s":"";
			timeDisplay.setText(tomatoTime + " minute" + s + " left");
			cancelTimer();
			tomatoTimer = new Timer();
			//update time display every minute (60,000 milliseconds)
			tomatoTimer.scheduleAtFixedRate(new TomatoTimerTask(), ONE_MINUTE, ONE_MINUTE);
		}
	}
	
	/**
	 * Runs when a tomato is finished. Cancels timer and prompts user for
	 * a description of what they accomplished during the tomato. Plays an
	 * audio clip of a bell noise.
	 */
	private void tomatoFinished() {
		timeDisplay.setText("Completed!");
		//run these on the JavaFX main thread to avoid exception.
		Platform.runLater(() -> {
			taskCompletedField.setEditable(true);
			taskCompletedField.setPromptText("What did you accomplish?");
			taskCompletedButton.setDisable(false);
		});
		//play bell sound
		//https://commons.wikimedia.org/wiki/File:Ladenklingel.ogg
		new AudioClip(new File("resources/Ladenklingel.ogg.mp3").toURI().toString())
			.play(volumeSelect.getValue());
		cancelTimer();
	}
	
	/**
	 * This runs when the user ends a tomato, then submits a description
	 * of what they did during that tomato.
	 * Adds description of task to list, then starts 5 minute break.
	 */
	@FXML
	protected void submitTaskDescription() {
		finishedTomatoes.getChildren().add(new Text(taskCompletedField.getText()));
		//break time!
		tomatoTime = 5;
		String s = (tomatoTime > 1)?"s":"";
		timeDisplay.setText(tomatoTime + " minute" + s + " left");
		cancelTimer();
		tomatoTimer = new Timer();
		
		taskCompletedField.setEditable(false);
		taskCompletedButton.setDisable(true);
		//update time display every minute (60,000 milliseconds)
		tomatoTimer.scheduleAtFixedRate(new TomatoTimerTask(false), ONE_MINUTE, ONE_MINUTE);
	}
	
	/**
	 * TimerTask for handling updating time display, bell notification and
	 * action to take at the end of breaks, tomatoes.
	 * @author Egan Dunning
	 *
	 */
	class TomatoTimerTask extends TimerTask {
		
		private boolean isTomato = true;
		
		public TomatoTimerTask() {}
		
		public TomatoTimerTask(boolean isTomato) {
			this.isTomato = isTomato;
		}
		
		@Override
		public void run() {
			tomatoTime--;
			switch(tomatoTime) {
			case 0:
				if(isTomato) {
					tomatoFinished();
				} else {
					timeDisplay.setText("Break is over!");
					cancelTimer();
				}
				break;
			case 1:
				timeDisplay.setText(tomatoTime + " minute left");
				break;
			default:
				timeDisplay.setText(tomatoTime + " minutes left");
			}
		}
	}
	
	/**
	 * Cancel any timers used for timing tomatoes.
	 */
	public static void cancelTimer() {
		if(tomatoTimer != null) {
			tomatoTimer.cancel();
		}
	}
}
