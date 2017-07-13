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

	public DataColumn(ColumnSchema<? extends DataColumn> schema, Adapter<?> adapter, String column_name, boolean is_identity, boolean is_unique,
			boolean is_composite_key, boolean is_readonly) {
		this.schema = schema;
		this.adapter = adapter;
		this.column_name = column_name;
		this.column_index = schema.size();
		this.is_identity = is_identity;
		this.is_unique = is_unique;
		this.is_composite_key = is_composite_key;
		this.is_readonly = is_readonly;
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

	public Object getValue(Object source_row) {
		Object[] rowarr = (Object[]) source_row;
		return rowarr.length <= column_index
				? null
				: rowarr[column_index];
	}

	public void setValue(Object target_row, Object value) {
		((Object[]) target_row)[column_index] = value;
	}

}
