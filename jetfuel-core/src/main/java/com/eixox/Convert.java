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

	/**
	 * Converts nulls, booleans, strings and numbers to boolean.
	 * 
	 * @param in
	 * @return
	 */
	public static synchronized final boolean toBoolean(Object in) {
		if (in == null)
			return false;
		else if (in instanceof Boolean)
			return (Boolean) in;
		else if (in instanceof Number)
			return ((Number) in).intValue() != 0;
		else if (in instanceof String)
			return ((String) in).isEmpty()
					? false
					: Boolean.parseBoolean((String) in);
		else
			throw new RuntimeException("Can't convert " + in.getClass() + " to " + Boolean.TYPE);
	}

	/**
	 * Converts nulls, numbers and strings to byte.
	 * 
	 * @param in
	 * @return
	 */
	public static synchronized final byte toByte(Object in) {
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
		else
			throw new RuntimeException("Can't convert " + in.getClass() + " to " + Byte.TYPE);
	}

	/**
	 * Converts null, numbers, dates and strings to char.
	 * 
	 * @param in
	 * @return
	 */
	public static synchronized final char toChar(Object in) {
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
		else
			throw new RuntimeException("Can't convert " + in.getClass() + " to " + Character.TYPE);
	}

	/**
	 * Converts null, numbers, dates and strings to short.
	 * 
	 * @param in
	 * @return
	 */
	public static synchronized final short toShort(Object in) {
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
		else
			throw new RuntimeException("Can't convert " + in.getClass() + " to " + Short.TYPE);
	}

	/**
	 * Converts null, numbers, dates and strings to int.
	 * 
	 * @param in
	 * @return
	 */
	public static synchronized final int toInt(Object in) {
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
		else
			throw new RuntimeException("Can't convert " + in.getClass() + " to " + Integer.TYPE);
	}

	/**
	 * Converts null, numbers, dates and strings to long.
	 * 
	 * @param in
	 * @return
	 */
	public static synchronized final long toLong(Object in) {
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
		else
			throw new RuntimeException("Can't convert " + in.getClass() + " to " + Long.TYPE);
	}

	/**
	 * Converts nulls, numbers, dates and strings to float.
	 * 
	 * @param in
	 * @return
	 */
	public static synchronized final float toFloat(Object in) {
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
		else
			throw new RuntimeException("Can't convert " + in.getClass() + " to " + Float.TYPE);
	}

	/**
	 * Converts nulls, numbers, dates and strings to double.
	 * 
	 * @param in
	 * @return
	 */
	public static synchronized final double toDouble(Object in) {
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
		else
			throw new RuntimeException("Can't convert " + in.getClass() + " to " + Double.TYPE);
	}

	/**
	 * Converts nulls, numbers, timestamps, date, time and strings to date.
	 * 
	 * @param in
	 * @return
	 */
	public static synchronized final Date toDate(Object in, DateFormat format) {
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
				throw new RuntimeException(e);
			}
		else if (in instanceof Calendar)
			return ((Calendar) in).getTime();
		else
			throw new RuntimeException("Can't convert " + in.getClass() + " to " + Date.class);
	}

	/**
	 * Converts nulls, numbers, timestamps, date, time and strings to a date.
	 * 
	 * @param in
	 * @param format
	 * @return
	 */
	public static synchronized final Date toDate(Object in, String format) {
		return toDate(in, new SimpleDateFormat(format));
	}

	/**
	 * Converts null, dates, numbers and strings to a Number.
	 * 
	 * @param in
	 * @param format
	 * @return
	 */
	public static synchronized final Number toNumber(Object in, NumberFormat format) {
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
				throw new RuntimeException(e);
			}
		else
			throw new RuntimeException("Can't convert " + in.getClass() + " to " + Number.class);

	}

	/**
	 * Converts null, dates, numbers and strings to a Number.
	 * 
	 * @param in
	 * @param format
	 * @return
	 */
	public static synchronized final Number toNumber(Object in, String format) {
		return toNumber(in, new DecimalFormat(format));
	}

	/**
	 * Converts null, dates, numbers and strings to a Number.
	 * 
	 * @param in
	 * @return
	 */
	public static synchronized final Number toNumber(Object in) {
		return toNumber(in, DecimalFormat.getNumberInstance());
	}

}
