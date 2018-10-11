package com.eixox.data.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.eixox.Convert;
import com.eixox.Visitor;
import com.eixox.data.Column;
import com.eixox.data.ColumnSchema;
import com.eixox.data.DataAspectField;
import com.eixox.data.DataDelete;
import com.eixox.data.DataSelect;
import com.eixox.data.DataStorage;
import com.eixox.data.DataUpdate;

/**
 * Represts an SQL storage reader for reading and writing entities;
 * 
 * @author Rodrigo Portela
 *
 * @param <T>
 */
public class DatabaseStorage<T> extends DataStorage<T> {

	/**
	 * The SQL aspect associated with the Entity;
	 */
	public final DatabaseAspect<T> aspect;

	/**
	 * The database to be used for reading and writing;
	 */
	public final Database database;

	/**
	 * Creates a new sql storage instance;
	 * 
	 * @param aspect
	 * @param database
	 */
	public DatabaseStorage(DatabaseAspect<T> aspect, Database database) {
		this.aspect = aspect;
		this.database = database;
	}

	/**
	 * Creates a new sql storage instance;
	 * 
	 * @param claz
	 * @param database
	 */
	public DatabaseStorage(Class<T> claz, Database database) {
		this(DatabaseAspect.getInstance(claz), database);
	}

	/**
	 * Gets the schema associated with this storage;
	 */
	@Override
	public ColumnSchema<?> getSchema() {
		return this.aspect;
	}

	/**
	 * Inserts one item onto the database;
	 */
	@Override
	public synchronized void insert(T item) {

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

		command.appendRaw(") VALUES (");
		prependComma = false;
		for (DataAspectField field : aspect) {
			if (!field.isIdentity()) {
				if (prependComma)
					command.appendRaw(", ");
				else
					prependComma = true;

				command.appendValue(field.getValue(item));
			}
		}
		command.appendRaw(")");

		if (aspect.identity != null) {
			Object identity_value = command.executeInsertAndScopeIdentity(aspect.identity);
			aspect.identity.setValue(item, identity_value);
		} else
			command.executeNonQuery();

	}

	@Override
	public synchronized void insert(Iterator<T> iterator) {

		int itemcounter = 0;
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
		command.appendRaw(") VALUES ");
		prependComma = false;

		while (iterator.hasNext()) {
			if (prependComma) {
				command.appendRaw(", (");
			} else {
				command.appendRaw("(");
				prependComma = true;
			}
			T item = iterator.next();
			boolean fieldComma = false;
			for (DataAspectField field : aspect) {
				if (!field.isIdentity()) {
					if (fieldComma)
						command.appendRaw(", ");
					else
						fieldComma = true;

					command.appendValue(field.getValue(item));
				}
			}
			command.appendRaw(")");
			itemcounter++;

			if (itemcounter == 1000) {
				command.executeNonQuery();
				insert(iterator);
				return;
			}
		}

		if (itemcounter > 0)
			command.executeNonQuery();

	}

