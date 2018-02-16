package com.eixox.hollidays.br;

import java.util.Calendar;

import com.eixox.hollidays.CalendarHelper;
import com.eixox.hollidays.Holliday;

public class CarnivalTuesday implements Holliday {

	@Override
	public String getName() {
		return "Carnival Tuesday";
	}

	@Override
	public String getReason() {
		return "Carnival Tuesday";
	}

	@Override
	public Calendar getCalendar(int year) {
		Calendar easter = CalendarHelper.getEasterSunday(year);
		easter.add(Calendar.DAY_OF_MONTH, -47);
		return easter;

	}

}
