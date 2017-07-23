package com.eixox.data.entities;

import com.eixox.data.ColumnSchema;
import com.eixox.data.DataDelete;

public class ListDelete extends DataDelete {

	public final ListStorage<?> storage;

	public ListDelete(ListStorage<?> storage) {
		this.storage = storage;
	}

	@Override
	public long execute() {
		long counter = 0;
		// truncate
		if (filter == null) {
			counter = storage.list.size();
			storage.list.clear();
		}
		// deletes by filter
		else {
			for (int i = 0; i < storage.list.size(); i++)
				if (filter.testEntity(storage.list.get(i))) {
					storage.list.remove(i);
					counter++;
				}
		}
		return counter;
	}

	@Override
	public ColumnSchema<?> getSchema() {
		return this.storage.aspect;
	}

}
