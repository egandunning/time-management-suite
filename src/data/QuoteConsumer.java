package data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import data.persistence.Serializer;
import models.Quote;

/**
 * Class to consume the quote of the day public API provided by
 * https://theysaidso.com/
 * 
 * @author Egan Dunning
 *
 */
public class QuoteConsumer {

    private static QuoteConsumer qc = null;

    private URL url;
    private HttpURLConnection conn;

    Quote quote;

    private QuoteConsumer() throws IOException {
	url = new URL("http://quotes.rest/qod.json");
	conn = (HttpURLConnection) url.openConnection();
	conn.setRequestMethod("GET");
	conn.setRequestProperty("Accept-Encoding", "application/json; charset=utf-8");
    }

    /**
     * Get instance of QuoteConsumer.
     * 
     * @return instance of QuoteConsumer. If constructor throws exception, returns
     *         null
     */
    public static QuoteConsumer getInstance() {
	if (qc == null) {
	    try {
		qc = new QuoteConsumer();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}

	return qc;
    }

    /**
     * Get the quote of the day from the They Said So Quotes API.
     * 
     * @return Quote object received from API. Returns null if status is not 200.
     */
    public Quote getQuoteOfTheDay() throws IOException {
	
	try {
    	Quote cachedQuote = getCachedQuote();
    	
    	if(cachedQuote != null) {
    	    return cachedQuote;
    	}
	}
    catch(Exception e) {
    	System.out.println("No cached quotes");
    }
	
	int status = conn.getResponseCode();

	if (status != 200) {

	    System.out.println("Status code from quotes.rest: " + status);
	    return null;
	}
	BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));

	// Could use a JSON parser, but it isn't worth it in this case
	String line;
	Quote quote = new Quote();
	short flag = 0;
	while ((line = in.readLine()) != null) {
	    if (flag == 2) {
		break;
	    }
	    line = line.trim();
	    System.out.println(line);

	    if (line.length() < 8) {
		continue;
	    }

	    if (line.substring(0, 7).equals("\"quote\"")) {
		System.out.println("quote");
		quote.setQuote(line.substring(9, line.length() - 1));
		flag++;

	    } else if (line.substring(0, 8).equals("\"author\"")) {
		System.out.println("author");
		quote.setAuthor(line.substring(11, line.length() - 2));
		flag++;
	    }
	}
	
	quote.setRetrieveTime(LocalDate.now());
	
	Serializer s = Serializer.getInstance();
	s.write(quote, "quote", true);
	
	return quote;
    }
    
    /**
     * Returns the cached quote. If the quote on disk is not from today, or
     * doesn't exist, return null.
     * @return A Quote object, or null if quote is stale.
     */
    public Quote getCachedQuote() {
	
	Quote cachedQuote;
	
	Serializer s = Serializer.getInstance();
	Serializable obj = s.read("quote");
	if(obj != null && obj instanceof Quote) {
	    cachedQuote = (Quote)obj;
	    LocalDate fetchDate = cachedQuote.getRetrieveTime();
	    if(fetchDate.equals(LocalDate.now())) {
		System.out.println("using cached quote");
		return cachedQuote;
	    }
	}
	return null;
    }
}
