package com.eixox.hollidays;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * A global handy calendar helper class;
 * 
 * @author rportela
 *
 */
public final class CalendarHelper {

	private CalendarHelper() {
	}

	/**
	 * Write a program to compute the date of Easter Sunday. Easter Sunday is the
	 * first Sunday after the first full moon of spring. Use the algorithm invented
	 * by the mathematician Carl Friedrich Gauss in 1800.
	 * 
	 * @author rportela
	 * @param y
	 * @return
	 */
	public static Calendar getEasterSunday(int y) {

		// Let y be the year (such as 1800 or 2001)
		// Divide y by 19 and call the remainder a. Ignore the quotient.
		int a = y % 19;
		// Divide y by 100 to get a quotient b and a remainder c.
		int b = y / 100;
		// Divide b by 4 to get a quotient d and a remainder e.
		int c = y % 100;
		int d = b / 4;
		int e = b % 4;
		// Divide 8 * b + 13 by 25 to get a quotient g. Ignore the remainder.
		int g = (8 * b + 13) / 25;
		// Divide 19 * a + b - d - g + 15 by 30 to get a remainder h. Ignore the
		// quotient.
		int h = (19 * a + b - d - g + 15) % 30;
		// Divide c by 4 to get a quotient j and a remainder k.
		int j = c / 4;
		int k = c % 4;
		// Divide a + 11 * h by 319 to get a quotient m. Ignore the remainder.
		int m = (a + 11 * h) / 319;
		// Divide 2 * e + 2 * j - k - h + m + 32 by 7 to get a remainder r. Ignore the
		// quotient.
		int r = (2 * e + 2 * j - k - h + m + 32) % 7;
		// Divide h - m + r + 90 by 25 to get a quotient n. Ignore the remainder.
		int n = (h - m + r + 90) / 25;
		// Divide h - m + r + n + 19 by 32 to get a remainder of p. Ignore the quotient.
		int p = (h - m + r + n + 19) % 32;
		// Then Easter falls on a day p of month n.

		/**
		 * Há dois casos particulares que ocorrem duas vezes por século:
		 * 
		 * Quando o domingo de Páscoa cair em Abril e o dia for 25 e o termo "h" for
		 * igual a 28, simultaneamente com "a" maior que 10, então o dia é corrigido
		 * para 18.
		 */
		if (n == 4 && p == 25 && h == 28 && a > 10)
			p = 18;
		/**
		 * Quando o domingo de Páscoa cair em Abril e o dia for 26, corrige-se para uma
		 * semana antes, ou seja, vai para dia 19;
		 */
		if (n == 4 && p == 26)
			p = 19;

		/**
		 * Neste século estes dois casos particulares só acontecerão em 2049 e 2076.
		 */

		return new GregorianCalendar(y, n - 1, p);
	}

	/**
	 * Pentecostes é uma celebração muito importante do calendário cristão, e
	 * comemora a descida do Espírito Santo sobre os apóstolos de Jesus Cristo.
	 * 
	 * Tradicionalmente, o Pentecostes é celebrado 50 dias depois do domingo de
	 * Páscoa, e no décimo dia depois do dia da Ascensão.
	 * 
	 * Pentecostes celebra a descida do Espírito Santo sobre os apóstolos e
	 * seguidores de Cristo, durante aquela celebração judaica do quinquagésimo dia
	 * em Jerusalém. Esta data também é considerada o dia do nascimento da igreja.
	 * 
	 * O termo Pentecostes é de origem grega, e o seu significado é referente ao
	 * número quinquagésimo (50º). Juntamente com Natal e a Páscoa, esta é a
	 * terceira data mais importante do Ano Litúrgico.
	 * 
	 * No Novo Testamento, Pentecostes é o dia da vinda do Espírito Santo, da
	 * chegada de Cristo na Terra. O Pentecostes marca o final da festa Pascal, de
	 * acordo com a doutrina cristã.
	 * 
	 * @author rportela
	 * @param y
	 * @return
	 */
	public static Calendar getPentecostes(int y) {
		Calendar easter = getEasterSunday(y);
		easter.add(Calendar.DAY_OF_MONTH, 50);
		return easter;
	}

	
}
