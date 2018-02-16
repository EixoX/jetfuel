package com.eixox.test.dates;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Assert;
import org.junit.Test;

public class DateTests {

	@Test
	public void testAddMonth() {

		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = new GregorianCalendar(2016, 0, 31);
		System.out.println(format.format(cal.getTime()));

		cal.add(Calendar.MONTH, 1);
		System.out.println(format.format(cal.getTime()));
		Assert.assertTrue(cal.getTimeInMillis() == new GregorianCalendar(2016, 1, 29).getTimeInMillis());

		cal.add(Calendar.MONTH, 1);
		System.out.println(format.format(cal.getTime()));
		Assert.assertTrue(cal.getTimeInMillis() == new GregorianCalendar(2016, 2, 29).getTimeInMillis());

	}
}
