package controllers;

import javafx.fxml.FXML;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import model.Quote;
import web.QuoteConsumer;

public class InfoTab {

	
	//This references the Text tag with the fx:id "quoteOfTheDay"
	@FXML
	public Text quoteOfTheDay;
	
	/**
	 * Called when GUI loads. Loads FXML file and inserts into the intro tab
	 */
	@FXML
	protected void initialize() {
		System.out.println("in initialize (InfoTab)");
		
		//get quote of the day
		Quote q = new Quote();
		quoteOfTheDay.setFont(Font.font("Times", FontPosture.ITALIC, 20));
		try {
			q = QuoteConsumer.getInstance().getQuoteOfTheDay();
			//We dont want our quote to say null
			if(q == null || q.getAuthor() == null || q.getQuote() == null) {
				throw new NullPointerException("Quote was null!!!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			q = new Quote();
			q.setQuote("\"Whoops! Error retrieving quote of the day.\"");
			q.setAuthor("Egan");
		}
		//set value of quote in fxml Text tag
		quoteOfTheDay.setText(q.getQuote() + " - " + q.getAuthor());
	}
}
