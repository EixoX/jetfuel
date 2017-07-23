package com.eixox.data.entities;

import java.util.Iterator;

import com.eixox.data.Filter;

public class ListStorageIterator<T> implements Iterator<T> {

	public final ListStorage<T> storage;
	public final Filter filter;
	public final int offset;
	public final int limit;

	private int end_position;
	private int current_position;
	private T current_value;

	public ListStorageIterator(ListStorage<T> storage, Filter filter, int offset, int limit) {
		this.storage = storage;
		this.filter = filter;
		this.offset = offset;
		this.limit = limit;
		this.end_position = limit > 0
				? offset + limit
				: storage.list.size();
		this.current_position = offset;
	}

	public boolean hasNext() {

		if (current_position >= end_position || current_position >= storage.list.size())
			return false;

		current_value = storage.list.get(current_position);
		current_position++;

		return (filter != null && filter.testEntity(current_value) == false)
				? hasNext()
				: true;
	}

	public T next() {
		return this.current_value;
	}

}
