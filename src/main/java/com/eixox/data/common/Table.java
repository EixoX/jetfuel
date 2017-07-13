package com.eixox.data.common;

import java.util.ArrayList;

import com.eixox.data.ColumnSchema;
import com.eixox.data.DataDelete;
import com.eixox.data.DataSelect;
import com.eixox.data.DataStorage;
import com.eixox.data.DataUpdate;

/**
 * Represents a NAIVE and generic in memory table representation;
 * 
 * @author Rodrigo Portela
 *
 */
public class Table extends DataStorage<TableRow> {

	public final TableColumnSchema schema;
	public final ArrayList<TableRow> rows = new ArrayList<TableRow>();

	/**
	 * Instantiates a new table;
	 * 
	 * @param tableName
	 */
	public Table(String tableName) {
		this.schema = new TableColumnSchema(tableName);
	}

	/**
	 * Gets the schema associated with this table;
	 */
	@Override
	public ColumnSchema<?> getSchema() {
		return this.schema;
	}

	/**
	 * Inserts a new row on the table;
	 */
	@Override
	public void insert(TableRow item) {
		this.rows.add(item);
	}

	/**
	 * Selects the rows on the table;
	 */
	@Override
	public DataSelect<TableRow> select() {
		return new TableSelect(this);
	}

	/**
	 * Deletes the rows on the table;
	 */
	@Override
	public DataDelete delete() {
		return new TableDelete(this);
	}

	/**
	 * Updates the rows on the table;
	 */
	@Override
	public DataUpdate update() {
		return new TableUpdate(this);
	}

}
