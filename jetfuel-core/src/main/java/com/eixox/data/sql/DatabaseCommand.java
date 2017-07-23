package com.eixox.data.sql;

import java.io.StringWriter;
import java.lang.reflect.Array;
import java.net.URL;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Node;

import com.eixox.data.Column;
import com.eixox.data.ColumnSchema;
import com.eixox.data.DataAspect;
import com.eixox.data.Filter;
import com.eixox.data.FilterExpression;
import com.eixox.data.FilterNode;
import com.eixox.data.FilterTerm;
import com.eixox.data.SortExpression;
import com.eixox.data.SortNode;

/**
 * A generic sql command executor;
 * 
 * @author Rodrigo Portela
 *
 */
public class DatabaseCommand {

	/**
	 * Stores the command text;
	 */
	public final StringBuilder text = new StringBuilder(1024);

	/**
	 * The command parameters;
	 */
	public final ArrayList<Object> parameters = new ArrayList<Object>();

	/**
	 * The database to use this sql command on;
	 */
	public final Database database;

	/**
	 * Creates a new instance of the sql command;
	 * 
	 * @param database
	 */
	public DatabaseCommand(Database database) {
		this.database = database;
	}

	/**
	 * Executes and returns if successful;
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public final boolean execute(Connection conn) throws SQLException {
		if (this.parameters.isEmpty()) {
			Statement stm = conn.createStatement();
			try {
				return stm.execute(text.toString());
			} finally {
				stm.close();
			}
		} else {
			PreparedStatement ps = conn.prepareStatement(this.text.toString());
			try {
				for (int i = 0; i < parameters.size(); i++)
					ps.setObject(i + 1, parameters.get(i));
				return ps.execute();
			} finally {
				ps.close();
			}
		}
	}

	/**
	 * Executes and returns if successful;
	 * 
	 * @return
	 */
	public final boolean execute() {
		try {
			Connection conn = database.popConnection();
			try {
				return execute(conn);
			} finally {
				database.pushConnection(conn);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Executes a non query (DELETE, UPDATE or INSERT without generated keys).
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public final int executeNonQuery(Connection conn) throws SQLException {
		if (this.parameters.isEmpty()) {
			Statement stm = conn.createStatement();
			try {
				return stm.executeUpdate(text.toString());
			} finally {
				stm.close();
			}
		} else {
			PreparedStatement ps = conn.prepareStatement(this.text.toString());
			try {
				for (int i = 0; i < parameters.size(); i++)
					ps.setObject(i + 1, parameters.get(i));
				return ps.executeUpdate();
			} finally {
				ps.close();
			}
		}
	}

	/**
	 * Executes a non query (DELETE, UPDATE or INSERT without generated keys).
	 * 
	 * @return
	 */
	public final int executeNonQuery() {
		try {
			Connection conn = database.popConnection();
			try {
				return executeNonQuery(conn);
			} finally {
				database.pushConnection(conn);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Executes a query that returns a resultset that will be processed by the
	 * given command processor;
	 * 
	 * @param conn
	 * @param processor
	 * @return
	 * @throws SQLException
	 */
	public final <T> T executeQuery(Connection conn, ResultsetProcessor<T> processor) throws SQLException {
		boolean oldAutoCommit = conn.getAutoCommit();
		conn.setAutoCommit(false);
		try {
			if (parameters.isEmpty()) {
				Statement stm = conn.createStatement();
				stm.setFetchSize(2000);
				try {
					ResultSet rs = stm.executeQuery(text.toString());
					try {
						return processor.process(rs);
					} finally {
						rs.close();
					}
				} finally {
					stm.close();
				}
			} else {
				PreparedStatement ps = conn.prepareStatement(this.text.toString());
				try {
					for (int i = 0; i < parameters.size(); i++)
						ps.setObject(i + 1, parameters.get(i));
					ResultSet rs = ps.executeQuery(text.toString());
					try {
						return processor.process(rs);
					} finally {
						rs.close();
					}
				} finally {
					ps.close();
				}
			}
		} finally {
			// conn.setAutoCommit(true);
			conn.setAutoCommit(oldAutoCommit);
		}
	}

	/**
	 * Executes a query that returns a resultset that will be processed by the
	 * given command processor;
	 * 
	 * @param processor
	 * @return
	 */
	public final <T> T executeQuery(ResultsetProcessor<T> processor) {
		try {
			Connection conn = database.popConnection();
			try {
				return executeQuery(conn, processor);
			} finally {
				database.pushConnection(conn);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Executes the query and builds entities with the result;
	 * 
	 * @param conn
	 * @param aspect
	 * @return
	 * @throws SQLException
	 */
	public final <T> ArrayList<T> executeQuery(Connection conn, DataAspect<T, ?> aspect) throws SQLException {
		return executeQuery(conn, new ResultsetToArrayList<T>(aspect));
	}

	/**
	 * Results the query and builds entities with the result;
	 * 
	 * @param conn
	 * @param claz
	 * @return
	 * @throws SQLException
	 */
	public final <T> ArrayList<T> executeQuery(Connection conn, Class<T> claz) throws SQLException {
		return executeQuery(conn, DatabaseAspect.getInstance(claz));
	}

	/**
	 * Executes the query and builds entities with the result;
	 * 
	 * @param claz
	 * @return
	 */
	public final <T> ArrayList<T> executeQuery(Class<T> claz) {
		return executeQuery(DatabaseAspect.getInstance(claz));
	}

	/**
	 * Executes the query and builds entities with the result;
	 * 
	 * @param aspect
	 * @return
	 */
	public final <T> ArrayList<T> executeQuery(DataAspect<T, ?> aspect) {
		return executeQuery(new ResultsetToArrayList<T>(aspect));
	}

	/**
	 * Executes an insert command and retrieves the scope identity of the row
	 * inserted;
	 * 
	 * @param conn
	 * @param identity
	 * @return
	 * @throws SQLException
	 */
	public Object executeInsertAndScopeIdentity(Connection conn, final Column identity) throws SQLException {
		return executeInsert(conn, new ResultsetProcessor<Object>() {
			public Object process(ResultSet rs) throws SQLException {
				return database.readIdentity(rs, identity);
			}
		});
	}

	/**
	 * Executes an insert command and retrieves the scope identity of the row
	 * inserted;
	 * 
	 * @param identity
	 * @return
	 */
	public final Object executeInsertAndScopeIdentity(Column identity) {
		try {
			Connection conn = database.popConnection();
			try {
				return executeInsertAndScopeIdentity(conn, identity);
			} finally {
				database.pushConnection(conn);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Executes an insert command and let the given processor handle the
	 * generated keys;
	 * 
	 * @param conn
	 * @param generatedKeysProcessor
	 * @return
	 * @throws SQLException
	 */
	public final int executeInsert(Connection conn, ResultsetProcessor<?> generatedKeysProcessor) throws SQLException {
		if (parameters.isEmpty()) {
			Statement stm = conn.createStatement();
			try {
				int result = stm.executeUpdate(text.toString(), Statement.RETURN_GENERATED_KEYS);
				if (generatedKeysProcessor != null) {
					ResultSet generatedKeys = stm.getGeneratedKeys();
					try {
						generatedKeysProcessor.process(generatedKeys);
					} finally {
						generatedKeys.close();
					}
				}
				return result;
			} finally {
				stm.close();
			}
		} else {
			PreparedStatement ps = conn.prepareStatement(this.text.toString(), Statement.RETURN_GENERATED_KEYS);
			try {
				for (int i = 0; i < parameters.size(); i++)
					ps.setObject(i + 1, parameters.get(i));
				int result = ps.executeUpdate();
				ResultSet generatedKeys = ps.getGeneratedKeys();
				try {
					generatedKeysProcessor.process(generatedKeys);
				} finally {
					generatedKeys.close();
				}
				return result;
			} finally {
				ps.close();
			}
		}
	}

	/**
	 * Executes an insert command and let the given processor handle the
	 * generated keys;
	 * 
	 * @param generatedKeysProcessor
	 * @return
	 */
	public final int executeInsert(ResultsetProcessor<?> generatedKeysProcessor) {
		try {
			Connection conn = database.popConnection();
			try {
				return executeInsert(conn, generatedKeysProcessor);
			} finally {
				database.pushConnection(conn);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Executes the command and returns the first value of the first row in the
	 * resultset;
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public final Object executeScalar(Connection conn) throws SQLException {
		if (this.parameters.isEmpty()) {
			Statement stm = conn.createStatement();
			try {
				ResultSet rs = stm.executeQuery(text.toString());
				try {
					return rs.next()
							? rs.getObject(1)
							: null;
				} finally {
					rs.close();
				}
			} finally {
				stm.close();
			}
		} else {
			PreparedStatement ps = conn.prepareStatement(this.text.toString());
			try {
				for (int i = 0; i < this.parameters.size(); i++)
					ps.setObject(i + 1, parameters.get(i));
				ResultSet rs = ps.executeQuery();
				try {
					return rs.next()
							? rs.getObject(1)
							: null;
				} finally {
					rs.close();
				}
			} finally {
				ps.close();
			}
		}
	}

	/**
	 * Executes the command and returns the first value of the first row in the
	 * resultset;
	 * 
	 * @return
	 */
	public final Object executeScalar() {
		try {
			Connection conn = database.popConnection();
			try {
				return executeScalar(conn);
			} finally {
				database.pushConnection(conn);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Appends a raw text to the underlying command;
	 * 
	 * @param sql
	 * @return
	 */
	public final DatabaseCommand appendRaw(String sql) {
		this.text.append(sql);
		return this;
	}

	/**
	 * Appends a prefixed and suffixed name to the underlying command;
	 * 
	 * @param name
	 * @return
	 */
	public final DatabaseCommand appendName(String name) {
		this.text.append(database.getNamePrefix());
		this.text.append(name.replace('\'', '_'));
		this.text.append(database.getNameSuffix());
		return this;
	}

	/**
	 * Appends prefixed and suffixed names to the underlying command;
	 * 
	 * @param names
	 * @return
	 */
	public final DatabaseCommand appendNames(String... names) {
		appendName(names[0]);
		for (int i = 1; i < names.length; i++) {
			this.text.append(", ");
			appendName(names[i]);
		}
		return this;
	}

	/**
	 * Appends prefixed and suffixed names to the underlying command;
	 * 
	 * @param names
	 * @return
	 */
	public final DatabaseCommand appendNames(Iterable<String> names) {
		boolean prependComma = false;
		for (String s : names) {
			if (prependComma)
				this.text.append(", ");
			else
				prependComma = true;
			appendName(s);
		}
		return this;
	}

	/**
	 * Appends prefixed and suffixed names to the underlying commmand;
	 * 
	 * @param aspect
	 * @return
	 */
	public final DatabaseCommand appendNames(ColumnSchema<?> schema) {
		appendName(schema.get(0).getColumnName());
		int s = schema.size();
		for (int i = 1; i < s; i++) {
			text.append(", ");
			appendName(schema.get(i).getColumnName());
		}
		return this;
	}

	/**
	 * Appends a boolean value to the underlying command;
	 * 
	 * @param value
	 * @return
	 */
	public DatabaseCommand appendBoolean(Boolean value) {
		this.text.append(value
				? "TRUE"
				: "FALSE");
		return this;
	}

	/**
	 * Appends a number to the underlying comand;
	 * 
	 * @param number
	 * @return
	 */
	public DatabaseCommand appendNumber(Number number) {
		this.text.append(number);
		return this;
	}

	/**
	 * Appends a Date to the underlying command;
	 * 
	 * @param date
	 * @return
	 */
	public DatabaseCommand appendDate(java.util.Date date) {
		this.text.append('\'');
		this.text.append(new Timestamp(date.getTime()));
		this.text.append('\'');
		return this;
	}

	/**
	 * Appends a date to the underlying command;
	 * 
	 * @param date
	 * @return
	 */
	public DatabaseCommand appendDate(Date date) {
		this.text.append('\'');
		this.text.append(date);
		this.text.append('\'');
		return this;
	}

	/**
	 * Appends a time to the underlying command;
	 * 
	 * @param time
	 * @return
	 */
	public DatabaseCommand appendTime(Time time) {
		this.text.append('\'');
		this.text.append(time);
		this.text.append('\'');
		return this;
	}

	/**
	 * Appends a timestamp to the underlying command;
	 * 
	 * @param timestamp
	 * @return
	 */
	public DatabaseCommand appendTimestamp(Timestamp timestamp) {
		this.text.append('\'');
		this.text.append(timestamp);
		this.text.append('\'');
		return this;
	}

	/**
	 * Appends an UUID to the underlying command;
	 * 
	 * @param value
	 * @return
	 */
	public DatabaseCommand appendUUID(UUID value) {
		this.text.append('\'');
		this.text.append(value);
		this.text.append('\'');
		return this;
	}

	/**
	 * Appends a XML content to the underlying command;
	 * 
	 * @param value
	 * @return
	 */
	public DatabaseCommand appendXml(Node value) {
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		DOMSource source = new DOMSource(value);
		try {
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.transform(source, result);
			appendString(writer.toString());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return this;
	}

	/**
	 * Appends a string value to the underlying command;
	 * 
	 * @param value
	 * @return
	 */
	public DatabaseCommand appendString(String value) {
		this.text.append('\'');
		int stringLength = value.length();
		for (int i = 0; i < stringLength; ++i) {
			char c = value.charAt(i);
			switch (c) {
			case '\\':
				this.text.append('\\');
				this.text.append('\\');
				break;
			case '\'':
				this.text.append('\'');
				this.text.append('\'');
				break;
			default:
				this.text.append(c);
			}
		}
		this.text.append('\'');
		return this;
	}

	/**
	 * Appends a byte array to the underlying command;
	 * 
	 * @param value
	 * @return
	 */
	public DatabaseCommand appendByteArray(byte[] value) {
		throw new RuntimeException("Byte array serialization to SQL not implemented for command on " + database.getClass());
	}

	/**
	 * Appends the given value to the underlying command or throws a
	 * RuntimeException if unable to serialize the value to SQL;
	 * 
	 * @param value
	 * @return
	 */
	public final DatabaseCommand appendValue(Object value) {
		if (value == null) {
			text.append("NULL");
			return this;
		}
		if (value instanceof Boolean)
			return appendBoolean((Boolean) value);
		if (value instanceof Number)
			return appendNumber((Number) value);
		if (value instanceof java.util.Date)
			return appendDate((java.util.Date) value);
		if (value instanceof Calendar)
			return appendTimestamp(new Timestamp(((Calendar) value).getTimeInMillis()));
		if (value instanceof Date)
			return appendDate((Date) value);
		if (value instanceof Time)
			return appendTime((Time) value);
		if (value instanceof Timestamp)
			return appendTimestamp((Timestamp) value);
		if (value instanceof String)
			return appendString((String) value);
		if (value instanceof URL)
			return appendString(value.toString());
		if (value instanceof UUID)
			return appendUUID((UUID) value);
		if (value instanceof byte[])
			return appendByteArray((byte[]) value);
		if (value instanceof ByteBuffer)
			return appendByteArray(((ByteBuffer) value).array());
		if (value instanceof Node)
			return appendXml((Node) value);
		if (value instanceof Enum<?>)
			return appendString(value.toString());
		if (value instanceof Character)
			return appendString(value.toString());

		throw new RuntimeException("Can't serialize to SQL: " + value.getClass());
	}

	/**
	 * Appends an array of values as comma separated SQL;
	 * 
	 * @param array
	 * @return
	 */
	public final DatabaseCommand appendValuesArray(Object array) {
		int s = Array.getLength(array);
		appendValue(Array.get(array, 0));
		for (int i = 1; i < s; i++) {
			this.text.append(", ");
			appendValue(Array.get(array, i));
		}
		return this;
	}

	/**
	 * Appends the iterable values as comma separated SQL;
	 * 
	 * @param iterable
	 * @return
	 */
	public final DatabaseCommand appendValues(Iterable<?> iterable) {
		boolean prependComma = false;
		for (Object o : ((Iterable<?>) iterable)) {
			if (prependComma)
				this.text.append(", ");
			else
				prependComma = true;
			appendValue(o);
		}
		return this;
	}

	/**
	 * Appends the values as comma separated list of SQL or throws a
	 * RuntimeException if the object can't be converted to an Array or
	 * Iterable;
	 * 
	 * @param col
	 */
	public final DatabaseCommand appendValues(Object col) {
		if (col == null) {
			text.append("NULL");
			return this;
		}

		// append array
		if (col.getClass().isArray())
			return appendValuesArray(col);

		// append iterable;
		if (Iterable.class.isAssignableFrom(col.getClass()))
			return appendValues((Iterable<?>) col);

		throw new RuntimeException("Can't convert " + col.getClass() + " to Array or Iterable");
	}

	/**
	 * Appends an equal to statement to the underlying command;
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	protected DatabaseCommand appendEqualTo(String name, Object value) {
		appendName(name);
		if (value == null)
			this.text.append(" IS NULL");
		else {
			this.text.append(" = ");
			appendValue(value);
		}
		return this;
	}

	/**
	 * Appends a greater or equal statement to the underlying command;
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	protected DatabaseCommand appendGreaterOrEqual(String name, Object value) {
		appendName(name);
		this.text.append(" >= ");
		appendValue(value);
		return this;
	}

	/**
	 * Appends a greater than statement to the underlying command;
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	protected DatabaseCommand appendGreaterThan(String name, Object value) {
		appendName(name);
		this.text.append(" > ");
		appendValue(value);
		return this;
	}

	/**
	 * Appends an IN statement to the underlying command;
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	protected DatabaseCommand appendIn(String name, Object value) {
		appendName(name);
		this.text.append(" IN (");
		appendValues(value);
		this.text.append(")");
		return this;
	}

	/**
	 * Appends a LIKE statement to the underlying command;
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	protected DatabaseCommand appendLike(String name, Object value) {
		appendName(name);
		this.text.append(" LIKE ");
		appendValue(value);
		return this;
	}

	/**
	 * Appends a lower or equal statement to the underlying command;
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	protected DatabaseCommand appendLowerOrEqual(String name, Object value) {
		appendName(name);
		this.text.append(" <= ");
		appendValue(value);
		return this;
	}

	/**
	 * Appends a lower than statement to the underlying command;
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	protected DatabaseCommand appendLowerThan(String name, Object value) {
		appendName(name);
		this.text.append(" < ");
		appendValue(value);
		return this;
	}

	/**
	 * Appends a not equal to statement to the underlying command;
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	protected DatabaseCommand appendNotEqualTo(String name, Object value) {
		appendName(name);
		if (value == null)
			this.text.append(" IS NOT NULL");
		else {
			this.text.append(" != ");
			appendValue(value);
		}
		return this;
	}

	/**
	 * Appends a NOT IN statement to the underlying command;
	 * 
	 * @param value
	 * @return
	 */
	protected DatabaseCommand appendNotIn(String name, Object value) {
		appendName(name);
		this.text.append(" NOT IN (");
		appendValues(value);
		this.text.append(")");
		return this;
	}

	/**
	 * Appends a NOT LIKE statement to the underlying command;
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	protected DatabaseCommand appendNotLike(String name, Object value) {
		appendName(name);
		this.text.append(" NOT LIKE ");
		appendValue(value);
		return this;
	}

	/**
	 * Appends a filter to the underlying command text;
	 * 
	 * @param filter
	 * @return
	 */
	public final DatabaseCommand appendFilter(Filter filter) {
		switch (filter.getFilterType()) {
		case EXPRESSION:
			FilterExpression exp = (FilterExpression) filter;
			this.text.append('(');
			appendFilter(exp.first);
			this.text.append(')');
			break;
		case NODE:
			FilterNode node = (FilterNode) filter;
			appendFilter(node.filter);
			if (node.next != null) {
				switch (node.operation) {
				case AND:
					this.text.append(" AND ");
					appendFilter(node.next);
					break;
				case OR:
					this.text.append(" OR ");
					appendFilter(node.next);
					break;
				default:
					throw new RuntimeException("Unknwon filter operation " + node.operation);
				}
			}
			break;
		case TERM:
			FilterTerm term = (FilterTerm) filter;
			switch (term.comparison) {
			case EQUAL_TO:
				return appendEqualTo(term.column.getColumnName(), term.value);
			case GREATER_OR_EQUAL:
				return appendGreaterOrEqual(term.column.getColumnName(), term.value);
			case GREATER_THAN:
				return appendGreaterThan(term.column.getColumnName(), term.value);
			case IN:
				return appendIn(term.column.getColumnName(), term.value);
			case LIKE:
				return appendLike(term.column.getColumnName(), term.value);
			case LOWER_OR_EQUAL:
				return appendLowerOrEqual(term.column.getColumnName(), term.value);
			case LOWER_THAN:
				return appendLowerThan(term.column.getColumnName(), term.value);
			case NOT_EQUAL_TO:
				return appendNotEqualTo(term.column.getColumnName(), term.value);
			case NOT_IN:
				return appendNotIn(term.column.getColumnName(), term.value);
			case NOT_LIKE:
				return appendNotLike(term.column.getColumnName(), term.value);
			default:
				throw new RuntimeException("Unknown filter comparison " + term.comparison);
			}
		default:
			throw new RuntimeException("Unknwon filter type " + filter.getFilterType());

		}
		return this;
	}

	/**
	 * Appends a WHERE clause to the underlying command text;
	 * 
	 * @param filter
	 * @return
	 */
	public final DatabaseCommand appendWhere(Filter filter) {
		if (filter != null) {
			this.text.append(" WHERE ");
			appendFilter(filter);
		}
		return this;
	}

	/**
	 * Appends an ORDER BY clause to the underlying command text;
	 * 
	 * @param node
	 * @return
	 */
	public final DatabaseCommand appendOrderBy(SortExpression exp) {
		if (exp != null) {
			this.text.append(" ORDER BY ");
			boolean prependComma = false;
			for (SortNode node = exp.first; node != null; node = node.next) {
				if (prependComma)
					text.append(", ");
				else
					prependComma = true;

				appendName(node.column.getColumnName());
				switch (node.direction) {
				case ASC:
					break;
				case DESC:
					this.text.append(" DESC");
					break;
				default:
					throw new RuntimeException("Unknown sort direction " + node.direction);
				}

			}
		}
		return this;
	}

	/**
	 * Appends a TOP clause to the underlying command text (if supported on
	 * supportsTop()).
	 * 
	 * @param top
	 * @return
	 */
	public final DatabaseCommand appendTop(int top) {
		if (top > 0 && database.supportsTop()) {
			this.text.append(" TOP(");
			this.text.append(top);
			this.text.append(") ");
		}
		return this;
	}

	/**
	 * Appends an OFFSET clause to the underlying command text (if supported on
	 * supportsOffset()).
	 * 
	 * @param offset
	 * @return
	 */
	public final DatabaseCommand appendOffset(int offset) {
		if (offset > 0 && database.supportsOffset()) {
			this.text.append(" OFFSET ");
			this.text.append(offset);
			this.text.append(' ');
		}
		return this;
	}

	/**
	 * Appends a LIMIT clause to the underlying command text (if supported on
	 * supportsLimit());
	 * 
	 * @param limit
	 * @return
	 */
	public final DatabaseCommand appendLimit(int limit) {
		if (limit > 0 && database.supportsLimit()) {
			this.text.append(" LIMIT ");
			this.text.append(limit);
			this.text.append(' ');
		}
		return this;
	}

	/**
	 * Gets the text content of this command;
	 */
	@Override
	public String toString() {
		return this.text.toString();
	}

}
