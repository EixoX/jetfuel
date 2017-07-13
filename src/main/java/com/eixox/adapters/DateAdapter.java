package com.eixox.adapters;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A generic date adapter that can read and write date strings.
 * 
 * @author Rodrigo Portela
 *
 */
public class DateAdapter extends Adapter<Date> {

	/**
	 * The date format to use for parsing and formatting dates.
	 */
	public final DateFormat format;

	/**
	 * Creates a new date adapter with the default date and time format
	 * instance.
	 */
	public DateAdapter() {
		this(DateFormat.getDateTimeInstance());
	}

	/**
	 * Creates a new date adapter with the provided format.
	 * 
	 * @param format
	 */
	public DateAdapter(DateFormat format) {
		super(Date.class);
		this.format = format;
	}

	/**
	 * Creates a new date adapter with the providaded format string.
	 * 
	 * @param format
	 */
	public DateAdapter(String format) {
		this(new SimpleDateFormat(format));
	}

	/**
	 * Creates a new date adapter with the providade annotations.
	 * 
	 * @param options
	 */
	public DateAdapter(UseAdapter options) {
		super(Date.class);
		Locale locale = Locale.forLanguageTag(options.language());
		this.format = options.format().isEmpty()
				? DateFormat.getDateTimeInstance()
				: new SimpleDateFormat(options.format(), locale);
	}

	/**
	 * Formats a date object using the formatter on this instance.
	 */
	@Override
	public String format(Date source) {
		return source == null
				? ""
				: this.format.format(source);
	}

	/**
	 * Parses the date object from the provided string using this instance's
	 * format.
	 */
	@Override
	public Date parse(String source) {
		try {
			return source == null || source.isEmpty()
					? null
					: this.format.parse(source);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Changes the type of another object to a date.
	 */
	@Override
	protected Date changeType(Class<?> sourceClass, Object source) {
		if (Calendar.class.isAssignableFrom(sourceClass))
			return ((Calendar) source).getTime();
		else if (Number.class.isAssignableFrom(sourceClass))
			return new Date(((Number) source).longValue());
		else
			return super.changeType(sourceClass, source);
	}

	/**
	 * Gets today as a date;
	 * 
	 * @return
	 */
	public static final Date today() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * Parses a date with a specific format pattern;
	 * 
	 * @param input
	 * @param format
	 * @return
	 * @throws ParseException
	 */
	public static final Date parse(String input, String format) throws ParseException {
		if (input == null || input.isEmpty())
			return null;

		if (input.length() > format.length())
			input = input.substring(0, format.length());
		else if (input.length() < format.length())
			format = format.substring(0, input.length());

		return new SimpleDateFormat(format).parse(input);

	}

}
