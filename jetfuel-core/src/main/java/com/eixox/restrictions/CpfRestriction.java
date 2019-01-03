package com.eixox.restrictions;

public class CpfRestriction implements Restriction {

	public CpfRestriction() {
	}

	public CpfRestriction(Cpf cpf) {
		// just complying to a constructor pattern.
	}

	public static boolean isValidObject(Object value) {
		try {
			if (value == null)
				return true;
			else if (value instanceof String) {
				String is = (String) value;
				if (is.isEmpty())
					return true;
				else {
					return isValid(Long.parseLong(is));
				}
			} else if (value instanceof Number) {
				return isValid(((Number) value).longValue());
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isValid(long value) {
		if (value < 1)
			return false;

		if (value == 11111111111L ||
				value == 22222222222L ||
				value == 33333333333L ||
				value == 44444444444L ||
				value == 55555555555L ||
				value == 66666666666L ||
				value == 77777777777L ||
				value == 88888888888L ||
				value == 99999999999L)
			return false;

		long a = ((value / 10000000000L) % 10);
		long b = ((value / 1000000000L) % 10);
		long c = ((value / 100000000L) % 10);
		long d = ((value / 10000000L) % 10);
		long e = ((value / 1000000L) % 10);
		long f = ((value / 100000L) % 10);
		long g = ((value / 10000L) % 10);
		long h = ((value / 1000L) % 10);
		long i = ((value / 100L) % 10);

		// Note: compute 1st verification digit.
		long d1 = (a * 1 + b * 2 + c * 3 + d * 4 + e * 5 + f * 6 + g * 7 + h * 8 + i * 9) % 11;
		if (d1 == 10)
			d1 = 0;

		// Note: compute 2nd verification digit.
		long d2 = (b * 1 + c * 2 + d * 3 + e * 4 + f * 5 + g * 6 + h * 7 + i * 8 + d1 * 9) % 11;
		if (d2 == 10)
			d2 = 0;

		return (d1 == ((value / 10) % 10) && d2 == (value % 10));
	}

	public RestrictionResult validate(Object input) {
		return isValidObject(input)
				? new RestrictionResult(true, "")
				: new RestrictionResult(false, "Não é um CPF válido.");
	}

}
