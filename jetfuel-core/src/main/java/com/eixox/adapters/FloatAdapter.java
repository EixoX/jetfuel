package com.eixox.adapters;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

/**
 * An adapter that can convert, parse and format Float objects;
 * 
 * @author Rodrigo Portela
 *
 */
public class FloatAdapter extends Adapter<Float> {

	/**
	 * Creates a new Float adapter object;
	 */
	public FloatAdapter() {
		super(Float.class);
	}

	/**
	 * Parses the input source string into a Float object;
	 */
	@Override
	public Float parse(String source) {
		return source == null || source.isEmpty() ?
				whenNull() :
				Float.valueOf(source);
	}

	/**
	 * Changes the type of the source to a Float object;
	 */
	@Override
	protected Float changeType(Class<?> sourceClass, Object source) {
		if (Number.class.isAssignableFrom(sourceClass))
			return ((Number) source).floatValue();
		else if (Date.class.isAssignableFrom(sourceClass))
			return (float) ((Date) source).getTime();
		else if (java.sql.Date.class.isAssignableFrom(sourceClass))
			return (float) ((java.sql.Date) source).getTime();
		else if (Time.class.isAssignableFrom(sourceClass))
			return (float) ((Time) source).getTime();
		else if (Timestamp.class.isAssignableFrom(sourceClass))
			return (float) ((Timestamp) source).getTime();
		else
			return super.changeType(sourceClass, source);
	}

	/**
	 * Gets the value of a Float when the input for conversion is null;
	 */
	@Override
	public Float whenNull() {
		return 0f;
	}

}
