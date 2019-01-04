package com.eixox.data;

import com.eixox.adapters.Adapter;

public class DataColumn implements Column {

	public final ColumnSchema<? extends DataColumn> schema;
	public final Adapter<?> adapter;
	public final String column_name;
	public final int column_index;
	public final boolean is_identity;
	public final boolean is_unique;
	public final boolean is_composite_key;
	public final boolean is_readonly;

	public DataColumn(
			ColumnSchema<? extends DataColumn> schema,
			Adapter<?> adapter,
			String columnName,
			boolean isIdentity,
			boolean isUnique,
			boolean isCompositeKey,
			boolean isReadOnly) {
		this.schema = schema;
		this.adapter = adapter;
		this.column_name = columnName;
		this.column_index = schema.size();
		this.is_identity = isIdentity;
		this.is_unique = isUnique;
		this.is_composite_key = isCompositeKey;
		this.is_readonly = isReadOnly;
	}

	public String getColumnName() {
		return this.column_name;
	}

	public int getColumnIndex() {
		return this.column_index;
	}

	public Class<?> getDataType() {
		return this.adapter.dataType;
	}

	public boolean isIdentity() {
		return this.is_identity;
	}

	public boolean isUnique() {
		return this.is_unique;
	}

	public boolean isCompositeKey() {
		return this.is_composite_key;
	}

	public boolean isReadOnly() {
		return this.is_readonly;
	}

	public ColumnSchema<?> getSchema() {
		return this.schema;
	}

	public Object getValue(Object sourceRow) {
		Object[] rowarr = (Object[]) sourceRow;
		return rowarr.length <= column_index
				? null
				: rowarr[column_index];
	}

	public void setValue(Object targetRow, Object value) {
		((Object[]) targetRow)[column_index] = value;
	}

}
