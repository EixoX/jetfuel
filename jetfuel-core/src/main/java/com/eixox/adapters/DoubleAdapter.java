package com.eixox.adapters;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

/**
 * An adapter that can convert, parse and format Double objects;
 * 
 * @author Rodrigo Portela
 *
 */
public class DoubleAdapter extends Adapter<Double> {

	/**
	 * Creates a new Double adapter object;
	 */
	public DoubleAdapter() {
		super(Double.class);
	}

	/**
	 * Parses the input source string into a Double object;
	 */
	@Override
	public Double parse(String source) {
		return source == null || source.isEmpty()
				? whenNull()
				: Double.valueOf(source);
	}

	/**
	 * Changes the type of the source to a Double object;
	 */
	@Override
	protected Double changeType(Class<?> sourceClass, Object source) {
		if (Number.class.isAssignableFrom(sourceClass))
			return ((Number) source).doubleValue();
		else if (Date.class.isAssignableFrom(sourceClass))
			return (double) ((Date) source).getTime();
		else if (java.sql.Date.class.isAssignableFrom(sourceClass))
			return (double) ((java.sql.Date) source).getTime();
		else if (Time.class.isAssignableFrom(sourceClass))
			return (double) ((Time) source).getTime();
		else if (Timestamp.class.isAssignableFrom(sourceClass))
			return (double) ((Timestamp) source).getTime();
		else
			return super.changeType(sourceClass, source);
	}

	/**
	 * Gets the value of a Double when the input for conversion is null;
	 */
	@Override
	public Double whenNull() {
		return 0.0;
	}

}
