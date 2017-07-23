package com.eixox.adapters;

/**
 * An adapter for Character Array objects;
 * 
 * @author Rodrigo Portela
 *
 */
public class CharacterArrayAdapter extends Adapter<char[]> {

	/**
	 * Creates a new character adapter class;
	 * 
	 * @param dataType
	 */
	public CharacterArrayAdapter(Class<char[]> dataType) {
		super(char[].class);
	}

	/**
	 * Parses the input char array into a string;
	 */
	@Override
	public char[] parse(String source) {
		return source == null || source.isEmpty()
				? null
				: source.toCharArray();
	}

	/**
	 * Formats the input source array as a string;
	 */
	@Override
	public String format(char[] source) {
		return source == null
				? ""
				: new String(source);
	}

}
