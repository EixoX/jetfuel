package com.eixox.adapters;

/**
 * An adapter that can convert, parse and format Byte objects;
 * 
 * @author Rodrigo Portela
 *
 */
public class ByteAdapter extends Adapter<Byte> {

	/**
	 * Creates a new Byte adapter object;
	 */
	public ByteAdapter() {
		super(Byte.class);
	}

	/**
	 * Parses the input source string into a Byte object;
	 */
	@Override
	public Byte parse(String source) {
		return source == null || source.isEmpty()
				? whenNull()
				: new Byte(source);
	}

	/**
	 * Changes the type of the source to a Byte object;
	 */
	@Override
	protected Byte changeType(Class<?> sourceClass, Object source) {
		if (Number.class.isAssignableFrom(sourceClass))
			return ((Number) source).byteValue();
		else
			return super.changeType(sourceClass, source);
	}

	/**
	 * Gets the value of a Byte when the input for conversion is null;
	 */
	@Override
	public Byte whenNull() {
		return 0;
	}

}
