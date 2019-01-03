package com.eixox.data;

import com.eixox.JetfuelException;

/**
 * Represents a filter node, part of a filter expression.
 * 
 * @author Rodrigo Portela
 *
 */
public class FilterNode implements Filter {

	/**
	 * The actual filter (term or expression)
	 */
	public final Filter filter;

	/**
	 * The filter operation to be used.
	 */
	public FilterOperation operation;

	/**
	 * The next node in the expression.
	 */
	public FilterNode next;

	/**
	 * Creates a new Filter node;
	 * 
	 * @param term
	 */
	public FilterNode(FilterTerm term) {
		this.filter = term;
	}

	/**
	 * Creates a new Filter Node.
	 * 
	 * @param expression
	 */
	public FilterNode(FilterExpression expression) {
		this.filter = expression;
	}

	/**
	 * Gets the filter type of this instance (NODE).
	 */
	public final FilterType getFilterType() {
		return FilterType.NODE;
	}

	/**
	 * Gets the schema of this filter node;
	 */
	public ColumnSchema<?> getSchema() {
		return this.filter.getSchema();
	}

	/**
	 * Tests an input object against this filter;
	 */
	public boolean testValue(Object input) {
		if (this.next == null)
			return this.filter.testValue(input);
		else
			switch (this.operation) {
			case AND:
				return this.filter.testValue(input) && this.next.testValue(input);
			case OR:
				return this.filter.testValue(input) || this.next.testValue(input);
			default:
				throw new JetfuelException("Unknown filter operation " + operation);
			}
	}

	/**
	 * Tests an input row against this filter;
	 */
	public boolean testRow(Object[] row) {
		if (this.next == null)
			return this.filter.testRow(row);
		else
			switch (this.operation) {
			case AND:
				return this.filter.testRow(row) && this.next.testRow(row);
			case OR:
				return this.filter.testRow(row) || this.next.testRow(row);
			default:
				throw new JetfuelException("Unknown filter operation " + operation);
			}
	}

	/**
	 * Tests an input entity against this filter;
	 */
	public boolean testEntity(Object entity) {
		if (this.next == null)
			return this.filter.testEntity(entity);
		else
			switch (this.operation) {
			case AND:
				return this.filter.testEntity(entity) && this.next.testEntity(entity);
			case OR:
				return this.filter.testEntity(entity) || this.next.testEntity(entity);
			default:
				throw new JetfuelException("Unknown filter operation " + operation);
			}
	}

}
