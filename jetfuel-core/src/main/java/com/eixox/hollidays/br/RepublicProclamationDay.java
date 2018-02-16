package com.eixox.hollidays.br;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.eixox.hollidays.Holliday;

public class RepublicProclamationDay implements Holliday {

	@Override
	public String getName() {
		return "Republic Proclamation Day";
	}

	@Override
	public String getReason() {
		return "Republic Proclamation Day";
	}

	@Override
	public Calendar getCalendar(int year) {
		return new GregorianCalendar(year, 10, 15);
	}

}
