package com.eixox.adapters;

public class PhoneAdapter extends LongAdapter {

	public static final String formatPhone(long phone) {

		String str = Long.toString(phone);
		switch (str.length()) {
		case 0:
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
			return str;
		case 6:
		case 7:
		case 8:
		case 9:
			return str.substring(0, str.length() - 4)
					+ "-" +
					str.substring(str.length() - 4, str.length());
		case 10:
		case 11:
			return "(" +
					str.substring(0, 2)
					+ ") " +
					str.substring(2, str.length() - 4) +
					"-" +
					str.substring(str.length() - 4, str.length());
		default:
			int npos = str.length() - 11;
			return str.substring(0, npos) +
					" (" +
					str.substring(npos, npos + 2)
					+ ") " +
					str.substring(npos + 2, str.length() - 4) +
					"-" +
					str.substring(str.length() - 4, str.length());
		}
	}

	@Override
	public String format(Long source) {
		return formatPhone(source);
	}
}
