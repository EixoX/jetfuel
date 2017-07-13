package com.eixox.data.common;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;

import com.eixox.data.SortExpression;

public class TableRowSorter implements Iterator<TableRow> {

	public final Table table;
	public final Iterator<TableRow> source;
	public final SortExpression sort;
	private final Iterator<TableRow> iterator;

	public TableRowSorter(Table table, SortExpression sort, Iterator<TableRow> source) {
		this.table = table;
		this.sort = sort;
		this.source = source;
		LinkedList<TableRow> list = new LinkedList<TableRow>();
		while (source.hasNext())
			list.add(source.next());
		Collections.sort(list, new Comparator<TableRow>() {

			public int compare(TableRow o1, TableRow o2) {
				// TODO Auto-generated method stub
				return 0;
			}
		});
		this.iterator = list.iterator();
	}

	public boolean hasNext() {
		return iterator.hasNext();
	}

	public TableRow next() {
		return iterator.next();
	}
}
