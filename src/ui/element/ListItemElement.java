package ui.element;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import javafx.scene.text.Text;
import models.GTDListItem;

public class ListItemElement {

	private Text text;
	private LocalDateTime deadline;
	
	public ListItemElement(GTDListItem item) {
		
		deadline = item.getDeadline();
		
		String timeUntilDeadline = timeUntilDeadline(deadline);
		
		setText(new Text(item.getText() + ": do it in " + timeUntilDeadline));
		
	}

	/**
	 * Return the time from now until the given deadline in a readable string.
	 * For example: "8 days and 1 hour"
	 * @param deadline The deadline to get time until.
	 * @return String representation of the time from now until deadline.
	 */
	private String timeUntilDeadline(LocalDateTime deadline) {
		
		LocalDateTime now = LocalDateTime.now();
		
		//find number of days until deadline
		long hours = now.until(deadline, ChronoUnit.HOURS);
		//determine if "hour" should be plural
		String hourPlural = (hours > 1) ? "s" : "";
		
		//if time until deadline is more than one day, calculate number of days 
		if(hours > 24) {
			
			long days = now.until(deadline, ChronoUnit.DAYS);
			//don't double count hours!
			hours -= days * 24;
			
			//determine if "day" should be plural
			String dayPlural = (days > 1) ? "s" : "";
			return String.format("%d day%s, %d hour%s", days, dayPlural, hours, hourPlural);//days + " day" + dayPlural + ", " + hours + " hour" + hourPlural;
		} else {
			return hours + " hour" + hourPlural;
		}
	}

	public Text getText() {
		return text;
	}

	public void setText(Text text) {
		this.text = text;
	}

	public LocalDateTime getDeadline() {
		return deadline;
	}
	
	public void setDeadline(LocalDateTime deadline) {
		this.deadline = deadline;
	}
}
