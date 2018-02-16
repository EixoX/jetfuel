package com.eixox.hollidays.br;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.eixox.hollidays.Holliday;

public class NewYearsDay implements Holliday {
	
	@Override
	public String getName() {
		return "New Year's Day";
	}

	@Override
	public String getReason() {
		return "New Year's Day";
	}

	@Override
	public Calendar getCalendar(int year) {
		return new GregorianCalendar(year, 0, 1);
	}
}
