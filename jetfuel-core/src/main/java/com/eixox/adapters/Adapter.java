package com.eixox.adapters;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.nio.ByteBuffer;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import org.w3c.dom.Document;

import com.eixox.JetfuelException;

/**
 * Adapts a source object to a specific data type;
 * 
 * @author Rodrigo Portela
 *
 * @param <T>
 */
public abstract class Adapter<T> {

	/**
	 * The expected data type of this adapter;
	 */
	public final Class<T> dataType;

	/**
	 * Creates a new data adapter object;
	 * 
	 * @param dataType
	 */
	public Adapter(Class<T> dataType) {
		this.dataType = dataType;
	}

	/**
	 * Gets the data type of the current Adapter;
	 * 
	 * @return
	 */
	public final Class<T> getDataType() {
		return this.dataType;
	}

	/**
	 * Parses the input string source;
	 * 
	 * @param source
	 * @return
	 */
	public T parse(String source) {
		throw new JetfuelException("Parse is not implemented for " + dataType);
	}

	/**
	 * Converts the input with input class to the data type of this adapter;
	 * 
	 * @param sourceClass
	 * @param source
	 * @return
	 */
	protected T changeType(Class<?> sourceClass, Object source) {
		throw new JetfuelException("Can't convert " + sourceClass + " to " + dataType);
	}

	/**
	 * Gets the value of a null object conversion;
	 * 
	 * @return
	 */
	public T whenNull() {
		return null;
	}

	/**
	 * Converts the input source into this specific data type.
	 * 
	 * @param source
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public final T convert(final Object source) {

		if (source == null)
			return whenNull();

		final Class<?> sourceClass = source.getClass();

		if (dataType.isAssignableFrom(sourceClass))
			return (T) source;

		else if (String.class.isAssignableFrom(sourceClass))
			return parse(source.toString());

		else
			return changeType(sourceClass, source);

	}

	/**
	 * Formats the input source as a string.
	 * 
	 * @param source
	 * @return
	 */
	public String format(T source) {
		return source == null
				? ""
				: source.toString();
	}

	/**
	 * Formats the input source as a string.
	 * 
	 * @param source
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String formatObject(Object source) {
		return format((T) source);
	}

	/**
	 * Holds a static instance of all object adapters;
	 */
	private static final LinkedHashMap<Class<?>, Class<?>> ADAPTERS;
	static {
		ADAPTERS = new LinkedHashMap<>();
		ADAPTERS.put(Boolean.TYPE, BooleanAdapter.class);
		ADAPTERS.put(Boolean.class, BooleanAdapter.class);
		ADAPTERS.put(Byte.TYPE, ByteAdapter.class);
		ADAPTERS.put(Byte.class, ByteAdapter.class);
		ADAPTERS.put(Character.TYPE, CharacterAdapter.class);
		ADAPTERS.put(Character.class, CharacterAdapter.class);
		ADAPTERS.put(Double.TYPE, DoubleAdapter.class);
		ADAPTERS.put(Double.class, DoubleAdapter.class);
		ADAPTERS.put(Float.TYPE, FloatAdapter.class);
		ADAPTERS.put(Float.class, FloatAdapter.class);
		ADAPTERS.put(Integer.TYPE, IntegerAdapter.class);
		ADAPTERS.put(Integer.class, IntegerAdapter.class);
		ADAPTERS.put(Long.TYPE, LongAdapter.class);
		ADAPTERS.put(Long.class, LongAdapter.class);
		ADAPTERS.put(Short.TYPE, ShortAdapter.class);
		ADAPTERS.put(Short.class, ShortAdapter.class);
		ADAPTERS.put(byte[].class, ByteArrayAdapter.class);
		ADAPTERS.put(ByteBuffer.class, ByteBufferAdapter.class);
		ADAPTERS.put(char[].class, CharacterArrayAdapter.class);
		ADAPTERS.put(java.util.Date.class, DateAdapter.class);
		ADAPTERS.put(Number.class, NumberAdapter.class);
		ADAPTERS.put(Date.class, SqlDateAdapter.class);
		ADAPTERS.put(String.class, StringAdapter.class);
		ADAPTERS.put(Time.class, TimeAdapter.class);
		ADAPTERS.put(Timestamp.class, TimestampAdapter.class);
		ADAPTERS.put(UUID.class, UUIDAdapter.class);
		ADAPTERS.put(Document.class, XmlDocumentAdapter.class);
	}

	/**
	 * Registers an adapter. This method will make sure that the adapter has a
	 * parameterless constructor by instantiating it and will locate the data type
	 * of the adapter, replacing any existing registered adapter for it.
	 * 
	 * @param adapter
	 */
	public static synchronized void register(Class<? extends Adapter<?>> adapter) {
		try {
			Adapter<?> instance = adapter.getConstructor().newInstance();
			ADAPTERS.put(instance.dataType, adapter);
		} catch (Exception e) {
			throw new JetfuelException(e);
		}
	}

