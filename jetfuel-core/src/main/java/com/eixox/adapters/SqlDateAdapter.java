package com.eixox.adapters;

import java.sql.Date;

/**
 * An adapter for Date objects;
 * 
 * @author Rodrigo Portela
 *
 */
public class SqlDateAdapter extends Adapter<Date> {

	/**
	 * Creates a new timestamp adapter;
	 */
	public SqlDateAdapter() {
		super(Date.class);
	}

	/**
	 * Parses the input source string as a Date object;
	 */
	@Override
	public Date parse(String source) {
		return source == null || source.isEmpty()
				? null
				: Date.valueOf(source);
	}

	/**
	 * Changes the type of the source object to this adapter's data type;
	 */
	@Override
	protected Date changeType(Class<?> sourceClass, Object source) {
		if (java.util.Date.class.isAssignableFrom(sourceClass))
			return new Date(((java.util.Date) source).getTime());
		else if (Number.class.isAssignableFrom(sourceClass))
			return new Date(((Number) source).longValue());
		else
			return super.changeType(sourceClass, source);
	}
}