	/**
	 * Creates a new select statement to be executed against this storage;
	 */
	@Override
	public DataSelect<T> select() {
		return new DataSelect<T>() {

			/**
			 * Executes the command and returns an array list containing the entities;
			 */
			@Override
			public List<T> toList() {

				ResultsetToArrayList<T> processor = new ResultsetToArrayList<T>(
						aspect,
						database.supportsOffset()
								? 0
								: offset,
						limit);

				return database.createCommand()
						.appendRaw("SELECT ")
						.appendTop(offset + limit)
						.appendNames(aspect)
						.appendRaw(" FROM ")
						.appendName(aspect.schemaName)
						.appendWhere(filter)
						.appendOrderBy(sort)
						.appendOffset(offset)
						.appendLimit(limit)
						.executeQuery(processor);
			}

			/**
			 * Executes the command and returns an iterator to the list containing the
			 * entities;
			 */
			public Iterator<T> iterator() {
				return toList().iterator();
			}

			/**
			 * Executes the command and returns the first entity that matches the filter on
			 * this instance;
			 */
			@Override
			public T first() {

				ResultsetToEntity<T> processor = new ResultsetToEntity<T>(aspect, database.supportsOffset()
						? 0
						: offset);

				return database.createCommand()
						.appendRaw("SELECT ")
						.appendTop(1)
						.appendNames(aspect)
						.appendRaw(" FROM ")
						.appendName(aspect.schemaName)
						.appendWhere(filter)
						.appendOrderBy(sort)
						.appendOffset(offset)
						.appendLimit(1)
						.executeQuery(processor);
			}

			/**
			 * Executes the select command and retrives the values returned as a MAP;
			 */
			@Override
			public Map<String, Object> toMap() {
				return database.createCommand()
						.appendRaw("SELECT ")
						.appendTop(1)
						.appendNames(aspect)
						.appendRaw(" FROM ")
						.appendName(aspect.schemaName)
						.appendWhere(filter)
						.appendOrderBy(sort)
						.appendOffset(offset)
						.appendLimit(1)
						.executeQuery(new ResultsetProcessor<Map<String, Object>>() {

							public Map<String, Object> process(ResultSet rs) throws SQLException {

								if (!rs.next())
									return null;

								LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
								for (DataAspectField field : aspect) {
									String name = field.name;
									String colName = field.columnName;
									Object value = rs.getObject(colName);

									map.put(name, value);
								}

								return map;
							}
						});
			}

			/**
			 * Counts the number of items that respect this instance's filter;
			 */
			@Override
			public long count() {
				return Convert.toLong(database.createCommand()
						.appendRaw("SELECT COUNT(*) FROM ")
						.appendName(aspect.schemaName)
						.appendWhere(filter)
						.executeScalar());
			}

			/**
			 * Checks if any entity respect this instance's filter;
			 */
			@Override
			public boolean exists() {
				return Convert.toInt(database.createCommand()
						.appendRaw("SELECT ")
						.appendTop(1)
						.appendRaw("1 as one FROM ")
						.appendName(aspect.schemaName)
						.appendWhere(filter)
						.appendLimit(1)
						.executeScalar()) == 1;
			}

			/**
			 * Gets the first value of the column with the given name;
			 */
			@Override
			public Object firstMember(Column column) {
				return database.createCommand()
						.appendRaw("SELECT ")
						.appendTop(offset + 1)
						.appendName(column.getColumnName())
						.appendRaw(" FROM ")
						.appendName(aspect.schemaName)
						.appendWhere(filter)
						.appendOrderBy(sort)
						.appendOffset(offset)
						.appendLimit(1)
						.executeScalar();
			}

			/**
			 * Gets the values of a specific column as an array list of member values;
			 */
			@Override
			public List<Object> getMembers(Column column) {
				return database.createCommand()
						.appendRaw("SELECT ")
						.appendTop(offset + limit)
						.appendName(column.getColumnName())
						.appendRaw(" FROM ")
						.appendName(aspect.schemaName)
						.appendWhere(filter)
						.appendOrderBy(sort)
						.appendOffset(offset)
						.appendLimit(limit)
						.executeQuery(new ResultsetProcessor<List<Object>>() {
							public List<Object> process(ResultSet rs) throws SQLException {

								if (offset > 0 && !database.supportsOffset())
									for (int i = 0; i < offset && rs.next(); i++)
										;

								int l = limit > 0
										? limit
										: Integer.MAX_VALUE;

								ArrayList<Object> list = new ArrayList<Object>();
								for (int i = 0; i < l && rs.next(); i++)
									list.add(rs.getObject(1));
								return list;
							}
						});
			}

			/**
			 * Gets the schema of the select instance;
			 */
			@Override
			public ColumnSchema<?> getSchema() {
				return aspect;
			}

			@Override
			public void accept(Visitor<T> visitor) {
				database.createCommand()
						.appendRaw("SELECT ")
						.appendTop(offset + limit)
						.appendNames(aspect)
						.appendRaw(" FROM ")
						.appendName(aspect.schemaName)
						.appendWhere(filter)
						.appendOrderBy(sort)
						.appendOffset(offset)
						.appendLimit(limit)
						.executeQuery(new ResultsetEntityVisitor<T>(
								aspect,
								database.supportsOffset()
										? 0
										: offset,
								limit,
								visitor));

			}

		};
	}

	/**
	 * Creates a new DELETE command to remove itens from this storage;
	 */
	@Override
	public DataDelete delete() {
		return new DataDelete() {

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
		};
	}

	/**
	 * Creates a new UPDATE command to change items on this storage;
	 */
	@Override
	public DataUpdate update() {
		return new DataUpdate() {

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
		};
	}

}
