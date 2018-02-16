package com.eixox.hollidays.br;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.eixox.hollidays.Holliday;

public class TiradentesDay implements Holliday {

	@Override
	public String getName() {
		return "Tiradentes Day";
	}

	@Override
	public String getReason() {
		return "Tiradentes Day";
	}

	@Override
	public Calendar getCalendar(int year) {
		return new GregorianCalendar(year, 03, 21);
	}

}
