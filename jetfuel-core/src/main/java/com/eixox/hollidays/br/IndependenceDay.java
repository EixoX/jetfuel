package com.eixox.hollidays.br;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.eixox.hollidays.Holliday;

public class IndependenceDay implements Holliday {

	@Override
	public String getName() {
		return "Independence Day";
	}

	@Override
	public String getReason() {
		return "Independence Day";
	}

	@Override
	public Calendar getCalendar(int year) {
		return new GregorianCalendar(year, 8, 07);
	}

}
