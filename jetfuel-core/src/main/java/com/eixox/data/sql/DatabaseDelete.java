package com.eixox.data.sql;

import com.eixox.data.ColumnSchema;
import com.eixox.data.DataDelete;

public class DatabaseDelete extends DataDelete {

	private final Database database;
	private final DatabaseAspect<?> aspect;

	public DatabaseDelete(Database database, DatabaseAspect<?> aspect) {
		this.database = database;
		this.aspect = aspect;
	}

	/**
	 * Gets the schema of the delete command;
	 */
	@Override
	public ColumnSchema<?> getSchema() {
		return aspect;
	}

	/**
	 * Executes the delete command and returns the number of records affected;
	 */
	@Override
	public long execute() {
		return database.createCommand()
				.appendRaw("DELETE FROM ")
				.appendName(aspect.schemaName)
				.appendWhere(this.filter)
				.executeNonQuery();
	}

}
