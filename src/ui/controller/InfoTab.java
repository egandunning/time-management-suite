package ui.controller;

import data.QuoteConsumer;
import javafx.fxml.FXML;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import models.Quote;

public class InfoTab {

    // This references the Text tag with the fx:id "quoteOfTheDay"
    @FXML
    public Text quoteOfTheDay;

    Quote q;

    /**
     * Called when GUI loads. Loads FXML file and inserts into the intro tab
     */
    @FXML
    protected void initialize() {
	System.out.println("in initialize (InfoTab)");

	// get quote of the day
	q = new Quote();
	quoteOfTheDay.setFont(Font.font("Times", FontPosture.ITALIC, 20));

	quoteOfTheDay.setText("Waiting for network...");

	// Get quote of the day asynchronously to avoid blocking GUI thread
	Thread getQuote = new Thread(new Runnable() {

	    @Override
	    public void run() {
		try {
		    // get quote of the day
		    q = QuoteConsumer.getInstance().getQuoteOfTheDay();
		    if (q == null || q.getAuthor() == null || q.getQuote() == null) {
			throw new NullPointerException("Quote was null!!!");
		    }

		} catch (Exception e) {
		    // We don't want our quote to say null
		    e.printStackTrace();
		    q = new Quote();
		    q.setQuote("\"Whoops! Error retrieving quote of the day.\"");
		    q.setAuthor("Egan");
		}
		// set value of quote in fxml Text tag
		quoteOfTheDay.setText(q.getQuote() + "\n- " + q.getAuthor());
	    }
	});

	// start thread
	getQuote.start();

    }
}
