package com.eixox.hollidays.br;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.eixox.hollidays.Holliday;

public class ChildrensDay implements Holliday {

	@Override
	public String getName() {
		return "Childrens Day";
	}

	@Override
	public String getReason() {
		return "Childrens Day";
	}

	@Override
	public Calendar getCalendar(int year) {
		return new GregorianCalendar(year, 9, 12);
	}

}
