package com.eixox.adapters;

import java.util.Date;

/**
 * An adapter that can convert, parse and format Boolean objects;
 * 
 * @author Rodrigo Portela
 *
 */
public class BooleanAdapter extends Adapter<Boolean> {

	/**
	 * Creates a new Boolean adapter object;
	 */
	public BooleanAdapter() {
		super(Boolean.class);
	}

	/**
	 * Parses the input source string into a Boolean object;
	 */
	@Override
	public Boolean parse(String source) {
		return source == null || source.isEmpty()
				? whenNull()
				: new Boolean(source);
	}

	/**
	 * Changes the type of the source to a Boolean object;
	 */
	@Override
	protected Boolean changeType(Class<?> sourceClass, Object source) {
		if (Number.class.isAssignableFrom(sourceClass))
			return ((Number) source).intValue() != 0;
		else if (Date.class.isAssignableFrom(sourceClass))
			return ((Date) source).getTime() != 0L;
		else
			return super.changeType(sourceClass, source);
	}

	/**
	 * Gets the value of a Boolean when the input for conversion is null;
	 */
	@Override
	public Boolean whenNull() {
		return Boolean.FALSE;
	}

}
