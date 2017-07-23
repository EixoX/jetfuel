package com.eixox.data.schema;

public class DatabaseSchemaColumn extends SchemaSimple {

	public DatabaseSchemaColumn(String name) {
		super(name, "COLUMN");
	}

	public int max_length;
	public int precision;
	public int scale;
	public boolean is_nullable;
	public boolean is_identity;
	public int column_ordinal;
	public boolean is_primary_key;
}
