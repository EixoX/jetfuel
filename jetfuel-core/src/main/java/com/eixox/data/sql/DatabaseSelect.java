package com.eixox.data.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.eixox.Convert;
import com.eixox.Visitor;
import com.eixox.data.Column;
import com.eixox.data.ColumnSchema;
import com.eixox.data.DataAspectField;
import com.eixox.data.DataSelect;

public class DatabaseSelect<T> extends DataSelect<T> {

	private static final String SELECT = "SELECT ";
	private static final String FROM = " FROM ";

	private final Database database;
	private final DatabaseAspect<T> aspect;

	public DatabaseSelect(Database database, DatabaseAspect<T> aspect) {
		this.database = database;
		this.aspect = aspect;
	}

	/**
	 * Executes the command and returns an array list containing the entities;
	 */
	@Override
	public List<T> toList() {

		ResultsetToArrayList<T> processor = new ResultsetToArrayList<>(
				aspect,
				database.supportsOffset()
						? 0
						: offset,
				limit);

		return database.createCommand()
				.appendRaw(SELECT)
				.appendTop(offset + limit)
				.appendNames(aspect)
				.appendRaw(FROM)
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

		ResultsetToEntity<T> processor = new ResultsetToEntity<>(aspect, database.supportsOffset()
				? 0
				: offset);

		return database.createCommand()
				.appendRaw(SELECT)
				.appendTop(1)
				.appendNames(aspect)
				.appendRaw(FROM)
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
				.appendRaw(SELECT)
				.appendTop(1)
				.appendNames(aspect)
				.appendRaw(FROM)
				.appendName(aspect.schemaName)
				.appendWhere(filter)
				.appendOrderBy(sort)
				.appendOffset(offset)
				.appendLimit(1)
				.executeQuery(new ResultsetProcessor<Map<String, Object>>() {

					public Map<String, Object> process(ResultSet rs) throws SQLException {

						if (!rs.next())
							return null;

						LinkedHashMap<String, Object> map = new LinkedHashMap<>();
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
				.appendRaw(SELECT)
				.appendTop(1)
				.appendRaw("1 as one")
				.appendRaw(FROM)
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
				.appendRaw(SELECT)
				.appendTop(offset + 1)
				.appendName(column.getColumnName())
				.appendRaw(FROM)
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
				.appendRaw(SELECT)
				.appendTop(offset + limit)
				.appendName(column.getColumnName())
				.appendRaw(FROM)
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

						ArrayList<Object> list = new ArrayList<>();
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
				.appendRaw(SELECT)
				.appendTop(offset + limit)
				.appendNames(aspect)
				.appendRaw(FROM)
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

}
