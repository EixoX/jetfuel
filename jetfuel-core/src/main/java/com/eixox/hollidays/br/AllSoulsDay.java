package com.eixox.hollidays.br;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.eixox.hollidays.Holliday;

public class AllSoulsDay implements Holliday {

	@Override
	public String getName() {
		return "All Souls Day";
	}

	@Override
	public String getReason() {
		return "All Souls’ Day is a day of alms giving and prayers for the dead. " +
				"The intent is for the living to assist those in purgatory. " +
				"Many western churches annually observe All Souls’ Day on November 2 " +
				"and many eastern churches celebrate it prior to Lent and the day before Pentecost.";
	}

	@Override
	public Calendar getCalendar(int year) {
		return new GregorianCalendar(year, 10, 2);
	}

}
