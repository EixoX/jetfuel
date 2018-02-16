package com.eixox.hollidays.br;

import java.util.Calendar;

import com.eixox.hollidays.CalendarHelper;
import com.eixox.hollidays.Holliday;

public class GoodFriday implements Holliday {

	@Override
	public String getName() {
		return "Good Friday";
	}

	@Override
	public String getReason() {
		return "Many Christians around the world observe Good Friday on the Friday before Easter Sunday. " +
				"It commemorates Jesus Christ’s Passion, crucifixion, and death, which is told in the Christian Bible. "
				+
				"It is the day after Maundy Thursday.";
	}

	/**
	 * A Sexta-Feira Santa, também chamada de Sexta Feira da Paixão, é a sexta-feira
	 * que ocorre antes do domingo de Páscoa, e é o dia em que os cristãos relembram
	 * a morte de Jesus Cristo.
	 */
	@Override
	public Calendar getCalendar(int year) {
		Calendar easter = CalendarHelper.getEasterSunday(year);
		easter.add(Calendar.DAY_OF_MONTH, -2);
		return easter;
	}

}
