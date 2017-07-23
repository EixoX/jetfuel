package com.eixox.data.schema;

import java.util.ArrayList;

public class SchemaComplex<T extends SchemaItem> extends SchemaItem {

	public SchemaComplex(String name, String type_name) {
		super(name, type_name);
	}

	public final ArrayList<T> members = new ArrayList<T>();

	public final int indexOf(String name) {
		for (int i = 0; i < members.size(); i++)
			if (name.equalsIgnoreCase(members.get(i).name))
				return i;
		return -1;
	}

	public final T get(String name) {
		int ordinal = indexOf(name);
		return ordinal >= 0 ? members.get(ordinal) : null;
	}
}
