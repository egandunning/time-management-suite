package ui.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
	
	private Timer tomatoTimer;
	private static final ArrayList<Timer> TIMERS = new ArrayList<>();
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
				tomato.setRotate(1);
			}
		});
		tomato.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
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
	
	@FXML
	protected void squashTomato(Event event) {
		timeDisplay.setText("SQUASHED!");
		tomatoTimer.cancel();
	}

	/**
	 * Event handler for starting a new tomato
	 * @author Egan Dunning
	 *
	 * @param <T> The MouseEvent
	 */
	class StartTomatoHandler<T extends MouseEvent> implements EventHandler<T> {

		@Override
		public void handle(T event) {
			tomatoTime = 3;
			timeDisplay.setText(tomatoTime + " minutes left");
			tomatoTimer = new Timer();
			//add timer to list of timer objects, used for canceling timers.
			TIMERS.add(tomatoTimer);
			//update time display every minute
			tomatoTimer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					tomatoTime--;
					switch(tomatoTime) {
					case 0:
						timeDisplay.setText("Completed!");
						new AudioClip(new File("resources/Ladenklingel.ogg.mp3").toURI().toString()).play();
						tomatoTimer.cancel();
						break;
					case 1:
						timeDisplay.setText(tomatoTime + " minute left");
						break;
					default:
						timeDisplay.setText(tomatoTime + " minutes left");
					}
				}
			}, 6000, 6000);
		}
	}

	/**
	 * Cancel any timers used for timing tomatoes.
	 */
	public static void cancelTimer() {
		for(Timer t : TIMERS) {
			t.cancel();
		}
	}
}
