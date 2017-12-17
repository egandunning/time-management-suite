package ui.element;

import javafx.scene.text.Text;
import models.GTDListItem;

public class GTDText extends Text {

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
