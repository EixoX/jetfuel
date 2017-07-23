package com.eixox.data;

/**
 * Programming model for "filter able" objects.
 * 
 * @author Rodrigo Portela
 *
 * @param <T>
 */
public interface Filterable<T> {

	/**
	 * Sets the filter to a given term.
	 * 
	 * @param term
	 * @return
	 */
	public T where(FilterTerm term);

	/**
	 * Sets the filter to a given expression.
	 * 
	 * @param expression
	 * @return
	 */
	public T where(FilterExpression expression);

	/**
	 * Sets the filter to a given filter term;
	 * 
	 * @param column
	 * @param comparison
	 * @param value
	 * @return
	 */
	public T where(Column column, FilterComparison comparison, Object value);

	/**
	 * Sets the filter to a given filter term;
	 * 
	 * @param column
	 * @param value
	 * @return
	 */
	public T where(Column column, Object value);

	/**
	 * Sets the filter to a given filter term.
	 * 
	 * @param name
	 * @param comparison
	 * @param value
	 * @return
	 */
	public T where(String name, FilterComparison comparison, Object value);

	/**
	 * Sets the filter o a given filter term.
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	public T where(String name, Object value);

	/**
	 * Appends a given term with an AND operator.
	 * 
	 * @param term
	 * @return
	 */
	public T andWhere(FilterTerm term);

	/**
	 * Appends a given expression with an AND operator.
	 * 
	 * @param expression
	 * @return
	 */
	public T andWhere(FilterExpression expression);

	/**
	 * Appends a given filter term with an AND operator;
	 * 
	 * @param column
	 * @param comparison
	 * @param value
	 * @return
	 */
	public T andWhere(Column column, FilterComparison comparison, Object value);

	/**
	 * Appends a given filter term with an AND operator;
	 * 
	 * @param column
	 * @param value
	 * @return
	 */
	public T andWhere(Column column, Object value);

	/**
	 * Appends a given term with an AND operator.
	 * 
	 * @param name
	 * @param comparison
	 * @param value
	 * @return
	 */
	public T andWhere(String name, FilterComparison comparison, Object value);

	/**
	 * Appends a given term with an AND operator.
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	public T andWhere(String name, Object value);

	/**
	 * Appends a given term with an OR operator.
	 * 
	 * @param term
	 * @return
	 */
	public T orWhere(FilterTerm term);

	/**
	 * Appends a given expression with an OR operator.
	 * 
	 * @param expression
	 * @return
	 */
	public T orWhere(FilterExpression expression);

	/**
	 * Appends a given filter term with an OR operator;
	 * 
	 * @param column
	 * @param comparison
	 * @param value
	 * @return
	 */
	public T orWhere(Column column, FilterComparison comparison, Object value);

	/**
	 * Appends a given filter term with an OR operator;
	 * 
	 * @param column
	 * @param value
	 * @return
	 */
	public T orWhere(Column column, Object value);

	/**
	 * Appends a given term with an OR operator.
	 * 
	 * @param name
	 * @param comparison
	 * @param value
	 * @return
	 */
	public T orWhere(String name, FilterComparison comparison, Object value);

	/**
	 * Appends a given term with an OR operator.
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	public T orWhere(String name, Object value);

}
