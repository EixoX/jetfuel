package com.eixox.data.sql;

import java.util.Map.Entry;

import com.eixox.data.Column;
import com.eixox.data.ColumnSchema;
import com.eixox.data.DataUpdate;

public class DatabaseUpdate extends DataUpdate {

	private final Database database;
	private final DatabaseAspect<?> aspect;

	public DatabaseUpdate(Database database, DatabaseAspect<?> aspect) {
		this.database = database;
		this.aspect = aspect;
	}

	/**
	 * Gets the storage aspect as schema;
	 */
	@Override
	public ColumnSchema<?> getSchema() {
		return aspect;
	}

	/**
	 * Executes the update command;
	 */
	@Override
	public synchronized long execute() {

		DatabaseCommand command = database.createCommand()
				.appendRaw("UPDATE ")
				.appendName(aspect.schemaName)
				.appendRaw(" SET ");

		boolean prependComma = false;
		for (Entry<Column, Object> entry : values.entrySet()) {
			if (prependComma)
				command.appendRaw(", ");
			else
				prependComma = true;

			command
					.appendName(entry.getKey().getColumnName())
					.appendRaw("=")
					.appendValue(entry.getValue());
		}

		return command
				.appendWhere(filter)
				.executeNonQuery();
	}
}
