package com.eixox.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.eixox.adapters.Adapter;

/**
 * Represents the generic aspect of any class. PUBLIC fields, properties and
 * methods are accessible through this aspect; We used the Template Method
 * design pattern to indicate that implementers of this class should decorate
 * the members to a specific AspectMember stated on the class definition.
 * 
 * @author Rodrigo Portela
 *
 * @param <T>
 * @param <G>
 */
public abstract class Aspect<T, G extends AspectMember> implements Iterable<G> {

	/**
	 * The list of members of this class;
	 */
	protected final ArrayList<G> members;
	/**
	 * The data type associated with this aspect;
	 */
	public final Class<T> dataType;

	/**
	 * Tells the constructor to decorate the public accessible fields.
	 * 
	 * @return
	 */
	protected boolean decoratesFields() {
		return true;
	}

	/**
	 * Decorates a given field so it can be part of this aspect.
	 * 
	 * @param field
	 * @return
	 * @throws Exception
	 */
	protected abstract G decorate(Field field)
			throws Exception;

	/**
	 * Indicates that a given field is decoratable (default = is public AND is not
	 * static AND is not transient);
	 * 
	 * @param field
	 * @return
	 */
	protected boolean isDecoratable(Field field) {
		int modifiers = field.getModifiers();
		return Modifier.isPublic(modifiers) &&
				!Modifier.isStatic(modifiers) &&
				!Modifier.isTransient(modifiers) &&
				!Modifier.isFinal(modifiers);
	}

	/**
	 * Creates a new instance of the aspect.
	 * 
	 * @param dataType
	 */
	public Aspect(Class<T> dataType) {
		this.dataType = dataType;
		this.members = new ArrayList<G>();

		if (decoratesFields()) {
			Field[] fields = dataType.getFields();
			this.members.ensureCapacity(fields.length);
			for (int i = 0; i < fields.length; i++) {
				if (isDecoratable(fields[i]))
					try {
						G child = decorate(fields[i]);
						if (child != null)
							members.add(child);
					} catch (Exception ex) {
						throw new RuntimeException(
								"Trouble decorating " + fields[i].getName() + " in class " + dataType, ex);
					}
			}
		}

	}

	/**
	 * Gets the position of a member by its name.
	 * 
	 * @param name
	 * @return
	 */
	public synchronized final int indexOf(String name) {
		int s = members.size();
		for (int i = 0; i < s; i++)
			if (name.equalsIgnoreCase(members.get(i).name))
				return i;
		return -1;
	}

	/**
	 * Gets a member by its position.
	 * 
	 * @param index
	 * @return
	 */
	public synchronized final G get(int index) {
		return this.members.get(index);
	}

	/**
	 * Gets the name of a member by the index position;
	 * 
	 * @param index
	 * @return
	 */
	public synchronized final String getName(int index) {
		return this.members.get(index).name;
	}

	/**
	 * Gets the adapter of a member by their index position;
	 * 
	 * @param index
	 * @return
	 */
	public synchronized final Adapter<?> getAdapter(int index) {
		return this.members.get(index).adapter;
	}

	/**
	 * Gets a member by its name.
	 * 
	 * @param name
	 * @return
	 */
	public synchronized final G get(String name) {
		int index = indexOf(name);
		if (index < 0)
			throw new RuntimeException(name + " not found on " + dataType);
		else
			return members.get(index);
	}

	/**
	 * Gets the value of a member by its position.
	 * 
	 * @param entity
	 * @param index
	 * @return
	 */
	public synchronized final Object getValue(T entity, int index) {
		return members.get(index).getValue(entity);
	}

	/**
	 * Gets the value of a member by its name.
	 * 
	 * @param entity
	 * @param name
	 * @return
	 */
	public synchronized final Object getValue(T entity, String name) {
		return get(name).getValue(entity);
	}

	/**
	 * Sets the value of a member by its position.
	 * 
	 * @param entity
	 * @param index
	 * @param value
	 */
	public synchronized final void setValue(T entity, int index, Object value) {
		members.get(index).setValue(entity, value);
	}

	/**
	 * Sets the value of a member by its name.
	 * 
	 * @param entity
	 * @param name
	 * @param value
	 */
	public synchronized final void setValue(T entity, String name, Object value) {
		get(name).setValue(entity, value);
	}

	/**
	 * Gets the values of an entity as a hashmap of names and values.
	 * 
	 * @param entity
	 * @return
	 */
	public synchronized final Map<String, Object> getValues(T entity) {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		for (G member : members)
			map.put(member.name, member.getValue(entity));
		return map;
	}

	/**
	 * Sets the values of an entity based on a value map.
	 * 
	 * @param entity
	 * @param map
	 * @return
	 */
	public synchronized final int setValues(T entity, Map<String, Object> map) {
		int counter = 0;
		for (Entry<String, Object> entry : map.entrySet()) {
			int ordinal = indexOf(entry.getKey());
			if (ordinal >= 0) {
				members.get(ordinal).setValue(entity, entry.getValue());
				counter++;
			}
		}
		return counter;
	}

	/**
	 * Creates a new instance using the default constructor.
	 * 
	 * @return
	 */
	public final T newInstance() {
		try {
			return this.dataType.getConstructor().newInstance();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Gets the iterator for the collection
	 */
	public synchronized Iterator<G> iterator() {
		return members.listIterator();
	}

	/**
	 * Gets the number of members in this aspect.
	 * 
	 * @return
	 */
	public final int size() {
		return this.members.size();
	}

	/**
	 * Gets specific fields from a data type.
	 * 
	 * @param dataType
	 * @param ignoreMissingFields
	 * @param fieldNames
	 * @return
	 */
	public static synchronized Field[] getFields(Class<?> dataType, boolean ignoreMissingFields, String... fieldNames) {

		Field[] actualFields = dataType.getFields();
		Field[] foundFields = new Field[fieldNames.length];

		for (int i = 0; i < fieldNames.length; i++) {
			for (int j = 0; j < actualFields.length; j++) {
				if (fieldNames[i].equalsIgnoreCase(actualFields[j].getName())) {
					foundFields[i] = actualFields[j];
					break;
				}
			}
			if (ignoreMissingFields == false && foundFields[i] == null)
				throw new RuntimeException(fieldNames[i] + " is not a field on " + dataType);
		}

		return foundFields;
	}
}
