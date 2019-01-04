package com.eixox.data.common;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.eixox.data.Filter;

public class TableRowIterator implements Iterator<TableRow> {

	public final Table source;
	public final Filter filter;
	public final int start_position;
	public final int end_position;
	private int currentPosition;
	private TableRow currentRow;

	public TableRowIterator(Table source, Filter filter, int offset, int limit) {
		this.source = source;
		this.filter = filter;
		this.start_position = offset;
		this.end_position = limit > 0
				? offset + limit
				: source.rows.size();
		this.currentPosition = offset;

	}

	public boolean hasNext() {
		this.currentRow = null;
		if (currentPosition >= end_position || currentPosition >= source.rows.size())
			return false;

		TableRow r = source.rows.get(currentPosition);
		currentPosition++;

		if (filter != null && !filter.testRow(r.cells))
			return hasNext();

		currentRow = r;
		return true;

	}

	public TableRow next() {
		if (currentRow == null)
			throw new NoSuchElementException();
		else
			return currentRow;
	}

}
