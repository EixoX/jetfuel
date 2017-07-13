package com.eixox.adapters;

/**
 * An adapter for Character objects;
 * 
 * @author Rodrigo Portela
 *
 */
public class CharacterAdapter extends Adapter<Character> {

	/**
	 * Creates a new Character adapter object;
	 */
	public CharacterAdapter() {
		super(Character.class);
	}

	/**
	 * Parses the source string into a Character object;
	 */
	@Override
	public Character parse(String source) {
		return source == null || source.isEmpty()
				? whenNull()
				: source.charAt(0);
	}

	/**
	 * Gets the zero character when null;
	 */
	@Override
	public Character whenNull() {
		return '\0';
	}

	/**
	 * Changes the type of the source object to a Character;
	 */
	@Override
	protected Character changeType(Class<?> sourceClass, Object source) {

		if (Number.class.isAssignableFrom(sourceClass))
			return (char) ((Number) source).intValue();
		else
			return super.changeType(sourceClass, source);
	}
}
