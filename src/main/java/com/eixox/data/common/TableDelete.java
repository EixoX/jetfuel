package com.eixox.data.common;

import com.eixox.data.ColumnSchema;
import com.eixox.data.DataDelete;

public class TableDelete extends DataDelete {

	public final Table table;

	public TableDelete(Table table) {
		this.table = table;
	}

	@Override
	public long execute() {
		int counter = 0;
		if (this.filter == null) {
			counter = table.rows.size();
			this.table.rows.clear();
		} else {
			for (int i = 0; i < this.table.rows.size(); i++) {
				TableRow row = this.table.rows.get(i);
				if (filter.testRow(row.cells)) {
					table.rows.remove(i);
					counter++;
				}
			}
		}
		return counter;
	}

	@Override
	public ColumnSchema<?> getSchema() {
		return this.table.schema;
	}

}
