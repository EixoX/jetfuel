package com.eixox.data.common;

import com.eixox.data.Column;
import com.eixox.data.ColumnSchema;

public class TableColumn implements Column {

	public final Table table;
	public final String columnName;
	public final int columnOrdinal;
	public final Class<?> dataType;
	private boolean isIdentity;
	private boolean isUnique;
	private boolean isCompositeKey;
	private boolean isReadOnly;

	protected TableColumn(Table table, String name, Class<?> dataType, int ordinal) {
		this.table = table;
		this.columnName = name;
		this.dataType = dataType;
		this.columnOrdinal = ordinal;
	}

	protected TableColumn(Table table, Column column, int ordinal) {
		this(table, column.getColumnName(), column.getDataType(), ordinal);
		this.isIdentity = column.isIdentity();
		this.isUnique = column.isUnique();
		this.isCompositeKey = column.isCompositeKey();
		this.isReadOnly = column.isReadOnly();
	}

	public final String getColumnName() {
		return this.columnName;
	}

	public final Class<?> getDataType() {
		return this.dataType;
	}

	public final boolean isIdentity() {
		return this.isIdentity;
	}

	public final boolean isUnique() {
		return this.isUnique;
	}

	public final boolean isCompositeKey() {
		return this.isCompositeKey;
	}

	public final boolean isReadOnly() {
		return this.isReadOnly;
	}

	@Override
	public String toString() {
		return this.columnName;
	}

	public ColumnSchema<?> getSchema() {
		return this.table.schema;
	}

	public int getColumnIndex() {
		return this.columnOrdinal;
	}

	public Object getValue(Object sourceRow) {
		return ((TableRow) sourceRow).getValue(this.columnOrdinal);
	}

	public void setValue(Object targetRow, Object value) {
		((TableRow) targetRow).cells[this.columnOrdinal] = value;

	}

}
