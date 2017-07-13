package com.eixox.adapters;

import java.math.BigDecimal;
import java.util.Date;

/**
 * An adapter that can convert, parse and format BigDecimal objects;
 * 
 * @author Rodrigo Portela
 *
 */
public class BigDecimalAdapter extends Adapter<BigDecimal> {

	/**
	 * Creates a new BigDecimal adapter object;
	 */
	public BigDecimalAdapter() {
		super(BigDecimal.class);
	}

	/**
	 * Parses the input source string into a BigDecimal object;
	 */
	@Override
	public BigDecimal parse(String source) {
		return source == null || source.isEmpty()
				? whenNull()
				: new BigDecimal(source);
	}

	/**
	 * Changes the type of the source to a BigDecimal object;
	 */
	@Override
	protected BigDecimal changeType(Class<?> sourceClass, Object source) {

		if (Number.class.isAssignableFrom(sourceClass))
			return BigDecimal.valueOf(((Number) source).doubleValue());

		else if (Date.class.isAssignableFrom(sourceClass))
			return BigDecimal.valueOf(((Date) source).getTime());

		else
			return super.changeType(sourceClass, source);
	}

	/**
	 * Gets the value of a BigDecimal when the input for conversion is null;
	 */
	@Override
	public BigDecimal whenNull() {
		return BigDecimal.valueOf(0.0);
	}

}
