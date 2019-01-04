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

	private int[] fieldOrdinals;
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
			this.first_row_has_names = annotation.firstRowHasNames();
			this.separator = annotation.separator();
			this.ignore_blank_lines = annotation.ignoreBlankLines();
			this.ignore_comment_lines = annotation.ignoreCommentLines();
			this.comment_qualifier = annotation.getCommentQualifier();
		}
	}

	private boolean isParseable(String lineContent) {
		return !(this.ignore_blank_lines &&
				lineContent.trim().isEmpty()) &&
				!(this.ignore_comment_lines &&
						lineContent.trim().startsWith(this.comment_qualifier));

	}

	public T parse(String lineContent) {

		if (!isParseable(lineContent))
			return null;

		String[] cells = lineContent.split(this.separator);

		// Is it supposed to filter by column names
		if (this.fieldOrdinals == null) {
			if (this.first_row_has_names) {
				fieldOrdinals = new int[cells.length];
				for (int i = 0; i < fieldOrdinals.length; i++)
					fieldOrdinals[i] = indexOfColumnName(cells[i]);
				return null;
			} else {
				this.fieldOrdinals = new int[0];
			}
		}

		// Read from the line as indexed by the first row
		if (fieldOrdinals.length > 0) {
			T e1 = newInstance();
			for (int i = 0; i < fieldOrdinals.length; i++)
				if (fieldOrdinals[i] >= 0 && i < cells.length)
					get(fieldOrdinals[i]).setValue(e1, cells[i]);
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
