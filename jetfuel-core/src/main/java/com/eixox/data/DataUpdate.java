package com.eixox.data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A generic row update;
 * 
 * @author Rodrigo Portela
 *
 */
public abstract class DataUpdate extends DataFilter<DataUpdate> {

	/**
	 * The values that should be updated when this command runs;
	 */
	public final Map<Column, Object> values = new LinkedHashMap<>();

	/**
	 * Gets a reference to this for chaining commands;
	 */
	@Override
	protected final DataUpdate getThis() {
		return this;
	}

	/**
	 * Appends a name value pair for updating;
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	public final DataUpdate set(String name, Object value) {
		this.values.put(getSchema().get(name), value);
		return this;
	}

	/**
	 * Appends a column value pair for updating;
	 * 
	 * @param column
	 * @param value
	 * @return
	 */
	public final DataUpdate set(Column column, Object value) {
		this.values.put(column, value);
		return this;
	}

	/**
	 * Executes the update command;
	 * 
	 * @return
	 */
	public abstract long execute();

}
