package com.eixox.data.text;

import java.lang.reflect.Field;

/**
 * A generic CSV aspect that can read and write fields to a text file;
 * 
 * @author Rodrigo Portela
 *
 * @param <T>
 */
public class CsvAspect<T> extends TextAspect<T, CsvAspectField> {

	private int[] field_ordinals;
	public boolean first_row_has_names = true;
	public String separator = "\t";
	public boolean ignore_blank_lines = true;
	public boolean ignore_comment_lines = true;
	public String comment_qualifier = "#";

	@Override
	protected CsvAspectField decorate(Field field) {
		Csv csv = field.getAnnotation(Csv.class);
		return csv == null
				? null
				: new CsvAspectField(this, field, csv.name());
	}

	public CsvAspect(Class<T> dataType) {
		super(dataType, dataType.getSimpleName());
		CsvFile annotation = dataType.getAnnotation(CsvFile.class);
		if (annotation != null) {
			this.first_row_has_names = annotation.first_row_has_names();
			this.separator = annotation.separator();
			this.ignore_blank_lines = annotation.ignore_blank_lines();
			this.ignore_comment_lines = annotation.ignore_comment_lines();
			this.comment_qualifier = annotation.comment_qualifier();
		}
	}

	public T parse(String line_content) {
		String[] cells = line_content.split(this.separator);

		// Ignore blank lines
		if (this.ignore_blank_lines && line_content.trim().isEmpty())
			return null;

		// Ignore comment lines
		if (this.ignore_comment_lines && line_content.trim().startsWith(this.comment_qualifier))
			return null;

		// Is it supposed to filter by column names
		if (this.field_ordinals == null) {
			if (this.first_row_has_names) {
				field_ordinals = new int[cells.length];
				for (int i = 0; i < field_ordinals.length; i++)
					field_ordinals[i] = indexOfColumnName(cells[i]);
				return null;
			} else
				this.field_ordinals = new int[0];
		}

		// Read from the line as indexed by the first row
		if (field_ordinals.length > 0) {
			T e1 = newInstance();
			for (int i = 0; i < field_ordinals.length; i++)
				if (field_ordinals[i] >= 0 && i < cells.length)
					get(field_ordinals[i]).setValue(e1, cells[i]);
			return e1;
		}
		// Raw read from the current line
		else {
			T e1 = newInstance();
			for (int i = 0; i < cells.length; i++)
				get(i).setValue(e1, cells[i]);
			return e1;
		}
	}

	public String format(T entity) {
		String[] cells = new String[size()];
		for (int i = 0; i < cells.length; i++)
			cells[i] = get(i).getFormattedValue(entity);
		return String.join(this.separator, cells);
	}

}
