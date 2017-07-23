package com.eixox.adapters;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * An adapter for Number objects;
 * 
 * @author Rodrigo Portela
 *
 */
public class NumberAdapter extends Adapter<Number> {

	/**
	 * Gets the number format used by this adapter;
	 */
	public final NumberFormat format;

	/**
	 * Creates a new number adapter;
	 * 
	 * @param format
	 */
	public NumberAdapter(NumberFormat format) {
		super(Number.class);
		this.format = format;
	}

	/**
	 * Creates a new number adapter;
	 * 
	 * @param format
	 */
	public NumberAdapter(String format) {
		this(new DecimalFormat(format));
	}

	/**
	 * Creates a new number adapter;
	 * 
	 * @param format
	 */
	public NumberAdapter() {
		this(NumberFormat.getNumberInstance());
	}

	/**
	 * Parses the input source into a Number object;
	 */
	@Override
	public Number parse(String source) {
		try {
			return source == null || source.isEmpty()
					? whenNull()
					: this.format.parse(source);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Formats the expected Number input into a string;
	 */
	@Override
	public String format(Number source) {
		return source == null
				? ""
				: this.format.format(source);
	}

	/**
	 * Changes the input source object to a Number;
	 */
	@Override
	protected Number changeType(Class<?> sourceClass, Object source) {
		if (Date.class.isAssignableFrom(sourceClass))
			return ((Date) source).getTime();
		else if (java.sql.Date.class.isAssignableFrom(sourceClass))
			return ((java.sql.Date) source).getTime();
		else if (Timestamp.class.isAssignableFrom(sourceClass))
			return ((Timestamp) source).getTime();
		else if (Time.class.isAssignableFrom(sourceClass))
			return ((Time) source).getTime();
		else
			return super.changeType(sourceClass, source);
	}

}