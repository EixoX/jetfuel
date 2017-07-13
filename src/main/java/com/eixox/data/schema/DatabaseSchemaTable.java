package com.eixox.data.schema;

public class DatabaseSchemaTable extends SchemaComplex<DatabaseSchemaColumn> {

	public DatabaseSchemaTable(String name) {
		super(name, "TABLE");
	}
}
