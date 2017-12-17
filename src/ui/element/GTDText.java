package ui.element;

import javafx.scene.text.Text;
import models.GTDListItem;

/**
 * A class for storing data about list items directly in a Text element.
 * @author Egan Dunning
 *
 */
public class GTDText extends Text {

	//Stores data about the list item: deadline and description
	private GTDListItem item;
	
	public GTDText() {}
	
	public GTDText(String text) {
		super(text);
	}

	public GTDText(double x, double y, String text) {
		super(x, y, text);
	}

	public GTDListItem getItem() {
		return item;
	}

	public void setItem(GTDListItem item) {
		this.item = item;
	}

}
