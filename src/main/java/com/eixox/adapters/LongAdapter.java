package com.eixox.adapters;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

/**
 * An adapter that can convert, parse and format Long objects;
 * 
 * @author Rodrigo Portela
 *
 */
public class LongAdapter extends Adapter<Long> {

	/**
	 * Creates a new Long adapter object;
	 */
	public LongAdapter() {
		super(Long.class);
	}

	/**
	 * Parses the input source string into a Long object;
	 */
	@Override
	public Long parse(String source) {
		return source == null || source.isEmpty()
				? whenNull()
				: new Long(source);
	}

	/**
	 * Changes the type of the source to a Long object;
	 */
	@Override
	protected Long changeType(Class<?> sourceClass, Object source) {
		if (Number.class.isAssignableFrom(sourceClass))
			return ((Number) source).longValue();
		else if (Date.class.isAssignableFrom(sourceClass))
			return ((Date) source).getTime();
		else if (java.sql.Date.class.isAssignableFrom(sourceClass))
			return ((java.sql.Date) source).getTime();
		else if (Time.class.isAssignableFrom(sourceClass))
			return ((Time) source).getTime();
		else if (Timestamp.class.isAssignableFrom(sourceClass))
			return ((Timestamp) source).getTime();
		else
			return super.changeType(sourceClass, source);
	}

	/**
	 * Gets the value of a Long when the input for conversion is null;
	 */
	@Override
	public Long whenNull() {
		return 0L;
	}

}
