package com.eixox;

import java.util.Calendar;
import java.util.Date;

public class Dates {

	public static synchronized final Date addYmd(Date date, int years, int months, int days) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if (years != 0) {
			cal.add(Calendar.YEAR, years);
		}
		if (months != 0) {
			cal.add(Calendar.MONTH, months);
		}
		if (days != 0) {
			cal.add(Calendar.DATE, days);
		}
		return cal.getTime();

	}

}
