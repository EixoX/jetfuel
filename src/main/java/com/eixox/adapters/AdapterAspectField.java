package com.eixox.adapters;

import java.lang.reflect.Field;

import com.eixox.reflection.AspectField;

public class AdapterAspectField extends AspectField {

	public final Adapter<?> adapter;

	public AdapterAspectField(Field field) {
		super(field);
		this.adapter = Adapter.getInstance(field);
	}

}
