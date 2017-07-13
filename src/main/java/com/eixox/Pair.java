package com.eixox;

/**
 * A generic pair that holds a key and a label;
 * 
 * @author Rodrigo Portela
 *
 * @param <T>
 * @param <G>
 */
public class Pair<T, G> {
	/**
	 * Holds the key of the pair.
	 */
	public final T key;
	/**
	 * Holds the label of the pair.
	 */
	public final G label;

	/**
	 * Constructs a new pair with the given key and label;
	 * 
	 * @param key
	 * @param label
	 */
	public Pair(T key, G label) {
		this.key = key;
		this.label = label;
	}

	@Override
	public String toString() {
		return (key == null
				? "NULL"
				: key.toString()) + "=" +
				(label == null
						? "NULL"
						: label.toString());
	}
}
