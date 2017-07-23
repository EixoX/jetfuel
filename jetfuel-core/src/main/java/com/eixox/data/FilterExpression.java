package com.eixox.data;

/**
 * The filter expression implementation.
 * 
 * @author Rodrigo Portela
 *
 */
public class FilterExpression implements Filter {

	/**
	 * The first filter node of the expression
	 */
	public final FilterNode first;

	/**
	 * The last filter node of the expression
	 */
	private FilterNode last;

	/**
	 * Creates a new Filter Expression;
	 * 
	 * @param term
	 */
	public FilterExpression(FilterTerm term) {
		this.first = new FilterNode(term);
		this.last = this.first;
	}

	/**
	 * Creates a new Filter Expression;
	 * 
	 * @param expression
	 */
	public FilterExpression(FilterExpression expression) {
		this.first = new FilterNode(expression);
		this.last = this.first;
	}

	/**
	 * Creates a new Filter Expression with the given term as first node.
	 * 
	 * @param column
	 * @param comparison
	 * @param value
	 */
	public FilterExpression(Column column, FilterComparison comparison, Object value) {
		this(new FilterTerm(column, comparison, value));
	}

	/**
	 * Creates a new Filter Expression with the given term as first node.
	 * 
	 * @param column
	 * @param value
	 */
	public FilterExpression(Column column, Object value) {
		this(new FilterTerm(column, value));
	}

	/**
	 * Gets the type of filter represented by this object (EXPRESSION).
	 */
	public final FilterType getFilterType() {
		return FilterType.EXPRESSION;
	}

	/**
	 * Appends the given filter term with an AND operator.
	 * 
	 * @param term
	 * @return
	 */
	public final FilterExpression and(FilterTerm term) {
		this.last.next = new FilterNode(term);
		this.last.operation = FilterOperation.AND;
		this.last = this.last.next;
		return this;
	}

	/**
	 * Appends the given filter expression with an AND operator.
	 * 
	 * @param expression
	 * @return
	 */
	public final FilterExpression and(FilterExpression expression) {
		this.last.next = new FilterNode(expression);
		this.last.operation = FilterOperation.AND;
		this.last = this.last.next;
		return this;
	}

	/**
	 * Appends the given filter term with an AND operator.
	 * 
	 * @param name
	 * @param comparison
	 * @param value
	 * @return
	 */
	public final FilterExpression and(String name, FilterComparison comparison, Object value) {
		return and(new FilterTerm(getSchema().get(name), comparison, value));
	}

	/**
	 * Appends the given filter term with an AND operator.
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	public final FilterExpression and(String name, Object value) {
		return and(new FilterTerm(getSchema().get(name), value));
	}

	/**
	 * Appends the given filter term with an AND operator.
	 * 
	 * @param name
	 * @param comparison
	 * @param value
	 * @return
	 */
	public final FilterExpression and(Column column, FilterComparison comparison, Object value) {
		return and(new FilterTerm(column, comparison, value));
	}

	/**
	 * Appends the given filter term with an AND operator.
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	public final FilterExpression and(Column column, Object value) {
		return and(new FilterTerm(column, value));
	}

	/**
	 * Appends the given filter term with an OR operator.
	 * 
	 * @param term
	 * @return
	 */
	public final FilterExpression or(FilterTerm term) {
		this.last.next = new FilterNode(term);
		this.last.operation = FilterOperation.OR;
		this.last = this.last.next;
		return this;
	}

	/**
	 * Appends the given filter expression with an OR operator.
	 * 
	 * @param expression
	 * @return
	 */
	public final FilterExpression or(FilterExpression expression) {
		this.last.next = new FilterNode(expression);
		this.last.operation = FilterOperation.OR;
		this.last = this.last.next;
		return this;
	}

	/**
	 * Appends the given filter term with an OR operator.
	 * 
	 * @param name
	 * @param comparison
	 * @param value
	 * @return
	 */
	public final FilterExpression or(String name, FilterComparison comparison, Object value) {
		return or(new FilterTerm(getSchema().get(name), comparison, value));
	}

	/**
	 * Appends the given filter term with an OR operator.
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	public final FilterExpression or(String name, Object value) {
		return or(new FilterTerm(getSchema().get(name), value));
	}

	/**
	 * Appends the given filter term with an OR operator.
	 * 
	 * @param name
	 * @param comparison
	 * @param value
	 * @return
	 */
	public final FilterExpression or(Column column, FilterComparison comparison, Object value) {
		return and(new FilterTerm(column, comparison, value));
	}

	/**
	 * Appends the given filter term with an OR operator.
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	public final FilterExpression or(Column column, Object value) {
		return and(new FilterTerm(column, value));
	}

	/**
	 * Gets the schema associated with this filter;
	 */
	public ColumnSchema<?> getSchema() {
		return this.first.getSchema();
	}

	/**
	 * Checks if the input object passes this filter;
	 */
	public boolean testValue(Object input) {
		return this.first.testValue(input);
	}

	/**
	 * Checks if the input row[] passes this filter;
	 */
	public boolean testRow(Object[] row) {
		return this.first.testRow(row);
	}

	/**
	 * Checks if the input entity passes this filter;
	 */
	public boolean testEntity(Object entity) {
		return this.first.testEntity(entity);
	}

}
