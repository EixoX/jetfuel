package com.eixox.data;

import java.util.Iterator;
import java.util.List;

/**
 * Represents a generic storage that can handle insert, select, delete and
 * update commands; Some useful methods for retrieving items are implemented;
 * 
 * @author Rodrigo Portela
 *
 */
public abstract class DataStorage<T> {

	/**
	 * Gets the schema that governs this data storage;
	 * 
	 * @return
	 */
	public abstract ColumnSchema<?> getSchema();

	/**
	 * Inserts one item;
	 * 
	 * @return
	 */
	public abstract void insert(T item);

	/**
	 * Inserts the items in the array;
	 * 
	 * @param items
	 */
	@SuppressWarnings("unchecked")
	public synchronized void insert(T... items) {
		for (int i = 0; i < items.length; i++)
			insert(items[i]);
	}

	/**
	 * Inserts the items;
	 * 
	 * @param items
	 */
	public synchronized final void insert(Iterable<T> items) {
		insert(items.iterator());
	}

	/**
	 * Inserts the items;
	 * 
	 * @param iterator
	 */
	public synchronized void insert(Iterator<T> iterator) {
		while (iterator.hasNext())
			insert(iterator.next());
	}

	/**
	 * Creates a select command for selecting items from the storage;
	 * 
	 * @return
	 */
	public abstract DataSelect<T> select();

	/**
	 * Gets an entity from the storage based on the identity value;
	 * 
	 * @param id
	 * @return
	 */
	public synchronized T selectByIdentity(Object id) {
		Column identity = getSchema().getIdentity();
		if (identity == null)
			throw new RuntimeException("This schema has no identity column: " + getClass() + " -> " + getSchema().getSchemaName());
		else
			return select().where(identity, id).first();
	}

	/**
	 * Gets an entity from the storage based on a member value;
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	public synchronized T selectByMember(String name, Object value) {
		return select().where(name, value).first();
	}

	/**
	 * Gets an entity based on the composite keys;
	 * 
	 * @param compositeKeys
	 * @return
	 */
	public synchronized T selectByCompositeKey(Object... compositeKeys) {
		ColumnSchema<?> schema = getSchema();
		List<? extends Column> keyColumns = schema.getCompositeKeys();
		DataSelect<T> select = select();
		for (int i = 0; i < keyColumns.size(); i++)
			select.andWhere(keyColumns.get(i), compositeKeys[i]);
		return select.first();
	}

	/**
	 * Creates a delete command for removing items from the storage;
	 * 
	 * @return
	 */
	public abstract DataDelete delete();

	/**
	 * Creates an update command for changing items on the storage;
	 * 
	 * @return
	 */
	public abstract DataUpdate update();

	/**
	 * Deletes one item;
	 * 
	 * @param items
	 * @return
	 */
	public synchronized long delete(T item) {

		ColumnSchema<?> schema = getSchema();

		// deletes by identity
		Column identity = schema.getIdentity();
		if (identity != null) {
			Object identity_value = identity.getValue(item);
			if (!isEmptyIdentity(identity_value))
				return delete()
						.where(identity, identity_value)
						.execute();

		}

		// deletes by unique key
		for (Column unique : schema.getUniqueColumns()) {
			Object unique_value = unique.getValue(item);
			if (unique_value != null)
				return delete()
						.where(unique, unique_value)
						.execute();

		}

		// deletes by composite key
		FilterExpression compositeKeyFilter = schema.getCompositeKeyFilter(item);
		if (compositeKeyFilter != null)
			return delete()
					.where(compositeKeyFilter)
					.execute();

		// can't delete the item;
		throw new RuntimeException("Unable to DELETE the item of type " + item.getClass() + " -> " + item);

	}

	/**
	 * Deletes the items in the array;
	 * 
	 * @param items
	 */
	@SuppressWarnings("unchecked")
	public synchronized long delete(T... items) {
		long counter = 0;
		for (int i = 0; i < items.length; i++)
			counter += delete(items[i]);
		return counter;
	}

