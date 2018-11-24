package util.validation;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;

/**
 * Class to validate a string representation of a time
 * 
 * @author Egan Dunning
 */
public class TimeFormat {

    /**
     * Return true if the given string is a valid time.
     * 
     * @param time
     *            the string to check.
     * @return true if the string couldn't be parsed to a time.
     */
    public static boolean validate(String time) {
	try {
	    LocalTime.parse(time);
	    return true;
	} catch (DateTimeParseException e) {
	    return false;
	}
    }
}
