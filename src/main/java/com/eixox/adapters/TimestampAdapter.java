package com.eixox.adapters;

import java.sql.Timestamp;
import java.util.Date;

/**
 * An adapter for Timestamp objects;
 * 
 * @author Rodrigo Portela
 *
 */
public class TimestampAdapter extends Adapter<Timestamp> {

	/**
	 * Creates a new timestamp adapter;
	 */
	public TimestampAdapter() {
		super(Timestamp.class);
	}

	/**
	 * Parses the input source string as a Timestamp object;
	 */
	@Override
	public Timestamp parse(String source) {
		return source == null || source.isEmpty()
				? null
				: Timestamp.valueOf(source);
	}

	/**
	 * Changes the type of the source object to this adapter's data type;
	 */
	@Override
	protected Timestamp changeType(Class<?> sourceClass, Object source) {
		if (Date.class.isAssignableFrom(sourceClass))
			return new Timestamp(((Date) source).getTime());
		else if (Number.class.isAssignableFrom(sourceClass))
			return new Timestamp(((Number) source).longValue());
		else
			return super.changeType(sourceClass, source);
	}
}
