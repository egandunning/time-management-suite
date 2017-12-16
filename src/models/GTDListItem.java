package models;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A class to represent items in GTD lists.
 * @author Egan Dunning
 *
 */
public class GTDListItem implements Serializable {

	private static final long serialVersionUID = 1L;
	private String text;
	private LocalDateTime deadline;
	
	public GTDListItem() {}
	
	public GTDListItem(String text, LocalDateTime deadline) {
		this.text = text;
		this.deadline = deadline;
	} 
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public LocalDateTime getDeadline() {
		return deadline;
	}

	public void setDeadline(LocalDateTime deadline) {
		this.deadline = deadline;
	}

}
