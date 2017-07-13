package com.eixox.adapters;

import java.sql.Time;
import java.util.Date;

/**
 * An adapter for Time objects;
 * 
 * @author Rodrigo Portela
 *
 */
public class TimeAdapter extends Adapter<Time> {

	/**
	 * Creates a new timestamp adapter;
	 */
	public TimeAdapter() {
		super(Time.class);
	}

	/**
	 * Parses the input source string as a Time object;
	 */
	@Override
	public Time parse(String source) {
		return source == null || source.isEmpty()
				? null
				: Time.valueOf(source);
	}

	/**
	 * Changes the type of the source object to this adapter's data type;
	 */
	@Override
	protected Time changeType(Class<?> sourceClass, Object source) {
		if (Date.class.isAssignableFrom(sourceClass))
			return new Time(((Date) source).getTime());
		else if (Number.class.isAssignableFrom(sourceClass))
			return new Time(((Number) source).longValue());
		else
			return super.changeType(sourceClass, source);
	}
}
