package ui.controller;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Pomodoro {

	@FXML private Button squash;
	@FXML private ImageView tomato;
	@FXML private Text timeDisplay;
	@FXML private TextField taskCompletedField;
	@FXML private Slider volumeSelect;
	@FXML private Spinner<Integer> tomatoMinutes;
	
	//This timer is static so that the main method can cancel the running
	//timer when the user exits the program.
	private static Timer tomatoTimer;
	private int tomatoTime;
	
	@FXML
	protected void initialize() {
		
		timeDisplay.setFont(new Font(25));
		
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
			timeDisplay.setText(tomatoTime + " minutes left");
			cancelTimer();
			tomatoTimer = new Timer();
			//update time display every minute (60,000 milliseconds)
			tomatoTimer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					tomatoTime--;
					switch(tomatoTime) {
					case 0: //tomato finished
						timeDisplay.setText("Completed!");
						//play bell sound
						//https://commons.wikimedia.org/wiki/File:Ladenklingel.ogg
						new AudioClip(new File("resources/Ladenklingel.ogg.mp3").toURI().toString())
							.play(volumeSelect.getValue());
						cancelTimer();
						break;
					case 1:
						timeDisplay.setText(tomatoTime + " minute left");
						break;
					default:
						timeDisplay.setText(tomatoTime + " minutes left");
					}
				}
			}, 60000, 60000);
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
