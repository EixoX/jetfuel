package com.eixox.data.common;

import java.util.Iterator;

import com.eixox.data.Filter;

public class TableRowIterator implements Iterator<TableRow> {

	public final Table source;
	public final Filter filter;
	public final int start_position;
	public final int end_position;
	private int current_position;
	private TableRow current_row;

	public TableRowIterator(Table source, Filter filter, int offset, int limit) {
		this.source = source;
		this.filter = filter;
		this.start_position = offset;
		this.end_position = limit > 0
				? offset + limit
				: source.rows.size();
		this.current_position = offset;

	}

	public boolean hasNext() {
		this.current_row = null;
		if (current_position >= end_position || current_position >= source.rows.size())
			return false;

		TableRow r = source.rows.get(current_position);
		current_position++;

		if (filter != null && !filter.testRow(r.cells))
			return hasNext();

		current_row = r;
		return true;

	}

	public TableRow next() {
		return current_row;
	}

}
