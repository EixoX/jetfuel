package com.eixox.data;

/*
 * Represents a "sort able" programming model.
 */
public interface Sortable<T> {

	public T orderBy(SortDirection direction, String... names);

	public T orderBy(String... names);

	public T orderBy(String name, SortDirection direction);

	public T thenOrderBy(SortDirection direction, String... names);

	public T thenOrderBy(String... names);

	public T thenOrderBy(String name, SortDirection direction);
}
