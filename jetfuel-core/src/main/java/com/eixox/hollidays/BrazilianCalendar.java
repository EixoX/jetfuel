package com.eixox.hollidays;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.eixox.hollidays.br.AllSoulsDay;
import com.eixox.hollidays.br.BlackConsciousnessDay;
import com.eixox.hollidays.br.CarnivalMonday;
import com.eixox.hollidays.br.CarnivalTuesday;
import com.eixox.hollidays.br.CarnivalWednesday;
import com.eixox.hollidays.br.ChildrensDay;
import com.eixox.hollidays.br.ChristmasDay;
import com.eixox.hollidays.br.CorpusChristi;
import com.eixox.hollidays.br.GoodFriday;
import com.eixox.hollidays.br.IndependenceDay;
import com.eixox.hollidays.br.LaborDay;
import com.eixox.hollidays.br.NewYearsDay;
import com.eixox.hollidays.br.NewYearsEve;
import com.eixox.hollidays.br.RepublicProclamationDay;
import com.eixox.hollidays.br.TiradentesDay;

/**
 * A helper class that can determine if a given date is a business day or not;
 * It also knows every holliday in Brazil;
 * 
 * @author rportela
 *
 */
public class BrazilianCalendar {

	/**
	 * Holds a static list of brazilian hollidays;
	 */
	public static final List<Holliday> HOLLIDAYS;

	static {
		HOLLIDAYS = new LinkedList<Holliday>();
		HOLLIDAYS.add(new AllSoulsDay());
		HOLLIDAYS.add(new BlackConsciousnessDay());
		HOLLIDAYS.add(new CarnivalMonday());
		HOLLIDAYS.add(new CarnivalTuesday());
		HOLLIDAYS.add(new CarnivalWednesday());
		HOLLIDAYS.add(new ChildrensDay());
		HOLLIDAYS.add(new ChristmasDay());
		HOLLIDAYS.add(new CorpusChristi());
		HOLLIDAYS.add(new GoodFriday());
		HOLLIDAYS.add(new IndependenceDay());
		HOLLIDAYS.add(new LaborDay());
		HOLLIDAYS.add(new NewYearsDay());
		HOLLIDAYS.add(new NewYearsEve());
		HOLLIDAYS.add(new RepublicProclamationDay());
		HOLLIDAYS.add(new TiradentesDay());
	}

	/**
	 * Indicates that a given calendar date is a business day or not;
	 * 
	 * @param calendar
	 * @return
	 */
	public static boolean isBusinessDay(final Calendar calendar) {
		int day_of_week = calendar.get(Calendar.DAY_OF_WEEK);
		if (day_of_week == Calendar.SUNDAY)
			return false;
		if (day_of_week == Calendar.SATURDAY)
			return false;
		for (Holliday holliday : HOLLIDAYS) {
			Calendar hol = holliday.getCalendar(calendar.get(Calendar.YEAR));
			if (hol.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
					hol.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH) &&
					hol.get(Calendar.YEAR) == calendar.get(Calendar.YEAR))
				return true;
		}
		return true;
	}

	/**
	 * Indicates that a given date is a business day or not based on the week day
	 * and the list of hollidays for Brazil;
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isBusinessDay(final Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return isBusinessDay(cal);
	}

}
