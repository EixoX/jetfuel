package com.eixox.hollidays.br;

import java.util.Calendar;

import com.eixox.hollidays.CalendarHelper;
import com.eixox.hollidays.Holliday;

public class CarnivalMonday implements Holliday {

	@Override
	public String getName() {
		return "Carnival Monday";
	}

	@Override
	public String getReason() {
		return "Carnival Monday";
	}

	@Override
	public Calendar getCalendar(int year) {
		Calendar easter = CalendarHelper.getEasterSunday(year);
		easter.add(Calendar.DAY_OF_MONTH, -48);
		return easter;

	}

}
