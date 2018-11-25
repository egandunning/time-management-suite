package models;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Bean for storing quotes
 * 
 * @author Egan Dunning
 */
public class Quote implements Serializable {

    private static final long serialVersionUID = 2L;
    private String quote;
    private String author;
    private LocalDate retrieveDate;

    public Quote() {
    }

    public String getQuote() {
	return quote;
    }

    public void setQuote(String quote) {
	this.quote = quote;
    }

    public String getAuthor() {
	return author;
    }

    public void setAuthor(String author) {
	this.author = author;
    }
    
    public LocalDate getRetrieveTime() {
	return retrieveDate;
    }

    public void setRetrieveTime(LocalDate retrieveTime) {
	this.retrieveDate = retrieveTime;
    }
    
    @Override
    public String toString() {
	return "Quote [quote=" + quote + ", author=" + author + ", retrieveDate=" + retrieveDate + "]";
    } 
}
