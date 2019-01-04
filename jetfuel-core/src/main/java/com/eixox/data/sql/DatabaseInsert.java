package com.eixox.data.sql;

import java.util.Iterator;

import com.eixox.data.DataAspectField;

public class DatabaseInsert<T> {

	private final Database database;
	private final DatabaseAspect<T> aspect;

	public DatabaseInsert(Database database, DatabaseAspect<T> aspect) {
		this.database = database;
		this.aspect = aspect;
	}

	private DatabaseCommand beginCommand() {
		DatabaseCommand command = database
				.createCommand()
				.appendRaw("INSERT INTO ")
				.appendName(aspect.schemaName)
				.appendRaw(" (");

		boolean prependComma = false;

		for (DataAspectField field : aspect) {
			if (!field.isIdentity()) {
				if (prependComma)
					command.appendRaw(", ");
				else
					prependComma = true;

				command.appendName(field.columnName);
			}
		}

		return command.appendRaw(") VALUES ");
	}

	private void appendValues(DatabaseCommand command, T item) {
		command.appendRaw("(");
		boolean fieldComma = false;
		for (DataAspectField field : aspect) {
			if (!field.isIdentity()) {
				if (fieldComma)
					command.appendRaw(", ");
				else
					fieldComma = true;

				Object fieldValue = field.getValue(item);
				command.appendValue(fieldValue);
			}
		}
		command.appendRaw(")");
	}

	public void execute(T item) {
		DatabaseCommand command = beginCommand();
		appendValues(command, item);
		if (aspect.identity != null) {
			Object identityValue = command.executeInsertAndScopeIdentity();
			aspect.identity.setValue(item, identityValue);
		} else {
			command.executeNonQuery();
		}
	}

	public void execute(Iterator<T> iterator) {

		int itemcounter = 0;
		DatabaseCommand command = beginCommand();
		boolean prependComma = false;
		while (iterator.hasNext()) {
			if (prependComma) {
				command.appendRaw(", ");
			} else {
				prependComma = true;
			}
			appendValues(command, iterator.next());
			itemcounter++;
			if (itemcounter == 1000) {
				command.executeNonQuery();
				execute(iterator);
				return;
			}
		}

		if (itemcounter > 0)
			command.executeNonQuery();
	}

	public void execute(Iterable<T> iterable) {
		execute(iterable.iterator());
	}
}
