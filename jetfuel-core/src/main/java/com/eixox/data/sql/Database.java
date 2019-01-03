package com.eixox.data.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.Date;
import java.util.LinkedList;
import java.util.Properties;

import com.eixox.JetfuelException;
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
	private int connectionThreshold;

	/**
	 * Sets a savepoint for transactions.
	 */
	private Savepoint transactionSavepoint;

	/**
	 * Saves the transaction used on a savepoint;
	 */
	private Connection transactionConnection;

	/**
	 * The connection pool list;
	 */
	private final LinkedList<Connection> connectionPool = new LinkedList<>();

	/**
	 * The dates the connection pool wast last used;
	 */
	private final LinkedList<Date> connectionLastUsed = new LinkedList<>();

	/**
	 * The database URL
	 */
	public final String url;

	/**
	 * The database properties;
	 */
	public final Properties properties;

	/**
	 * Creates a new SQL database;
	 * 
	 * @param url
	 * @param properties
	 */
	public Database(String url, Properties properties, int connectionThreshold) {
		this.url = url;
		this.properties = properties;
		this.connectionThreshold = connectionThreshold;
	}

	/**
	 * Creates a new SQL database with 1 minute threshold;
	 * 
	 * @param url
	 * @param properties
	 */
	public Database(String url, Properties properties) {

		this(url, properties, 1 * 60 * 1000);
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
		if (this.transactionConnection != null)
			return this.transactionConnection;

		else if (!connectionPool.isEmpty()) {
			Connection conn = connectionPool.removeFirst();
			Date connUsed = connectionLastUsed.removeFirst();
			if (!conn.isClosed()) {
				// 19 minutes threshold
				if (new Date().getTime() - connUsed.getTime() <= connectionThreshold) {
					return conn;
				}

				// just close it and do nothing
				try {
					conn.close();
				} catch (Exception e) {
					// do nothing
				}
			}
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

		if (this.transactionConnection == null) {
			connectionPool.addLast(conn);
			connectionLastUsed.addLast(new Date());
		}

	}

	/**
	 * Pops every connection of the pool and closes it; Also pops the last used
	 * dates; If anything happens, the exception is written to STDOUT;
	 */
	public synchronized void recycleConnections() {
		for (Connection conn = connectionPool.removeFirst(); conn != null; conn = connectionPool.removeFirst()) {
			connectionLastUsed.removeFirst();
			try {
				conn.close();
			} catch (Exception e) {
				System.getLogger(getClass().getName()).log(
						System.Logger.Level.ERROR,
						e.getMessage(),
						e);
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
	 * @param generatedKeys
	 * @param identity
	 * @return
	 * @throws SQLException
	 */
	public Object readIdentity(ResultSet generatedKeys) throws SQLException {
		return generatedKeys.next()
				? generatedKeys.getObject(1)
				: null;
	}

	/**
	 * Reads the database schema from the underlying database;
	 * 
	 * @return
	 */
	public synchronized DatabaseSchema readSchema() {
		throw new JetfuelException("SQL Schema read not implemented in " + getClass());
	}

	/**
	 * Begins a transaction on the current database;
	 * 
	 * @return
	 * @throws SQLException
	 */
	public synchronized Savepoint beginTransaction() throws SQLException {
		if (this.transactionConnection != null)
			throw new JetfuelException("Another transaction is already running: " + transactionSavepoint);

		this.transactionConnection = popConnection();
		this.transactionConnection.setAutoCommit(false);
		this.transactionSavepoint = this.transactionConnection.setSavepoint();
		return this.transactionSavepoint;
	}

	/**
	 * Commits a running transaction or does nothing if none is present;
	 * 
	 * @throws SQLException
	 */
	public synchronized void commit() throws SQLException {
		if (this.transactionConnection != null) {
			this.transactionConnection.commit();
			this.transactionSavepoint = null;
			this.transactionConnection.setAutoCommit(true);
			this.pushConnection(this.transactionConnection);
			this.transactionConnection = null;
		}
	}

	/**
	 * Rolls back a running transaction;
	 * 
	 * @throws SQLException
	 */
	public synchronized void rollback() throws SQLException {
		if (this.transactionConnection != null) {
			this.transactionConnection.rollback(this.transactionSavepoint);
			this.transactionSavepoint = null;
			this.transactionConnection.setAutoCommit(true);
			this.pushConnection(this.transactionConnection);
			this.transactionConnection = null;
		}
	}

}