	/**
	 * Deletes the items;
	 * 
	 * @param items
	 * @return
	 */
	public synchronized final long delete(Iterable<T> items) {
		return delete(items.iterator());
	}

	/**
	 * Deletes the items on the iterator;
	 * 
	 * @param items
	 */
	public synchronized long delete(Iterator<T> items) {
		long counter = 0;
		while (items.hasNext())
			counter += delete(items.next());
		return counter;
	}

	/**
	 * Updates an item based on a specific column to be used as filter;
	 * 
	 * @param item
	 * @param column
	 * @return
	 */
	private final long updateByColumn(T item, Column column, Object value) {
		DataUpdate update = update();
		ColumnSchema<?> schema = getSchema();

		for (Column member : schema)
			if (member.getColumnIndex() != column.getColumnIndex() &&
					member.isIdentity() == false &&
					member.isReadOnly() == false)
				update.set(member, member.getValue(item));

		return update.where(column, value).execute();
	}

	/**
	 * Updates an item
	 * 
	 * @param item
	 */
	public synchronized long update(T item) {
		ColumnSchema<?> schema = getSchema();

		// updates by an identity
		Column identity = schema.getIdentity();
		if (identity != null) {
			Object identity_value = identity.getValue(item);
			if (isEmptyIdentity(identity_value))
				throw new RuntimeException("An IDENTITY value is expected for the UPDATE command on " + item.getClass() + " -> " + item);
			else
				return updateByColumn(item, identity, identity_value);
		}

		// updates by unique keys
		for (Column unique : schema.getUniqueColumns()) {
			Object unique_value = unique.getValue(item);
			if (unique_value != null)
				return updateByColumn(item, unique, unique_value);
		}

		// updates by composite key
		FilterExpression compositeKeyFilter = schema.getCompositeKeyFilter(item);
		if (compositeKeyFilter != null) {
			DataUpdate update = update();
			for (Column member : schema)
				if (!member.isCompositeKey() && !member.isIdentity() && !member.isReadOnly())
					update.set(member, member.getValue(item));

			return update
					.where(compositeKeyFilter)
					.execute();
		}

		// nothing else to do, won't update
		return 0L;

	}

	/**
	 * Updates the items;
	 * 
	 * @param items
	 */
	@SuppressWarnings("unchecked")
	public synchronized long update(T... items) {
		long counter = 0;
		for (int i = 0; i < items.length; i++)
			counter += update(items[i]);
		return counter;
	}

	/**
	 * Updates the items;
	 * 
	 * @param items
	 */
	public final long update(Iterable<T> items) {
		return update(items.iterator());
	}

	/**
	 * Updates the items;
	 * 
	 * @param items
	 */
	public synchronized long update(Iterator<T> items) {
		long counter = 0;
		while (items.hasNext())
			counter += update(items.next());
		return counter;
	}

	/**
	 * Saves one item to the storage;
	 * 
	 * @param item
	 */
	public synchronized final void save(T item) {
		if (update(item) == 0L)
			insert(item);
	}

	/**
	 * Saves the items;
	 * 
	 * @param items
	 */
	@SuppressWarnings("unchecked")
	public synchronized void save(T... items) {
		for (int i = 0; i < items.length; i++)
			save(items[i]);
	}

	/**
	 * Saves the items;
	 * 
	 * @param items
	 */
	public synchronized final void save(Iterable<T> items) {
		save(items.iterator());
	}

	/**
	 * Saves the items;
	 * 
	 * @param items
	 */
	public synchronized void save(Iterator<T> items) {
		while (items.hasNext())
			save(items.next());
	}

	/**
	 * Checks the value for NULLs and ZEROs;
	 * 
	 * @param value
	 * @return
	 */
	private static boolean isEmptyIdentity(Object value) {
		return value == null ||
				(value instanceof Number && ((Number) value).longValue() == 0L);
	}
}
