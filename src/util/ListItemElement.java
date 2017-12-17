package util;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import models.GTDListItem;
import ui.element.GTDText;

/**
 * Class to create Text elements from the GTDListItem bean.
 * @author Egan Dunning
 */
public class ListItemElement {

	public static GTDText generate(GTDListItem item) {
		
		if(item == null) {
			return null;
		}
		
		GTDText text;
		LocalDateTime deadline;
		
		deadline = item.getDeadline();
		
		if(deadline != null) {
			String timeUntilDeadline = timeUntilDeadline(deadline);
			text = new GTDText(item.getText() + ": do it in " + timeUntilDeadline);
		} else {
			text = new GTDText(item.getText());
		}
		
		//set field to store data about list item.
		text.setItem(new GTDListItem(item.getText(), item.getDeadline()));
		return text;
	}

	/**
	 * Return the time from now until the given deadline in a readable string.
	 * For example: "8 days and 1 hour"
	 * @param deadline The deadline to get time until.
	 * @return String representation of the time from now until deadline.
	 */
	private static String timeUntilDeadline(LocalDateTime deadline) {
		
		if(deadline == null) {
			return "";
		}
		
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
}
