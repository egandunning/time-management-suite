package model;

import java.io.Serializable;

/**
 * Bean for storing quotes
 * @author Egan Dunning
 */
public class Quote implements Serializable {

	private static final long serialVersionUID = 1L;
	private String quote;
	private String author;

	public Quote() {}

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

	@Override
	public String toString() {
		return "Quote [quote=" + quote + ", author=" + author + "]";
	}
}