	/**
	 * Gets the data adapter registered on the static instances map or, if an array,
	 * instantiates a new array adapter based on the component adapter; ListAdapters
	 * cannot be instantiated using this method due to type erasure on the
	 * compilation.
	 * 
	 * @param dataType
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static synchronized <T> Adapter<T> getInstance(Class<T> dataType) {
		Class<?> adapterClass = ADAPTERS.get(dataType);
		if (adapterClass != null)
			try {
				return (Adapter<T>) adapterClass.getConstructor().newInstance();
			} catch (Exception e) {
				throw new JetfuelException(e);
			}
		else if (dataType.isArray())
			return new ArrayAdapter(dataType);
		else if (List.class.isAssignableFrom(dataType))
			return ListAdapter.getInstance(dataType);
		else if (Enum.class.isAssignableFrom(dataType))
			return new EnumAdapter(dataType);
		else
			return null;
	}

	/**
	 * Uses an annotation to instantiate an adapter that can be either a list, array
	 * or simple adapter.
	 * 
	 * @param dataType
	 * @param annotation
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Adapter<?> getInstance(Class<?> dataType, UseAdapter annotation) {
		Class<? extends Adapter<?>> adapterClass = annotation.value();
		Adapter<?> annotatedAdapter = null;
		Constructor<? extends Adapter<?>> constructor = null;
		try {
			constructor = adapterClass.getConstructor(UseAdapter.class);
			annotatedAdapter = constructor.newInstance(annotation);
		} catch (NoSuchMethodException e) {
			try {
				annotatedAdapter = adapterClass.getConstructor().newInstance();
			} catch (Exception ex) {
				throw new JetfuelException(ex);
			}
		} catch (Exception e) {
			throw new JetfuelException(e);
		}

		// is it a direct match?
		if (dataType.isAssignableFrom(annotatedAdapter.dataType))
			return annotatedAdapter;

		// is it an array?
		else if (dataType.isArray())
			return new ArrayAdapter(dataType, annotatedAdapter);

		// is it a list?
		else if (List.class.isAssignableFrom(dataType))
			return new ListAdapter(dataType, annotatedAdapter);

		// This will probably raise issues because the type adapted by the
		// annotation is not the same as the expected data type. Developer issue
		// nevertheless.
		else
			return annotatedAdapter;
	}

	/**
	 * Gets the adapter that's best suited to read and write values to a specific
	 * field of a class.
	 * 
	 * @param field
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static final synchronized Adapter getInstance(Field field) {

		Class<?> fieldType = field.getType();

		UseAdapter annotation = field.getAnnotation(UseAdapter.class);
		if (annotation != null)
			return getInstance(fieldType, annotation);

		Class<?> adapterClass = ADAPTERS.get(fieldType);

		// Direct match
		if (adapterClass != null)
			try {
				return (Adapter<?>) adapterClass.getConstructor().newInstance();
			} catch (Exception e) {
				throw new JetfuelException(e);
			}

		// Is it an array?
		else if (fieldType.isArray())
			return new ArrayAdapter(fieldType);

		// Is it an enum?
		else if (fieldType.isEnum())
			return new EnumAdapter(fieldType);

		// is it a list?
		else if (List.class.isAssignableFrom(fieldType))
			try {
				return new ListAdapter(fieldType, (ParameterizedType) field.getGenericType());
			} catch (ClassNotFoundException e) {
				throw new JetfuelException(e);
			}
		else {
			return null;
		}
	}

	/**
	 * Gets the adapter that's best suited to read and write values to a specific
	 * method of a class.
	 * 
	 * @param field
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static final synchronized Adapter getInstance(Method method) {

		Class<?> methodType = method.getReturnType();

		UseAdapter annotation = method.getAnnotation(UseAdapter.class);
		if (annotation != null)
			return getInstance(methodType, annotation);

		Class<?> adapterClass = ADAPTERS.get(methodType);

		// Direct match
		if (adapterClass != null)
			try {
				return (Adapter<?>) adapterClass.getConstructor().newInstance();
			} catch (Exception e) {
				throw new JetfuelException(e);
			}

		// Is it an array?
		else if (methodType.isArray())
			return new ArrayAdapter(methodType);

		// is it a list?
		else if (List.class.isAssignableFrom(methodType))
			try {
				return new ListAdapter(methodType, (ParameterizedType) method.getGenericReturnType());
			} catch (ClassNotFoundException e) {
				throw new JetfuelException(e);
			}

		// Is it an enum?
		else if (Enum.class.isAssignableFrom(methodType))
			return new EnumAdapter(methodType);

		else
			return null;
	}

}
