package com.eixox.data.schema;

public abstract class SchemaItem {

	public final String name;
	public final String type_name;

	public SchemaItem(String name, String type_name) {
		this.name = name;
		this.type_name = type_name;
	}

	@Override
	public String toString() {
		return this.name + " (" + this.type_name + ")";
	}

}
