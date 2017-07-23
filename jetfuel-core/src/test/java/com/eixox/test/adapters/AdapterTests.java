package com.eixox.test.adapters;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import com.eixox.UUIDHelper;
import com.eixox.adapters.BooleanAdapter;
import com.eixox.adapters.ByteArrayAdapter;
import com.eixox.adapters.DateAdapter;
import com.eixox.adapters.DateYmdAdapter;
import com.eixox.adapters.DoubleAdapter;
import com.eixox.adapters.IntegerAdapter;
import com.eixox.adapters.LongAdapter;

public class AdapterTests {

	public boolean myField = true;

	public int myInt = -1;

	public double myDouble = -2.0;

	public long myLong = -3L;

	@Test
	public void setNullBoolean() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		BooleanAdapter adapter = new BooleanAdapter();
		Field field = getClass().getField("myField");
		field.set(this, adapter.convert(null));
		Assert.assertTrue(this.myField == false);
	}

	@Test
	public void setNullInteger() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		IntegerAdapter adapter = new IntegerAdapter();
		Field field = getClass().getField("myInt");
		field.set(this, adapter.convert(null));
		Assert.assertTrue(this.myInt == 0);
	}

	@Test
	public void setNullDouble() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		DoubleAdapter adapter = new DoubleAdapter();
		Field field = getClass().getField("myDouble");
		field.set(this, adapter.convert(null));
		Assert.assertTrue(this.myDouble == 0.0);
	}

	@Test
	public void setNullLong() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		LongAdapter adapter = new LongAdapter();
		Field field = getClass().getField("myLong");
		field.set(this, adapter.convert(null));
		Assert.assertTrue(this.myLong == 0L);
	}

	@Test
	public void dateYMDio() {
		DateAdapter adapter = new DateAdapter("yyyyMMdd");
		Date date = adapter.parse("20161231");
		Assert.assertTrue(date.getTime() == new GregorianCalendar(2016, 11, 31).getTimeInMillis());
		String formatted = adapter.format(date);
		Assert.assertTrue("20161231".equals(formatted));
	}

	@Test
	public void dateYMDio2() {
		DateYmdAdapter adapter = new DateYmdAdapter();
		Date date = adapter.parse("20161231");
		Assert.assertTrue(date.getTime() == new GregorianCalendar(2016, 11, 31).getTimeInMillis());
		String formatted = adapter.format(date);
		Assert.assertTrue("20161231".equals(formatted));
	}

	private double getTimeElapsed(Date from) {
		long ms = new Date().getTime() - from.getTime();
		return ms / 1000.0;
	}

	@Test
	public void timebasedUUID() {

		Date now = new Date();
		System.out.println("GENERATION STARTED: " + now);

		int SIZE = 1000000;
		ArrayList<UUID> list = new ArrayList<UUID>(SIZE);
		for (int i = 0; i < SIZE; i++) {
			list.add(UUIDHelper.generateTimebased());
			if (i % 1000 == 0)
				System.out.println(list.get(i));
		}

		System.out.println("GENERATION ENDED. Took " + getTimeElapsed(now) + " seconds.");
		now = new Date();
		System.out.println("DUPLICATE CHECK STARTED: " + now);

		// check for repeated values.
		HashSet<UUID> set = new HashSet<UUID>(SIZE);
		for (int i = 0; i < SIZE; i++)
			if (set.contains(list.get(i)))
				Assert.fail("DUPLICATE " + list.get(i) + " at " + i);
			else
				set.add(list.get(i));

		System.out.println("DUPLICATE CHECK ENDED (" + set.size() + "). Took " + getTimeElapsed(now) + " seconds.");
		now = new Date();
		System.out.println("CLONE CHECK STARTED: " + now);
		// clone
		ArrayList<UUID> clone = new ArrayList<UUID>(list.size());
		for (int i = 0; i < SIZE; i++) {
			UUID old = list.get(i);
			clone.add(new UUID(old.getMostSignificantBits(), old.getLeastSignificantBits()));
		}
		System.out.println("CLONE ENDED. Took " + getTimeElapsed(now) + " seconds.");
		now = new Date();
		System.out.println("SORTING CHECK STARTED: " + now);

		Collections.sort(list);

		for (int i = 0; i < SIZE; i++) {
			boolean areEqual = list.get(i).equals(clone.get(i));
			if (!areEqual) {
				Assert.fail(list.get(i) + " != " + clone.get(i) + " at " + i);
			}
		}
		System.out.println("SORTING CHECK ENDED. Took " + getTimeElapsed(now) + " seconds.");

	}

	@Test
	public void testByteArray() {

		ByteArrayAdapter baa = new ByteArrayAdapter();
		String text = "rodrigo portela";

		byte[] bytes = text.getBytes();
		String formatted = baa.format(bytes);
		System.out.println(formatted);

		byte[] parsed = baa.parse(formatted);
		String parsedText = new String(parsed);
		System.out.println(parsedText);

		Assert.assertTrue(parsed.length == bytes.length);
		for (int i = 0; i < parsed.length; i++)
			Assert.assertEquals(parsed[i], bytes[i]);

		Assert.assertTrue(text.equals(parsedText));

	}
}
