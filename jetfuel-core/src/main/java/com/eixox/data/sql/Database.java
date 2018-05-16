package com.eixox.data.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.Date;
import java.util.LinkedList;
import java.util.Properties;

import com.eixox.data.Column;
import com.eixox.data.schema.DatabaseSchema;

/**
 * Represents a generic database with a custom connection pool that reuses
 * connections for 19 minutes;
 * 
 * @author Rodrigo Portela
 *
 */
public abstract class Database {

	/**
	 * Sets a connection threshold;
	 */
	private int connection_threshold;

	/**
	 * Sets a savepoint for transactions.
	 */
	private Savepoint transaction_savepoint;

	/**
	 * Saves the transaction used on a savepoint;
	 */
	private Connection transaction_connection;

	/**
	 * The connection pool list;
	 */
	private final LinkedList<Connection> connection_pool = new LinkedList<Connection>();

	/**
	 * The dates the connection pool wast last used;
	 */
	private final LinkedList<Date> connection_last_used = new LinkedList<Date>();

	/**
	 * The database URL
	 */
	public final String url;

	/**
	 * The database properties;
	 */
	public final Properties properties;

	/**
	 * Creates a new sql database;
	 * 
	 * @param url
	 * @param properties
	 */
	public Database(String url, Properties properties, int connection_threshold) {
		this.url = url;
		this.properties = properties;
		this.connection_threshold = connection_threshold;
	}

	/**
	 * Creates a new sql database;
	 * 
	 * @param url
	 * @param properties
	 */
	public Database(String url, Properties properties) {
		this(url, properties, 1 * 60 * 1000); // 1 minute threshold;
	}

	/**
	 * Pops a connection from the pool and checks if it is open and the last used
	 * time is below 19 minutes. If those things are false, it attempts to close the
	 * connection. Otherwise it returns the connection or simply instantiates a new
	 * one;
	 * 
	 * @return
	 * @throws SQLException
	 */
	public synchronized Connection popConnection() throws SQLException {
		if (this.transaction_connection != null)
			return this.transaction_connection;

		else if (!connection_pool.isEmpty()) {
			Connection conn = connection_pool.removeFirst();
			Date conn_used = connection_last_used.removeFirst();
			if (!conn.isClosed()) {
				// 19 minutes threshold
				if (new Date().getTime() - conn_used.getTime() <= connection_threshold) {
					return conn;
				}

				// just close it and do nothing
				try {
					conn.close();
				} catch (Exception e) {
				}
			} // try to pop another one;
			return popConnection();
		}

		return createConnection();

	}

	/**
	 * Creates a new connection based on the url and properties in this database;
	 * 
	 * @return
	 * @throws SQLException
	 */
	protected Connection createConnection() throws SQLException {
		return properties == null
				? DriverManager.getConnection(url)
				: DriverManager.getConnection(url, properties);
	}

	/**
	 * Returns a connection to the pool and sets the last used date to the current
	 * Date instance;
	 * 
	 * @param conn
	 */
	public synchronized void pushConnection(Connection conn) {

		if (this.transaction_connection == null) {
			connection_pool.addLast(conn);
			connection_last_used.addLast(new Date());
		}

	}

	/**
	 * Pops every connection of the pool and closes it; Also pops the last used
	 * dates; If anything happens, the exception is written to STDOUT;
	 */
	public synchronized void recycleConnections() {
		for (Connection conn = connection_pool.removeFirst(); conn != null; conn = connection_pool.removeFirst()) {
			connection_last_used.removeFirst();
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Finalizes this database recycling any connections still on the pool;
	 */
	@Override
	protected void finalize() throws Throwable {
		recycleConnections();
	}

	/**
	 * Creates a new SQL command to run against this database instance;
	 * 
	 * @return
	 */
	public abstract DatabaseCommand createCommand();

	/**
	 * Gets the name prefix in this database command;
	 * 
	 * @return
	 */
	public abstract char getNamePrefix();

	/**
	 * Gets the name suffix in this database command;
	 * 
	 * @return
	 */
	public abstract char getNameSuffix();

	/**
	 * Indicates that the TOP clause is supported;
	 * 
	 * @return
	 */
	public abstract boolean supportsTop();

	/**
	 * Indicates that the OFFSET clause is supported;
	 * 
	 * @return
	 */
	public abstract boolean supportsOffset();

	/**
	 * Indicates that the LIMIT clause is supported;
	 * 
	 * @return
	 */
	public abstract boolean supportsLimit();

	/**
	 * Reads the identity column from a set of generated keys;
	 * 
	 * @param generated_keys
	 * @param identity
	 * @return
	 * @throws SQLException
	 */
	public Object readIdentity(ResultSet generated_keys, Column identity) throws SQLException {
		return generated_keys.next()
				? generated_keys.getObject(1)
				: null;
	}

	/**
	 * Reads the database schema from the underlying database;
	 * 
	 * @return
	 */
	public synchronized DatabaseSchema readSchema() {
		throw new RuntimeException("SQL Schema read not implemented in " + getClass());
	}

	/**
	 * Begins a transaction on the current database;
	 * 
	 * @return
	 * @throws SQLException
	 */
	public synchronized Savepoint beginTransaction() throws SQLException {
		if (this.transaction_connection != null)
			throw new RuntimeException("Another transaction is already running: " + transaction_savepoint);

		this.transaction_connection = popConnection();
		this.transaction_connection.setAutoCommit(false);
		this.transaction_savepoint = this.transaction_connection.setSavepoint();
		return this.transaction_savepoint;
	}

	/**
	 * Commits a running transaction or does nothing if none is present;
	 * 
	 * @throws SQLException
	 */
	public synchronized void commit() throws SQLException {
		if (this.transaction_connection != null) {
			this.transaction_connection.commit();
			this.transaction_savepoint = null;
			this.transaction_connection.setAutoCommit(true);
			this.pushConnection(this.transaction_connection);
			this.transaction_connection = null;
		}
	}

	/**
	 * Rolls back a running transaction;
	 * 
	 * @throws SQLException
	 */
	public synchronized void rollback() throws SQLException {
		if (this.transaction_connection != null) {
			this.transaction_connection.rollback(this.transaction_savepoint);
			this.transaction_savepoint = null;
			this.transaction_connection.setAutoCommit(true);
			this.pushConnection(this.transaction_connection);
			this.transaction_connection = null;
		}
	}

}
