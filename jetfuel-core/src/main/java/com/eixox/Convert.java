package com.eixox;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Super helper class for converting object types.
 * 
 * @author Rodrigo Portela
 *
 */
public final class Convert {

	/**
	 * Na na na. You can't instantiate this.
	 */
	private Convert() {
	}

	private static JetfuelException createException(Object source, Class<?> target) {
		return new JetfuelException("Can't convert " +
				(source == null
						? "NULL"
						: source.getClass()) +
				" to " +
				target);
	}

	/**
	 * Converts nulls, booleans, strings and numbers to boolean.
	 * 
	 * @param in
	 * @return
	 */
	public static final synchronized boolean toBoolean(Object in) {
		if (in == null)
			return false;
		else if (in instanceof Boolean)
			return (Boolean) in;
		else if (in instanceof Number)
			return ((Number) in).intValue() != 0;
		else if (in instanceof String) {
			if (((String) in).isEmpty())
				return false;
			else
				return Boolean.parseBoolean((String) in);
		} else {
			throw createException(in, Boolean.TYPE);
		}

	}

	/**
	 * Converts nulls, numbers and strings to byte.
	 * 
	 * @param in
	 * @return
	 */
	public static final synchronized byte toByte(Object in) {
		if (in == null)
			return 0;
		else if (in instanceof Byte)
			return (Byte) in;
		else if (in instanceof Number)
			return ((Number) in).byteValue();
		else if (in instanceof String)
			return ((String) in).isEmpty()
					? 0
					: Byte.parseByte((String) in);
		else if (in instanceof Date)
			return (byte) ((Date) in).getTime();
		else {
			throw createException(in, Byte.TYPE);
		}

	}

	/**
	 * Converts null, numbers, dates and strings to char.
	 * 
	 * @param in
	 * @return
	 */
	public static final synchronized char toChar(Object in) {
		if (in == null)
			return 0;
		else if (in instanceof Character)
			return (Character) in;
		else if (in instanceof Number)
			return (char) ((Number) in).intValue();
		else if (in instanceof String)
			return ((String) in).isEmpty()
					? Character.MIN_VALUE
					: ((String) in).charAt(0);
		else if (in instanceof Date)
			return (char) ((Date) in).getTime();
		else {
			throw createException(in, Character.TYPE);
		}

	}

	/**
	 * Converts null, numbers, dates and strings to short.
	 * 
	 * @param in
	 * @return
	 */
	public static final synchronized short toShort(Object in) {
		if (in == null)
			return 0;
		else if (in instanceof Short)
			return (Short) in;
		else if (in instanceof Number)
			return ((Number) in).shortValue();
		else if (in instanceof String)
			return ((String) in).isEmpty()
					? 0
					: Short.parseShort((String) in);
		else if (in instanceof Date)
			return (short) ((Date) in).getTime();
		else {
			throw createException(in, Short.TYPE);
		}

	}

	/**
	 * Converts null, numbers, dates and strings to int.
	 * 
	 * @param in
	 * @return
	 */
	public static final synchronized int toInt(Object in) {
		if (in == null)
			return 0;
		else if (in instanceof Integer)
			return (Integer) in;
		else if (in instanceof Number)
			return ((Number) in).intValue();
		else if (in instanceof String)
			return ((String) in).isEmpty()
					? 0
					: Integer.parseInt((String) in);
		else if (in instanceof Date)
			return (int) ((Date) in).getTime();
		else {
			throw createException(in, Integer.TYPE);
		}
	}

	/**
	 * Converts null, numbers, dates and strings to long.
	 * 
	 * @param in
	 * @return
	 */
	public static final synchronized long toLong(Object in) {
		if (in == null)
			return 0L;
		else if (in instanceof Long)
			return (Long) in;
		else if (in instanceof Number)
			return ((Number) in).longValue();
		else if (in instanceof String)
			return ((String) in).isEmpty()
					? 0L
					: Long.parseLong((String) in);
		else if (in instanceof Date)
			return ((Date) in).getTime();
		else {
			throw createException(in, Long.TYPE);
		}
	}

	/**
	 * Converts nulls, numbers, dates and strings to float.
	 * 
	 * @param in
	 * @return
	 */
	public static final synchronized float toFloat(Object in) {
		if (in == null)
			return 0f;
		else if (in instanceof Float)
			return (Float) in;
		else if (in instanceof Number)
			return ((Number) in).floatValue();
		else if (in instanceof String)
			return ((String) in).isEmpty()
					? 0f
					: Float.parseFloat((String) in);
		else if (in instanceof Date)
			return ((Date) in).getTime();
		else {
			throw createException(in, Float.TYPE);
		}
	}

	/**
	 * Converts nulls, numbers, dates and strings to double.
	 * 
	 * @param in
	 * @return
	 */
	public static final synchronized double toDouble(Object in) {
		if (in == null)
			return 0.0;
		else if (in instanceof Double)
			return (Double) in;
		else if (in instanceof Number)
			return ((Number) in).doubleValue();
		else if (in instanceof String)
			return ((String) in).isEmpty()
					? 0.0
					: Double.parseDouble((String) in);
		else if (in instanceof Date)
			return ((Date) in).getTime();
		else {
			throw createException(in, Double.TYPE);
		}
	}

	/**
	 * Converts nulls, numbers, timestamps, date, time and strings to date.
	 * 
	 * @param in
	 * @return
	 */
	public static final synchronized Date toDate(Object in, DateFormat format) {
		if (in == null)
			return null;
		else if (in instanceof Date)
			return (Date) in;
		else if (in instanceof Number)
			return new Date(((Number) in).longValue());
		else if (in instanceof String)
			try {
				return format.parse((String) in);
			} catch (ParseException e) {
				throw new JetfuelException(e);
			}
		else if (in instanceof Calendar)
			return ((Calendar) in).getTime();
		else {
			throw createException(in, Date.class);
		}
	}

	/**
	 * Converts nulls, numbers, timestamps, date, time and strings to a date.
	 * 
	 * @param in
	 * @param format
	 * @return
	 */
	public static final synchronized Date toDate(Object in, String format) {
		return toDate(in, new SimpleDateFormat(format));
	}

	/**
	 * Converts null, dates, numbers and strings to a Number.
	 * 
	 * @param in
	 * @param format
	 * @return
	 */
	public static final synchronized Number toNumber(Object in, NumberFormat format) {
		if (in == null)
			return 0.0;
		else if (in instanceof Number)
			return (Number) in;
		else if (in instanceof Date)
			return ((Date) in).getTime();
		else if (in instanceof Calendar)
			return ((Calendar) in).getTimeInMillis();
		else if (in instanceof String)
			try {
				return ((String) in).isEmpty()
						? 0.0
						: format.parse((String) in);
			} catch (Exception e) {
				throw new JetfuelException(e);
			}
		else {
			throw createException(in, Number.class);
		}

	}

	/**
	 * Converts null, dates, numbers and strings to a Number.
	 * 
	 * @param in
	 * @param format
	 * @return
	 */
	public static final synchronized Number toNumber(Object in, String format) {
		return toNumber(in, new DecimalFormat(format));
	}

	/**
	 * Converts null, dates, numbers and strings to a Number.
	 * 
	 * @param in
	 * @return
	 */
	public static final synchronized Number toNumber(Object in) {
		return toNumber(in, DecimalFormat.getNumberInstance());
	}

}
