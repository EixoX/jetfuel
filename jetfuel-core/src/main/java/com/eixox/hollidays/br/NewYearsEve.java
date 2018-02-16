package com.eixox.hollidays.br;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.eixox.hollidays.Holliday;

public class NewYearsEve implements Holliday {

	@Override
	public String getName() {
		return "New Year's Eve";
	}

	@Override
	public String getReason() {
		return "New Year's Eve";
	}

	@Override
	public Calendar getCalendar(int year) {
		return new GregorianCalendar(year, 11, 31);
	}

}
