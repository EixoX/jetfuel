package com.eixox.data.entities;

import java.util.Map.Entry;

import com.eixox.data.Column;
import com.eixox.data.ColumnSchema;
import com.eixox.data.DataUpdate;

public class ListUpdate<T> extends DataUpdate {

	public final ListStorage<T> storage;

	public ListUpdate(ListStorage<T> storage) {
		this.storage = storage;
	}

	@Override
	public long execute() {

		long counter = 0;
		ListStorageIterator<T> iterator = new ListStorageIterator<T>(storage, filter, 0, 0);
		while (iterator.hasNext()) {
			T record = iterator.next();
			for (Entry<Column, Object> entry : values.entrySet())
				entry.getKey().setValue(record, entry.getValue());
			counter++;
		}
		return counter;
	}

	@Override
	public ColumnSchema<?> getSchema() {
		return this.storage.aspect;
	}

}
