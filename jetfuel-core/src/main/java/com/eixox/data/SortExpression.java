package com.eixox.data;

import java.util.List;

import com.eixox.data.common.TableRow;

public class SortExpression {

	public final SortNode first;
	public SortNode last;

	public SortExpression(ColumnSchema<?> schema, String... names) {
		this(schema, SortDirection.ASC, names);
	}

	public SortExpression(ColumnSchema<?> schema, SortDirection direction, String name) {
		this.first = new SortNode(schema.get(name), direction);
		this.last = this.first;
	}

	public SortExpression(ColumnSchema<?> schema, SortDirection direction, String... names) {
		this(schema, direction, names[0]);
		for (int i = 1; i < names.length; i++) {
			this.last.next = new SortNode(schema.get(names[i]), direction);
			this.last = this.last.next;
		}
	}

	public final SortExpression thenBy(SortDirection direction, String... names) {
		ColumnSchema<?> schema = getSchema();
		for (int i = 0; i < names.length; i++) {
			this.last.next = new SortNode(schema.get(names[i]), direction);
			this.last = this.last.next;
		}
		return this;
	}

	public final SortExpression thenBy(String name, SortDirection direction) {
		return thenBy(direction, name);
	}

	public final SortExpression thenBy(String... names) {
		return thenBy(SortDirection.ASC, names);
	}

	@SuppressWarnings("rawtypes")
	public final ColumnSchema getSchema() {
		return this.first.column.getSchema();
	}

	public final void sort(List<Object[]> rows) {
		for (SortNode node = this.first; node != null; node = node.next)
			node.sort(rows);
	}

	public final void sortRows(List<TableRow> rows) {
		for (SortNode node = this.first; node != null; node = node.next)
			node.sortRows(rows);
	}

	public final <T> void sortEntities(List<T> entities) {
		for (SortNode node = this.first; node != null; node = node.next)
			node.sortEntities(entities);
	}
}
