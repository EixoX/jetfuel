package com.eixox.adapters;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

/**
 * An adapter that can convert, parse and format Short objects;
 * 
 * @author Rodrigo Portela
 *
 */
public class ShortAdapter extends Adapter<Short> {

	/**
	 * Creates a new Short adapter object;
	 */
	public ShortAdapter() {
		super(Short.class);
	}

	/**
	 * Parses the input source string into a Short object;
	 */
	@Override
	public Short parse(String source) {
		return source == null || source.isEmpty()
				? whenNull()
				: new Short(source);
	}

	/**
	 * Changes the type of the source to a Short object;
	 */
	@Override
	protected Short changeType(Class<?> sourceClass, Object source) {
		if (Number.class.isAssignableFrom(sourceClass))
			return ((Number) source).shortValue();
		else if (Date.class.isAssignableFrom(sourceClass))
			return (short) ((Date) source).getTime();
		else if (java.sql.Date.class.isAssignableFrom(sourceClass))
			return (short) ((java.sql.Date) source).getTime();
		else if (Time.class.isAssignableFrom(sourceClass))
			return (short) ((Time) source).getTime();
		else if (Timestamp.class.isAssignableFrom(sourceClass))
			return (short) ((Timestamp) source).getTime();
		else
			return super.changeType(sourceClass, source);
	}

	/**
	 * Gets the value of a Short when the input for conversion is null;
	 */
	@Override
	public Short whenNull() {
		return 0;
	}

}
