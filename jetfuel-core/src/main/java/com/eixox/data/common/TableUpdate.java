package com.eixox.data.common;

import java.util.Map.Entry;

import com.eixox.data.Column;
import com.eixox.data.ColumnSchema;
import com.eixox.data.DataUpdate;

public class TableUpdate extends DataUpdate {

	public Table table;

	public TableUpdate(Table table) {
		this.table = table;

	}

	@Override
	public long execute() {
		int counter = 0;
		TableRowIterator iterator = new TableRowIterator(table, filter, 0, 0);
		while (iterator.hasNext()) {
			TableRow row = iterator.next();
			for (Entry<Column, Object> entry : this.values.entrySet())
				entry.getKey().setValue(row, entry.getValue());
			counter++;
		}
		return counter;
	}

	@Override
	public ColumnSchema<?> getSchema() {
		return this.table.schema;
	}

}
