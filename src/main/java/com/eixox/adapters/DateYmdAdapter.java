package com.eixox.adapters;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateYmdAdapter extends DateAdapter {

	public final Date parse(String source) {
		if (source == null || source.isEmpty())
			return null;

		int year = 0;
		int month = 0;
		int day = 0;
		int hour = 0;
		int minute = 0;
		int second = 0;

		switch (source.length()) {
		case 1:
		case 2:
		case 3:
			year = Integer.parseInt(source);
			month = 1;
			day = 1;
			break;
		case 4:
			year = Integer.parseInt(source.substring(0, 2));
			month = Integer.parseInt(source.substring(2, 4));
			day = 1;
			break;
		case 5:
			year = Integer.parseInt(source.substring(0, 2));
			month = Integer.parseInt(source.substring(2, 3));
			day = Integer.parseInt(source.substring(3, 5));
			break;
		case 6:
			year = Integer.parseInt(source.substring(0, 2));
			month = Integer.parseInt(source.substring(2, 4));
			day = Integer.parseInt(source.substring(4, 6));
			break;
		case 7:
			year = Integer.parseInt(source.substring(0, 4));
			month = Integer.parseInt(source.substring(4, 5));
			day = Integer.parseInt(source.substring(5, 7));
			break;
		case 8:
			year = Integer.parseInt(source.substring(0, 4));
			month = Integer.parseInt(source.substring(4, 6));
			day = Integer.parseInt(source.substring(6, 8));
			break;
		case 10:
			year = Integer.parseInt(source.substring(0, 4));
			month = Integer.parseInt(source.substring(4, 6));
			day = Integer.parseInt(source.substring(6, 8));
			hour = Integer.parseInt(source.substring(8, 10));
			break;
		case 12:
			year = Integer.parseInt(source.substring(0, 4));
			month = Integer.parseInt(source.substring(4, 6));
			day = Integer.parseInt(source.substring(6, 8));
			hour = Integer.parseInt(source.substring(8, 10));
			minute = Integer.parseInt(source.substring(10, 12));
			break;
		case 14:
			year = Integer.parseInt(source.substring(0, 4));
			month = Integer.parseInt(source.substring(4, 6));
			day = Integer.parseInt(source.substring(6, 8));
			hour = Integer.parseInt(source.substring(8, 10));
			minute = Integer.parseInt(source.substring(10, 12));
			second = Integer.parseInt(source.substring(12, 14));
			break;
		case 15:
			year = Integer.parseInt(source.substring(0, 4));
			month = Integer.parseInt(source.substring(4, 6));
			day = Integer.parseInt(source.substring(6, 8));
			hour = Integer.parseInt(source.substring(9, 11));
			minute = Integer.parseInt(source.substring(11, 13));
			second = Integer.parseInt(source.substring(13, 15));
			break;
		case 16:
			year = Integer.parseInt(source.substring(0, 4));
			month = Integer.parseInt(source.substring(4, 6));
			day = Integer.parseInt(source.substring(6, 8));
			hour = Integer.parseInt(source.substring(8, 10));
			minute = Integer.parseInt(source.substring(11, 13));
			second = Integer.parseInt(source.substring(14, 16));
			break;
		case 17:
			year = Integer.parseInt(source.substring(0, 4));
			month = Integer.parseInt(source.substring(4, 6));
			day = Integer.parseInt(source.substring(6, 8));
			hour = Integer.parseInt(source.substring(9, 11));
			minute = Integer.parseInt(source.substring(12, 14));
			second = Integer.parseInt(source.substring(15, 17));
			break;
		default:
			throw new RuntimeException("Unrecognizable YMD date format on " + source);
		}

		if (year < 100) {
			if (year < 60)
				year += 2000;
			else
				year += 1900;
		}

		return new GregorianCalendar(year, month - 1, day, hour, minute, second).getTime();
	}

	@Override
	public String format(Date source) {
		if (source == null)
			return "";

		Calendar cal = Calendar.getInstance();
		cal.setTime(source);

		int y = cal.get(Calendar.YEAR);
		int M = cal.get(Calendar.MONTH) + 1;
		int d = cal.get(Calendar.DATE);
		int h = cal.get(Calendar.HOUR_OF_DAY);
		int m = cal.get(Calendar.MINUTE);
		int s = cal.get(Calendar.SECOND);

		String p1 = Integer.toString(y) +
				(M < 10
						? "0" + Integer.toString(M)
						: Integer.toString(M))
				+
				(d < 10
						? "0" + Integer.toString(d)
						: Integer.toString(d));

		if (h == 0 && m == 0 && s == 0)
			return p1;
		else
			return p1 +
					(h < 10
							? "0" + Integer.toString(h)
							: Integer.toString(h))
					+
					(m < 10
							? "0" + Integer.toString(m)
							: Integer.toString(m))
					+
					(s < 10
							? "0" + Integer.toString(s)
							: Integer.toString(s));

	}
}
