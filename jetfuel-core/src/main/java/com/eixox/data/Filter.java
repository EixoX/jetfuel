package com.eixox.data;

/**
 * A "filter" concept
 * 
 * @author Rodrigo Portela
 *
 */
public interface Filter {

	/**
	 * Get the filter type (usually term, node or expression).
	 * 
	 * @return
	 */
	public FilterType getFilterType();

	/**
	 * Gets the column schema associated with the filter;
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public ColumnSchema getSchema();

	/**
	 * Checks if the input passes this filter;
	 * 
	 * @param input
	 * @return
	 */
	public boolean testValue(Object input);

	/**
	 * Checks if the input row passes this filter;
	 * 
	 * @param row
	 * @return
	 */
	public boolean testRow(Object[] row);

	/**
	 * Checks if the input entity passes this filter;
	 * 
	 * @param entity
	 * @param aspect
	 * @return
	 */
	public boolean testEntity(Object entity);

}
