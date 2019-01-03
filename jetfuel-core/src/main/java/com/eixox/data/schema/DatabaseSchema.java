package com.eixox.data.schema;

import java.util.ArrayList;
import java.util.List;

public class DatabaseSchema extends SchemaComplex<DatabaseSchemaTable> {

	public DatabaseSchema(String name) {
		super(name, "DATABASE");
	}

	public final List<DatabaseSchemaTable> views = new ArrayList<>();

}
