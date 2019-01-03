package com.eixox.adapters;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import com.eixox.JetfuelException;

/**
 * A class responsible for adapting lists;
 * 
 * @author Rodrigo Portela
 *
 * @param <T>
 */
public class ListAdapter<T> extends Adapter<List<T>> {

	/**
	 * Gets the list's component adapter;
	 */
	public final Adapter<T> componentAdapter;

	public final Class<T> componentType;

	/**
	 * Creates a new list of the provided data type and using a specific component
	 * adapter;
	 * 
	 * @param dataType
	 * @param componentAdapter
	 */
	public ListAdapter(Class<List<T>> dataType, Adapter<T> componentAdapter) {
		super(dataType);
		this.componentAdapter = componentAdapter;
		this.componentType = componentAdapter.dataType;
	}

	/**
	 * Creates a new list of the provided data type and tries to find a component
	 * adapter for the parameterized type;
	 * 
	 * @param dataType
	 * @param parameterizedType
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public ListAdapter(Class<List<T>> dataType, ParameterizedType parameterizedType) throws ClassNotFoundException {
		super(dataType);
		Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
		Type actualType = actualTypeArguments[0];
		this.componentType = (Class<T>) Class.forName(actualType.getTypeName());
		this.componentAdapter = Adapter.getInstance(this.componentType);
	}

	/**
	 * Creates a new list of this adapter's data type;
	 * 
	 * @return
	 */
	public List<T> newList() {
		try {
			return dataType.getConstructor().newInstance();
		} catch (Exception e) {
			throw new JetfuelException(e);
		}
	}

	/**
	 * Parses the input string using the provided separator;
	 * 
	 * @param source
	 * @param regex
	 * @return
	 */
	public List<T> parse(String source, String regex) {
		if (source == null)
			return Collections.emptyList();

		String[] spl = source.split(regex);
		List<T> list = newList();
		for (int i = 0; i < spl.length; i++) {
			T item = componentAdapter.convert(spl[i]);
			list.add(item);
		}
		return list;
	}

	/**
	 * Formats the input source list as string using the provided separators;
	 * 
	 * @param source
	 * @param separator
	 * @return
	 */
	public String format(List<T> source, String separator) {
		if (source == null || source.isEmpty())
			return "";

		int size = source.size();
		StringBuilder builder = new StringBuilder();
		T item = source.get(0);
		String itemString = componentAdapter.format(item);
		builder.append(itemString);
		for (int i = 1; i < size; i++) {
			item = source.get(i);
			itemString = componentAdapter.format(item);
			builder.append(separator);
			builder.append(itemString);
		}
		return builder.toString();

	}

	/**
	 * Parses the input source string as a comma separated value list;
	 */
	@Override
	public List<T> parse(String source) {
		return parse(source, ",");
	}

	/**
	 * Formats an input list as comma separated values;
	 */
	@Override
	public String format(List<T> source) {
		return format(source, ", ");
	}

	/**
	 * Converts the source array into a list of this adapter's data type;
	 * 
	 * @param source
	 * @return
	 */
	public List<T> convertArray(Object source) {
		int size = Array.getLength(source);
		List<T> list = newList();
		for (int i = 0; i < size; i++) {
			Object value = Array.get(source, i);
			T item = componentAdapter.convert(value);
			list.add(item);
		}
		return list;
	}

	/**
	 * Converts any iterable into a list of this adapter instance data type;
	 * 
	 * @param source
	 * @return
	 */
	public List<T> convertIterable(Iterable<?> source) {
		List<T> list = newList();
		for (Object o : ((Iterable<?>) source)) {
			T value = componentAdapter.convert(o);
			list.add(value);
		}
		return list;
	}

	/**
	 * Changes the type of the source object to a List of this adapter's data type;
	 */
	@Override
	protected List<T> changeType(Class<?> sourceClass, Object source) {
		if (sourceClass.isArray())
			return convertArray(source);
		else if (Iterable.class.isAssignableFrom(sourceClass))
			return convertIterable((Iterable<?>) source);
		else
			return super.changeType(sourceClass, source);
	}

}
