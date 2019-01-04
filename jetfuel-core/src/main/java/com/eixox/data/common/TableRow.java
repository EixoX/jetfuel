package com.eixox.data.common;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class TableRow implements Iterable<Object> {

	public final Object[] cells;
	public final Table table;

	protected TableRow(Table table, Object[] cells) {
		this.table = table;
		this.cells = cells;
	}

	public final Iterator<Object> iterator() {
		return new Iterator<Object>() {
			public int ordinal = 0;

			public Object next() {
				if (ordinal >= cells.length)
					throw new NoSuchElementException();
				Object o = cells[ordinal];
				ordinal++;
				return o;
			}

			public boolean hasNext() {
				return ordinal < cells.length;
			}
		};
	}

	public final Object getValue(int ordinal) {
		return ordinal < 0 || ordinal >= cells.length
				? null
				: cells[ordinal];
	}

	public final Object getValue(String name) {
		return getValue(table.schema.indexOf(name));
	}

	public final int getColumnCount() {
		return this.table.schema.size();
	}

}
