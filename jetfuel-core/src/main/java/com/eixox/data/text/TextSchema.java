package com.eixox.data.text;

import com.eixox.data.Column;
import com.eixox.data.ColumnSchema;

/**
 * A text schema that can read and write to text lines;
 * 
 * @author Rodrigo Portela
 *
 * @param <T>
 * @param <G>
 */
public interface TextSchema<T, G extends Column> extends ColumnSchema<G> {

	/**
	 * Parses the input content;
	 * 
	 * @param lineContent
	 * @return
	 */
	public T parse(String lineContent);

	/**
	 * Formats the entity as a string;
	 * 
	 * @param entity
	 * @return
	 */
	public String format(T entity);

}
