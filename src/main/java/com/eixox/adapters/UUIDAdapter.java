package com.eixox.adapters;

import java.util.UUID;

/**
 * An adapter for UUID objects;
 * 
 * @author Rodrigo Portela
 *
 */
public class UUIDAdapter extends Adapter<UUID> {

	/**
	 * Creates a new UUID adapter class;
	 */
	public UUIDAdapter() {
		super(UUID.class);
	}

	/**
	 * Parses the source string into an UUID object;
	 */
	@Override
	public UUID parse(String source) {
		return source == null || source.isEmpty()
				? null
				: UUID.fromString(source);
	}

	/**
	 * Changes the data type of the source object to an UUID;
	 */
	@Override
	protected UUID changeType(Class<?> sourceClass, Object source) {
		if (byte[].class.isAssignableFrom(sourceClass))
			return UUID.nameUUIDFromBytes((byte[]) source);
		else
			return super.changeType(sourceClass, source);
	}

}
