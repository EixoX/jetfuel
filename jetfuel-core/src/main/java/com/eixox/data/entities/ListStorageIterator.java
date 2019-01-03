package com.eixox.data.entities;

import java.util.Iterator;

import com.eixox.data.Filter;

public class ListStorageIterator<T> implements Iterator<T> {

	public final ListStorage<T> storage;
	public final Filter filter;
	public final int offset;
	public final int limit;

	private int endPosition;
	private int currentPosition;
	private T currentValue;

	public ListStorageIterator(ListStorage<T> storage, Filter filter, int offset, int limit) {
		this.storage = storage;
		this.filter = filter;
		this.offset = offset;
		this.limit = limit;
		this.endPosition = limit > 0
				? offset + limit
				: storage.list.size();
		this.currentPosition = offset;
	}

	public boolean hasNext() {

		if (currentPosition >= endPosition || currentPosition >= storage.list.size())
			return false;

		currentValue = storage.list.get(currentPosition);
		currentPosition++;

		if (filter == null || filter.testEntity(currentValue))
			return true;
		else
			return hasNext();
	}

	public T next() {
		return this.currentValue;
	}

}
