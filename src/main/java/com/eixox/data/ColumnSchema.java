package com.eixox.data;

import java.util.List;

/**
 * Represents a column schema;
 * 
 * @author Rodrigo Portela
 *
 */
public interface ColumnSchema<T extends Column> extends Iterable<T> {

	/**
	 * Gets the schema name;
	 * 
	 * @return
	 */
	public String getSchemaName();

	/**
	 * Gets the schema size;
	 * 
	 * @return
	 */
	public int size();

	/**
	 * Gets a column by the index position;
	 * 
	 * @param index
	 * @return
	 */
	public T get(int index);

	/**
	 * Gets a column by the name;
	 * 
	 * @param name
	 * @return
	 */
	public T get(String name);

	/**
	 * Gets the index (ordinal) position of a column by the name;
	 * 
	 * @param name
	 * @return
	 */
	public int indexOf(String name);

	/**
	 * Gets the identity column;
	 * 
	 * @return
	 */
	public T getIdentity();

	/**
	 * Gets the unique columns;
	 * 
	 * @return
	 */
	public List<T> getUniqueColumns();

	/**
	 * Gets the composite key columns;
	 * 
	 * @return
	 */
	public List<T> getCompositeKeys();

	/**
	 * Creates the composite key using the selected row as values;
	 * 
	 * @param row
	 * @return
	 */
	public FilterExpression getCompositeKeyFilter(Object row);
}
