package com.eixox.hollidays.br;

import java.util.Calendar;

import com.eixox.hollidays.CalendarHelper;
import com.eixox.hollidays.Holliday;

/**
 * O Corpus Christi é um feriado facultativo comemorado pela religião Católica.
 * 
 * 
 * @author rportela
 *
 */
public class CorpusChristi implements Holliday {

	@Override
	public String getName() {
		return "Corpus Christi in Brazil";
	}

	@Override
	public String getReason() {
		return "Corpus Christi is a Christian observance that honors the Holy Eucharist. " +
				"It is also known as the Feast of the Most Holy Body of Christ, as well as the Day of Wreaths.";
	}

	/**
	 * Esta data é celebrada anualmente 60 dias depois da Páscoa, sempre na
	 * quinta-feira seguinte ao Domingo da Santíssima Trindade (domingo seguinte ao
	 * Domingo de Pentecostes), normalmente com procissões em vias públicas.
	 */
	@Override
	public Calendar getCalendar(int year) {
		Calendar easter = CalendarHelper.getEasterSunday(year);
		easter.add(Calendar.DAY_OF_MONTH, 60);
		return easter;
	}
}
