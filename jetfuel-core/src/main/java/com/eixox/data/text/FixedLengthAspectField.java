package com.eixox.data.text;

import java.lang.reflect.Field;

import com.eixox.data.DataAspectField;

public class FixedLengthAspectField extends DataAspectField {

	public final int start;
	public final int end;

	public FixedLengthAspectField(FixedLengthAspect<?> aspect, Field field, FixedLength annotation) {
		super(aspect, field, null);
		this.start = annotation.start();
		this.end = annotation.end();
	}

}
