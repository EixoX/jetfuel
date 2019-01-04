package com.eixox.data.schema;

public abstract class SchemaItem {

	public final String name;
	public final String type_name;

	public SchemaItem(String name, String typeName) {
		this.name = name;
		this.type_name = typeName;
	}

	@Override
	public String toString() {
		return this.name + " (" + this.type_name + ")";
	}

}
