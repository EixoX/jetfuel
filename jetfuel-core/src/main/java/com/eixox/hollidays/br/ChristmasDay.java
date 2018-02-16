package com.eixox.hollidays.br;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.eixox.hollidays.Holliday;

public class ChristmasDay implements Holliday {

	@Override
	public String getName() {
		return "Christmas Day";
	}

	@Override
	public String getReason() {
		return "Christmas Day";
	}

	@Override
	public Calendar getCalendar(int year) {
		return new GregorianCalendar(year, 11, 25);
	}

}
