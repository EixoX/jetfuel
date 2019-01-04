package com.eixox.data.text;

import java.lang.reflect.Field;

public class FixedLengthAspect<T> extends TextAspect<T, FixedLengthAspectField> {

	public final char[] line_buffer;

	public FixedLengthAspect(Class<T> dataType) {
		super(dataType, dataType.getSimpleName());
		int ll = 0;
		for (FixedLengthAspectField f : this)
			if (f.end > ll)
				ll = f.end;
		this.line_buffer = new char[ll];

	}

	@Override
	protected FixedLengthAspectField decorate(Field field) {
		FixedLength fl = field.getAnnotation(FixedLength.class);
		return fl == null
				? null
				: new FixedLengthAspectField(this, field, fl);
	}

	public T parse(String lineContent) {
		T item = newInstance();
		for (int i = 0; i < size(); i++) {
			FixedLengthAspectField field = get(i);
			String subs = lineContent.substring(field.start, field.end);
			subs = subs.trim();
			field.setValue(item, subs);
		}
		return item;
	}

	public synchronized String format(T entity) {
		for (int i = 0; i < line_buffer.length; i++)
			line_buffer[i] = ' ';

		for (int i = 0; i < size(); i++) {
			FixedLengthAspectField field = get(i);
			String val = field.getFormattedValue(entity);
			int l = Math.min(val.length(), field.end - field.start);
			for (int j = 0; i < l; i++)
				line_buffer[field.start + j] = val.charAt(j);
		}

		return new String(line_buffer);
	}

}
