package com.eixox.data;

import java.lang.reflect.Array;
import java.util.regex.Pattern;

/**
 * Represents a filter term.
 * 
 * @author Rodrigo Portela
 *
 */
public class FilterTerm implements Filter {

	/**
	 * The column that is being filtered;
	 */
	public final Column column;

	/**
	 * The comparison to use when filtering.
	 */
	public final FilterComparison comparison;

	/**
	 * The value to be filtered.
	 */
	public final Object value;

	/**
	 * Creates a new filter term with the given parameters.
	 * 
	 * @param column
	 * @param comparison
	 * @param value
	 */
	public FilterTerm(Column column, FilterComparison comparison, Object value) {
		this.column = column;
		this.comparison = comparison;
		this.value = value;
	}

	/**
	 * Creates a new filter term with the given parameters.
	 * 
	 * @param column
	 * @param value
	 */
	public FilterTerm(Column column, Object value) {
		this(column, FilterComparison.EQUAL_TO, value);
	}

	/**
	 * Gets the type of filter used by this instance (TERM).
	 */
	public final FilterType getFilterType() {
		return FilterType.TERM;
	}

	/**
	 * Validates that the value either an enumerable or array and that the input
	 * is contained inside it.
	 * 
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private boolean testIn(Object input) {

		if (this.value == null)
			return false;

		Class<? extends Object> inClass = this.value.getClass();

		// test an array
		if (inClass.isArray()) {
			int size = Array.getLength(value);
			for (int i = 0; i < size; i++) {
				Object v = Array.get(value, i);
				if (v != null && v.equals(input))
					return true;
			}
			return false;
		}
		// tests an interable;
		else if (Iterable.class.isAssignableFrom(inClass)) {
			for (Object o : (Iterable) value)
				if (o != null && o.equals(input))
					return true;
			return false;
		}
		// throws an exception cause we can't test anything else
		else {
			throw new RuntimeException("We can only use IN comparison in Arrays or Iterables: " + inClass);
		}

	}

	/**
	 * Checks if the given input passes the filter.
	 * 
	 * @param input
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean testValue(Object input) {
		switch (this.comparison) {
		case EQUAL_TO:
			return value == null
					? input == null
					: value.equals(input);
		case GREATER_OR_EQUAL:
			return input == null
					? false
					: ((Comparable) input).compareTo(value) >= 0;
		case GREATER_THAN:
			return input == null
					? false
					: ((Comparable) input).compareTo(value) > 0;
		case IN:
			return testIn(input);
		case LIKE:
			return value == null || input == null
					? false
					: Pattern.matches((String) value, (CharSequence) input);
		case LOWER_OR_EQUAL:
			return input == null
					? false
					: ((Comparable) input).compareTo(value) <= 0;
		case LOWER_THAN:
			return input == null
					? false
					: ((Comparable) input).compareTo(value) < 0;
		case NOT_EQUAL_TO:
			return value == null
					? input != null
					: !value.equals(input);
		case NOT_IN:
			return !testIn(input);
		case NOT_LIKE:
			return value == null || input == null
					? true
					: !Pattern.matches((String) value, (CharSequence) input);
		default:
			throw new RuntimeException("Unknown filter comparison " + comparison);
		}
	}

	/**
	 * Gets the schema associated with the filter;
	 */
	public final ColumnSchema<?> getSchema() {
		return column.getSchema();
	}

	/**
	 * Tests the input row for this filter;
	 */
	public final boolean testRow(Object[] row) {
		int ordinal = this.column.getColumnIndex();
		return testValue(row.length <= ordinal
				? null
				: row[ordinal]);
	}

	/**
	 * Tests the input entity for this filter;
	 */
	public final boolean testEntity(Object entity) {
		DataAspectField field = (DataAspectField) this.column;
		Object value = field.getValue(entity);
		return testValue(value);
	}

}
