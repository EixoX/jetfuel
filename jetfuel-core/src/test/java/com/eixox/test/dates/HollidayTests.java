package com.eixox.test.dates;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Assert;
import org.junit.Test;

import com.eixox.hollidays.CalendarHelper;
import com.eixox.hollidays.br.CarnivalMonday;

public class HollidayTests {

	private void assertEqual(Calendar a, Calendar b) {
		boolean areEqual = a.getTimeInMillis() == b.getTimeInMillis();
		DateFormat format = DateFormat.getDateTimeInstance();
		String message = format.format(a.getTime()) + " != " + format.format(b.getTime());
		Assert.assertTrue(message, areEqual);
	}

	@Test
	public void testEasterSunday() {

		assertEqual(CalendarHelper.getEasterSunday(2000), new GregorianCalendar(2000, 3, 23));
		assertEqual(CalendarHelper.getEasterSunday(2001), new GregorianCalendar(2001, 3, 15));
		assertEqual(CalendarHelper.getEasterSunday(2002), new GregorianCalendar(2002, 2, 31));
		assertEqual(CalendarHelper.getEasterSunday(2003), new GregorianCalendar(2003, 3, 20));
		assertEqual(CalendarHelper.getEasterSunday(2004), new GregorianCalendar(2004, 3, 11));
		assertEqual(CalendarHelper.getEasterSunday(2005), new GregorianCalendar(2005, 2, 27));
		assertEqual(CalendarHelper.getEasterSunday(2006), new GregorianCalendar(2006, 3, 16));
		assertEqual(CalendarHelper.getEasterSunday(2007), new GregorianCalendar(2007, 3, 8));
		assertEqual(CalendarHelper.getEasterSunday(2008), new GregorianCalendar(2008, 2, 23));
		assertEqual(CalendarHelper.getEasterSunday(2009), new GregorianCalendar(2009, 3, 12));
		assertEqual(CalendarHelper.getEasterSunday(2010), new GregorianCalendar(2010, 3, 4));
		assertEqual(CalendarHelper.getEasterSunday(2011), new GregorianCalendar(2011, 3, 24));
		assertEqual(CalendarHelper.getEasterSunday(2012), new GregorianCalendar(2012, 3, 8));
		assertEqual(CalendarHelper.getEasterSunday(2013), new GregorianCalendar(2013, 2, 31));
		assertEqual(CalendarHelper.getEasterSunday(2014), new GregorianCalendar(2014, 3, 20));
		assertEqual(CalendarHelper.getEasterSunday(2015), new GregorianCalendar(2015, 3, 5));
		assertEqual(CalendarHelper.getEasterSunday(2016), new GregorianCalendar(2016, 2, 27));
		assertEqual(CalendarHelper.getEasterSunday(2017), new GregorianCalendar(2017, 3, 16));
		assertEqual(CalendarHelper.getEasterSunday(2018), new GregorianCalendar(2018, 3, 1));
		assertEqual(CalendarHelper.getEasterSunday(2019), new GregorianCalendar(2019, 3, 21));
		assertEqual(CalendarHelper.getEasterSunday(2020), new GregorianCalendar(2020, 3, 12));
		assertEqual(CalendarHelper.getEasterSunday(2021), new GregorianCalendar(2021, 3, 4));
		assertEqual(CalendarHelper.getEasterSunday(2022), new GregorianCalendar(2022, 3, 17));
		assertEqual(CalendarHelper.getEasterSunday(2023), new GregorianCalendar(2023, 3, 9));
		assertEqual(CalendarHelper.getEasterSunday(2024), new GregorianCalendar(2024, 2, 31));
		assertEqual(CalendarHelper.getEasterSunday(2025), new GregorianCalendar(2025, 3, 20));
		assertEqual(CalendarHelper.getEasterSunday(2026), new GregorianCalendar(2026, 3, 5));
		assertEqual(CalendarHelper.getEasterSunday(2027), new GregorianCalendar(2027, 2, 28));
		assertEqual(CalendarHelper.getEasterSunday(2028), new GregorianCalendar(2028, 3, 16));
		assertEqual(CalendarHelper.getEasterSunday(2029), new GregorianCalendar(2029, 3, 1));
		assertEqual(CalendarHelper.getEasterSunday(2030), new GregorianCalendar(2030, 3, 21));
		assertEqual(CalendarHelper.getEasterSunday(2031), new GregorianCalendar(2031, 3, 13));
		assertEqual(CalendarHelper.getEasterSunday(2032), new GregorianCalendar(2032, 2, 28));
		assertEqual(CalendarHelper.getEasterSunday(2033), new GregorianCalendar(2033, 3, 17));
		assertEqual(CalendarHelper.getEasterSunday(2034), new GregorianCalendar(2034, 3, 9));
		assertEqual(CalendarHelper.getEasterSunday(2035), new GregorianCalendar(2035, 2, 25));
		assertEqual(CalendarHelper.getEasterSunday(2036), new GregorianCalendar(2036, 3, 13));
		assertEqual(CalendarHelper.getEasterSunday(2037), new GregorianCalendar(2037, 3, 5));
		assertEqual(CalendarHelper.getEasterSunday(2038), new GregorianCalendar(2038, 3, 25));
		assertEqual(CalendarHelper.getEasterSunday(2039), new GregorianCalendar(2039, 3, 10));
		assertEqual(CalendarHelper.getEasterSunday(2040), new GregorianCalendar(2040, 3, 1));
		assertEqual(CalendarHelper.getEasterSunday(2041), new GregorianCalendar(2041, 3, 21));
		assertEqual(CalendarHelper.getEasterSunday(2042), new GregorianCalendar(2042, 3, 6));
		assertEqual(CalendarHelper.getEasterSunday(2043), new GregorianCalendar(2043, 2, 29));
		assertEqual(CalendarHelper.getEasterSunday(2044), new GregorianCalendar(2044, 3, 17));
		assertEqual(CalendarHelper.getEasterSunday(2045), new GregorianCalendar(2045, 3, 9));
		assertEqual(CalendarHelper.getEasterSunday(2046), new GregorianCalendar(2046, 2, 25));
		assertEqual(CalendarHelper.getEasterSunday(2047), new GregorianCalendar(2047, 3, 14));
		assertEqual(CalendarHelper.getEasterSunday(2048), new GregorianCalendar(2048, 3, 5));
		assertEqual(CalendarHelper.getEasterSunday(2049), new GregorianCalendar(2049, 3, 18));

	}

	@Test
	public void testCarinvalMonday() {

		assertEqual(new CarnivalMonday().getCalendar(2016), new GregorianCalendar(2016, 1, 8));
	}
}
