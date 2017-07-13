package com.eixox.data.schema;

import java.util.ArrayList;

public class DatabaseSchema extends SchemaComplex<DatabaseSchemaTable> {

	public DatabaseSchema(String name) {
		super(name, "DATABASE");
	}

	public final ArrayList<DatabaseSchemaTable> views = new ArrayList<DatabaseSchemaTable>();

}
