package com.eixox.data;

/**
 * A base implementation of a "filterable" model.
 * 
 * @author Rodrigo Portela
 *
 * @param <T>
 */
public abstract class DataFilter<T extends DataFilter<T>> implements Filterable<T> {

	/**
	 * Gets the schema of the filterable;
	 * 
	 * @return
	 */
	public abstract ColumnSchema<?> getSchema();

	/**
	 * The filter expression object of this instance.
	 */
	public FilterExpression filter;

	/**
	 * Gets a reference to this object for chaining more commands;
	 * 
	 * @return
	 */
	protected abstract T getThis();

	/**
	 * Sets the filter object to the given term.
	 */
	public final T where(FilterTerm term) {
		this.filter = new FilterExpression(term);
		return getThis();
	}

	/**
	 * Sets the filter object to the given expression.
	 */
	public final T where(FilterExpression expression) {
		this.filter = new FilterExpression(expression);
		return getThis();
	}

	/**
	 * Sets the filter object to the given term.
	 */
	public final T where(String name, FilterComparison comparison, Object value) {
		return where(new FilterTerm(getSchema().get(name), comparison, value));
	}

	/**
	 * Sets the filter object to the given term.
	 */
	public final T where(String name, Object value) {
		return where(new FilterTerm(getSchema().get(name), value));
	}

	/**
	 * Sets the filter object to the given filter term;
	 */
	public T where(Column column, FilterComparison comparison, Object value) {
		return where(new FilterTerm(column, comparison, value));
	}

	/**
	 * Sets the filter object to the given filter term;
	 */
	public T where(Column column, Object value) {
		return where(new FilterTerm(column, value));
	}

	/**
	 * Appends a given term with an AND operator.
	 */
	public final T andWhere(FilterTerm term) {
		this.filter = this.filter == null
				? new FilterExpression(term)
				: this.filter.and(term);
		return getThis();
	}

	/**
	 * Appends a given expression with an AND operator.
	 */
	public final T andWhere(FilterExpression expression) {
		this.filter = this.filter == null
				? new FilterExpression(expression)
				: this.filter.and(expression);
		return getThis();
	}

	/**
	 * Appends a given term with an AND operator.
	 */
	public final T andWhere(String name, FilterComparison comparison, Object value) {
		return andWhere(new FilterTerm(getSchema().get(name), comparison, value));
	}

	/**
	 * Appends a given term with an AND operator.
	 */
	public final T andWhere(String name, Object value) {
		return andWhere(new FilterTerm(getSchema().get(name), value));
	}

	/**
	 * Appends the given filter term with an AND operator;
	 */
	public T andWhere(Column column, FilterComparison comparison, Object value) {
		return andWhere(new FilterTerm(column, comparison, value));
	}

	/**
	 * Appends the given filter term with an AND operator;
	 */
	public T andWhere(Column column, Object value) {
		return andWhere(new FilterTerm(column, value));
	}

	/**
	 * Appends a given term with an OR operator.
	 */
	public final T orWhere(FilterTerm term) {
		this.filter = this.filter == null
				? new FilterExpression(term)
				: this.filter.or(term);
		return getThis();
	}

	/**
	 * Appends a given term with an OR operator.
	 */
	public final T orWhere(FilterExpression expression) {
		this.filter = this.filter == null
				? new FilterExpression(expression)
				: this.filter.or(expression);
		return getThis();
	}

	/**
	 * Appends a given term with an OR operator.
	 */
	public final T orWhere(String name, FilterComparison comparison, Object value) {
		return orWhere(new FilterTerm(getSchema().get(name), comparison, value));
	}

	/**
	 * Appends a given term with an OR operator.
	 */
	public final T orWhere(String name, Object value) {
		return orWhere(new FilterTerm(getSchema().get(name), value));
	}

	/**
	 * Appends the given filter term with an OR operator;
	 */
	public T orWhere(Column column, FilterComparison comparison, Object value) {
		return orWhere(new FilterTerm(column, comparison, value));
	}

	/**
	 * Appends the given filter term with an OR operator;
	 */
	public final T orWhere(Column column, Object value) {
		return orWhere(new FilterTerm(column, value));
	}

}
