package com.eixox.data.text;

import java.lang.reflect.Field;

import com.eixox.data.DataAspectField;

public class CsvAspectField extends DataAspectField {

	public CsvAspectField(CsvAspect<?> aspect, Field field, String columnName) {
		super(aspect, field, columnName);
	}

}
