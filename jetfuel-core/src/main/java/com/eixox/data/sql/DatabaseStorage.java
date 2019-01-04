package com.eixox.data.sql;

import java.util.Iterator;

import com.eixox.data.ColumnSchema;
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
		new DatabaseInsert<>(database, aspect).execute(item);
	}

	@Override
	public synchronized void insert(Iterator<T> iterator) {
		new DatabaseInsert<>(database, aspect).execute(iterator);
	}

	/**
	 * Creates a new select statement to be executed against this storage;
	 */
	@Override
	public DataSelect<T> select() {
		return new DatabaseSelect<>(database, aspect);
	}

	/**
	 * Creates a new DELETE command to remove itens from this storage;
	 */
	@Override
	public DataDelete delete() {
		return new DatabaseDelete(database, aspect);
	}

	/**
	 * Creates a new UPDATE command to change items on this storage;
	 */
	@Override
	public DataUpdate update() {
		return new DatabaseUpdate(database, aspect);
	}

}
