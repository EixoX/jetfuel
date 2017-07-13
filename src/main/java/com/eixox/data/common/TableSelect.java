package com.eixox.data.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.eixox.Visitor;
import com.eixox.data.Column;
import com.eixox.data.ColumnSchema;
import com.eixox.data.DataSelect;

/**
 * Represents a Table Select command;
 * 
 * @author Rodrigo Portela
 *
 */
public class TableSelect extends DataSelect<TableRow> {

	/**
	 * Gets the underlying table to be selected;
	 */
	public final Table table;

	/**
	 * Instantiates a new Select command;
	 * 
	 * @param table
	 */
	public TableSelect(Table table) {
		this.table = table;
	}

	/**
	 * Creates an iterator that can go over the records selected by this
	 * instance;
	 */
	public synchronized Iterator<TableRow> iterator() {
		Iterator<TableRow> iterator = new TableRowIterator(table, filter, offset, limit);
		if (this.sort != null) {
			LinkedList<TableRow> list = new LinkedList<TableRow>();
			while (iterator.hasNext())
				list.add(iterator.next());
			this.sort.sortRows(list);
			iterator = list.iterator();
		}
		return iterator;
	}

	/**
	 * Gets the first row selectable by this instance;
	 */
	@Override
	public synchronized TableRow first() {
		Iterator<TableRow> iterator = iterator();
		return iterator.hasNext()
				? iterator.next()
				: null;
	}

	/**
	 * Converts the first row selectable by this instance to a Map.
	 */
	@Override
	public Map<String, Object> toMap() {

		TableRow item = first();
		if (item == null)
			return null;

		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		for (Column col : getSchema()) {
			String name = col.getColumnName();
			Object value = col.getValue(item);
			map.put(name, value);
		}

		return map;
	}

	/**
	 * Counts the rows that passes this instance's filter;
	 */
	@Override
	public long count() {
		long counter = 0;
		TableRowIterator iterator = new TableRowIterator(table, filter, 0, 0);
		while (iterator.hasNext())
			counter++;
		return counter;
	}

	/**
	 * Checks if any rows passes this instance's filter;
	 */
	@Override
	public boolean exists() {
		return new TableRowIterator(table, filter, 0, 0).hasNext();
	}

	/**
	 * Gets the first member of the first row that passes this instance's
	 * filter;
	 */
	@Override
	public Object firstMember(Column column) {
		TableRowIterator iterator = new TableRowIterator(table, filter, 0, 0);
		return iterator.hasNext()
				? column.getValue(iterator.next())
				: null;
	}

	/**
	 * Gets the values of the members that passes this instance's filter;
	 */
	@Override
	public List<Object> getMembers(Column column) {
		ArrayList<Object> list = new ArrayList<Object>();
		for (TableRow row : this) {
			Object v = column.getValue(row);
			list.add(v);
		}
		return list;
	}

	/**
	 * Get the schema of the table;
	 */
	@Override
	public ColumnSchema<?> getSchema() {
		return table.schema;
	}

	@Override
	public void accept(Visitor<TableRow> visitor) {
		for (Iterator<TableRow> i = iterator(); i.hasNext(); visitor.visit(i.next()))
			;

	}

}
