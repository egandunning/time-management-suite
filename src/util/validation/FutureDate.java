package util.validation;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Class for checking if a date is in the future or today.
 * @author Egan Dunning
 *
 */
public class FutureDate {

	/**
	 * Return true if a date is in the future or is today.
	 * @param date the date to validate.
	 * @return true if the date is in the future or is today.
	 */
	public static boolean validate(LocalDate date) {
		return date.isAfter(LocalDate.now().minus(1, ChronoUnit.DAYS));
	}

}
