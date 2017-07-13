package com.eixox.data.sql;

import java.lang.reflect.Field;
import java.util.HashMap;

import com.eixox.data.DataAspect;
import com.eixox.data.DataAspectField;

/**
 * Represents an SQL aspect that loads fields annotated with @DatabaseColumn
 * 
 * @author Rodrigo Portela
 *
 * @param <T>
 */
public final class DatabaseAspect<T> extends DataAspect<T, DataAspectField> {

	/**
	 * Locates the table name in a class declaration;
	 * 
	 * @param dataType
	 * @return
	 */
	private static final String getTableName(Class<?> dataType) {
		DatabaseTable dt = dataType.getAnnotation(DatabaseTable.class);
		if (dt == null)
			throw new RuntimeException("Please annotate " + dataType + " with @DatabaseTable");
		else
			return dt.value();
	}

	/**
	 * Creates a new sql aspect;
	 * 
	 * @param dataType
	 */
	private DatabaseAspect(Class<T> dataType) {
		super(dataType, getTableName(dataType));
	}

	/**
	 * Finds the @DatabaseColumn annotation and instantiates SqlAspect Fields;
	 */
	@Override
	protected DataAspectField decorate(Field field) {
		DatabaseColumn col = field.getAnnotation(DatabaseColumn.class);
		return col == null
				? null
				: new DataAspectField(this, field, col.value());
	}

	/**
	 * Holds all instances of the Sql Aspects;
	 */
	private static final HashMap<Class<?>, DatabaseAspect<?>> INSTANCES = new HashMap<Class<?>, DatabaseAspect<?>>();

	/**
	 * Gets the Sql Aspect of a specific class;
	 * 
	 * @param claz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static synchronized final <T> DatabaseAspect<T> getInstance(Class<T> claz) {
		DatabaseAspect<T> aspect = (DatabaseAspect<T>) INSTANCES.get(claz);
		if (aspect == null) {
			aspect = new DatabaseAspect<T>(claz);
			INSTANCES.put(claz, aspect);
		}
		return aspect;
	}
}
