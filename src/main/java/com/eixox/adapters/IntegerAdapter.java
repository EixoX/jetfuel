package com.eixox.adapters;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

/**
 * An adapter that can convert, parse and format Integer objects;
 * 
 * @author Rodrigo Portela
 *
 */
public class IntegerAdapter extends Adapter<Integer> {

	/**
	 * Creates a new Integer adapter object;
	 */
	public IntegerAdapter() {
		super(Integer.class);
	}

	/**
	 * Parses the input source string into a Integer object;
	 */
	@Override
	public Integer parse(String source) {
		return source == null || source.isEmpty()
				? whenNull()
				: new Integer(source);
	}

	/**
	 * Changes the type of the source to a Integer object;
	 */
	@Override
	protected Integer changeType(Class<?> sourceClass, Object source) {
		if (Number.class.isAssignableFrom(sourceClass))
			return ((Number) source).intValue();
		else if (Date.class.isAssignableFrom(sourceClass))
			return (int) ((Date) source).getTime();
		else if (java.sql.Date.class.isAssignableFrom(sourceClass))
			return (int) ((java.sql.Date) source).getTime();
		else if (Time.class.isAssignableFrom(sourceClass))
			return (int) ((Time) source).getTime();
		else if (Timestamp.class.isAssignableFrom(sourceClass))
			return (int) ((Timestamp) source).getTime();
		else
			return super.changeType(sourceClass, source);
	}

	/**
	 * Gets the value of a Integer when the input for conversion is null;
	 */
	@Override
	public Integer whenNull() {
		return 0;
	}

}
