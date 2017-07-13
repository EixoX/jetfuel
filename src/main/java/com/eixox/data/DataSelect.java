package com.eixox.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.eixox.Visitor;

/**
 * A generic select that returns a table or an iteration of THINGS;
 * 
 * @author Rodrigo Portela
 *
 */
public abstract class DataSelect<T>
		extends
		DataFilter<DataSelect<T>>
		implements
		Pageable<DataSelect<T>>,
		Sortable<DataSelect<T>>,
		Iterable<T> {

	/**
	 * The sort expression.
	 */
	public SortExpression sort;

	/**
	 * The offset of records.
	 */
	public int offset;

	/**
	 * The record limit
	 */
	public int limit;

	/**
	 * Sets the sort expression to the given parameters.
	 */
	public final DataSelect<T> orderBy(SortDirection direction, String... names) {
		this.sort = new SortExpression(getSchema(), direction, names);
		return this;
	}

	/**
	 * Sets the sort expression to the given parameters.
	 */
	public final DataSelect<T> orderBy(String... names) {
		this.sort = new SortExpression(getSchema(), names);
		return this;
	}

	/**
	 * Sets the sort expression to the given parameters.
	 */
	public final DataSelect<T> orderBy(String name, SortDirection direction) {
		this.sort = new SortExpression(getSchema(), direction, name);
		return this;
	}

	/**
	 * Appends the given parameters to the sort expression.
	 */
	public final DataSelect<T> thenOrderBy(SortDirection direction, String... names) {
		this.sort = this.sort == null
				? new SortExpression(getSchema(), direction, names)
				: this.sort.thenBy(direction, names);
		return this;
	}

	/**
	 * Appends the given parameters to the sort expression.
	 */
	public final DataSelect<T> thenOrderBy(String... names) {
		this.sort = this.sort == null
				? new SortExpression(getSchema(), names)
				: this.sort.thenBy(names);
		return this;
	}

	/**
	 * Appends the given parameters to the sort expression.
	 */
	public final DataSelect<T> thenOrderBy(String name, SortDirection direction) {
		this.sort = this.sort == null
				? new SortExpression(getSchema(), direction, name)
				: this.sort.thenBy(direction, name);
		return this;
	}

	/**
	 * Sets the page size and ordinal of the command.
	 */
	public final DataSelect<T> page(int pageSize, int pageOrdinal) {
		this.offset = pageSize * pageOrdinal;
		this.limit = pageSize;
		return this;
	}

	/**
	 * Sets the record offset.
	 */
	public final DataSelect<T> setOffset(int offset) {
		this.offset = offset;
		return this;
	}

	/**
	 * Sets the record limit.
	 */
	public final DataSelect<T> setLimit(int limit) {
		this.limit = limit;
		return this;
	}

	/**
	 * Gets the first item with the specific filter;
	 * 
	 * @return
	 */
	public abstract T first();

	/**
	 * Executes the select command, retrieves the first row and creates a map of
	 * the result.
	 * 
	 * @return
	 */
	public abstract Map<String, Object> toMap();

	/**
	 * Counts the number of items that match the filters associated with this
	 * select expression;
	 * 
	 * @return
	 */
	public abstract long count();

	/**
	 * Checks if the given filter exists;
	 * 
	 * @return
	 */
	public abstract boolean exists();

	/**
	 * Gets the first value that matches the given filter;
	 * 
	 * @param column
	 * @return
	 */
	public abstract Object firstMember(Column column);

	/**
	 * Gets the first member that matches the given filter.
	 * 
	 * @param name
	 * @return
	 */
	public final Object firstMember(String name) {
		return firstMember(getSchema().get(name));
	}

	/**
	 * Gets only the desired column into a list;
	 * 
	 * @param column
	 * @return
	 */
	public abstract List<Object> getMembers(Column column);

	/**
	 * Gets only the given member into a list;
	 * 
	 * @param name
	 * @return
	 */
	public final List<Object> getMembers(String name) {
		return getMembers(getSchema().get(name));
	}

	/**
	 * Gets the reference to this object.
	 */
	@Override
	protected final DataSelect<T> getThis() {
		return this;
	}

	/**
	 * Adds the select result to a list;
	 * 
	 * @return
	 */
	public List<T> toList() {
		ArrayList<T> list = new ArrayList<T>(limit > 0
				? limit
				: 10);

		for (Iterator<T> i = iterator(); i.hasNext();)
			list.add(i.next());

		return list;
	}

	/**
	 * Accepts a visitor and applies to all data instances;
	 * 
	 * @param visitor
	 */
	public abstract void accept(Visitor<T> visitor);

}
