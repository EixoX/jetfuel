package com.eixox.data;

import java.lang.reflect.Field;

import com.eixox.reflection.AspectField;

/**
 * A concrete implementation of a type field marked as a data column.
 * 
 * @author Rodrigo Portela
 *
 */
public class DataAspectField extends AspectField implements Column {

	/**
	 * The aspect that contains this field;
	 */
	public final DataAspect<?, ?> aspect;

	/**
	 * The designated column name for the field;
	 */
	public final String columnName;

	/**
	 * The designated column ordinal of the field;
	 */
	public final int columnOrdinal;

	/**
	 * Creates a new Data Aspect Field instance;
	 * 
	 * @param aspect
	 * @param field
	 * @param columnName
	 */
	public DataAspectField(DataAspect<?, ?> aspect, Field field, String columnName) {
		super(field);
		this.aspect = aspect;
		this.columnOrdinal = aspect.size();
		this.columnName = columnName == null || columnName.isEmpty()
				? field.getName()
				: columnName;
	}

	/**
	 * Creates a new Data Aspect Field Instance
	 * 
	 * @param aspect
	 * @param field
	 */
	public DataAspectField(DataAspect<?, ?> aspect, Field field) {
		this(aspect, field, null);
	}

	/**
	 * Indicates that this field is an identity field;
	 */
	public final boolean isIdentity() {
		return this.field.isAnnotationPresent(Identity.class);
	}

	/**
	 * Indicates that his field is a unique value in a collection;
	 */
	public final boolean isUnique() {
		return this.field.isAnnotationPresent(Unique.class);
	}

	/**
	 * Indicates that this field is member of a composite key;
	 */
	public final boolean isCompositeKey() {
		return this.field.isAnnotationPresent(CompositeKey.class);
	}

	/**
	 * Indicates that this field should be read only;
	 */
	public final boolean isReadOnly() {
		return this.field.isAnnotationPresent(ReadOnly.class);
	}

	/**
	 * Gets the stored column name of this field;
	 */
	public final String getColumnName() {
		return this.columnName;
	}

	/**
	 * Gets the data type of this field;
	 */
	public final Class<?> getDataType() {
		return super.adapter.dataType;
	}

	/**
	 * Gets the schema associated with this coulumn;
	 */
	public final ColumnSchema<?> getSchema() {
		return this.aspect;
	}

	/**
	 * Gets the ordinal position of this column in it's parent schema;
	 */
	public final int getColumnIndex() {
		return this.columnOrdinal;
	}

}
