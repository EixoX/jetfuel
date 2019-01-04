package com.eixox;

import java.util.HashMap;
import java.util.Map;

public final class StringHelper {

	private StringHelper() {
	}

	private static Map<Character, String> htmlEncoderMap;

	private static synchronized Map<Character, String> buildHtmlEncoderMap() {
		if (htmlEncoderMap == null) {
			htmlEncoderMap = new HashMap<>(100);
			htmlEncoderMap.put('"', "&quot;");
			htmlEncoderMap.put('\'', "&apos;");
			htmlEncoderMap.put('&', "&amp;");
			htmlEncoderMap.put('<', "&lt;");
			htmlEncoderMap.put('>', "&gt;");
			htmlEncoderMap.put('¡', "&iexcl;");
			htmlEncoderMap.put('¢', "&cent;");
			htmlEncoderMap.put('£', "&pound;");
			htmlEncoderMap.put('¤', "&curren;");
			htmlEncoderMap.put('¥', "&yen;");
			htmlEncoderMap.put('¦', "&brvbar;");
			htmlEncoderMap.put('§', "&sect;");
			htmlEncoderMap.put('¨', "&uml;");
			htmlEncoderMap.put('©', "&copy;");
			htmlEncoderMap.put('ª', "&ordf;");
			htmlEncoderMap.put('«', "&laquo;");
			htmlEncoderMap.put('¬', "&not;");
			htmlEncoderMap.put('®', "&reg;");
			htmlEncoderMap.put('¯', "&macr;");
			htmlEncoderMap.put('°', "&deg;");
			htmlEncoderMap.put('±', "&plusmn;");
			htmlEncoderMap.put('²', "&sup2;");
			htmlEncoderMap.put('³', "&sup3;");
			htmlEncoderMap.put('´', "&acute;");
			htmlEncoderMap.put('µ', "&micro;");
			htmlEncoderMap.put('¶', "&para;");
			htmlEncoderMap.put('·', "&middot;");
			htmlEncoderMap.put('¸', "&cedil;");
			htmlEncoderMap.put('¹', "&sup1;");
			htmlEncoderMap.put('º', "&ordm;");
			htmlEncoderMap.put('»', "&raquo;");
			htmlEncoderMap.put('¼', "&frac14;");
			htmlEncoderMap.put('½', "&frac12;");
			htmlEncoderMap.put('¾', "&frac34;");
			htmlEncoderMap.put('¿', "&iquest;");
			htmlEncoderMap.put('×', "&times;");
			htmlEncoderMap.put('÷', "&divide;");
			htmlEncoderMap.put('À', "&Agrave;");
			htmlEncoderMap.put('Á', "&Aacute;");
			htmlEncoderMap.put('Â', "&Acirc;");
			htmlEncoderMap.put('Ã', "&Atilde;");
			htmlEncoderMap.put('Ä', "&Auml;");
			htmlEncoderMap.put('Å', "&Aring;");
			htmlEncoderMap.put('Æ', "&AElig;");
			htmlEncoderMap.put('Ç', "&Ccedil;");
			htmlEncoderMap.put('È', "&Egrave;");
			htmlEncoderMap.put('É', "&Eacute;");
			htmlEncoderMap.put('Ê', "&Ecirc;");
			htmlEncoderMap.put('Ë', "&Euml;");
			htmlEncoderMap.put('Ì', "&Igrave;");
			htmlEncoderMap.put('Í', "&Iacute;");
			htmlEncoderMap.put('Î', "&Icirc;");
			htmlEncoderMap.put('Ï', "&Iuml;");
			htmlEncoderMap.put('Ð', "&ETH;");
			htmlEncoderMap.put('Ñ', "&Ntilde;");
			htmlEncoderMap.put('Ò', "&Ograve;");
			htmlEncoderMap.put('Ó', "&Oacute;");
			htmlEncoderMap.put('Ô', "&Ocirc;");
			htmlEncoderMap.put('Õ', "&Otilde;");
			htmlEncoderMap.put('Ö', "&Ouml;");
			htmlEncoderMap.put('Ø', "&Oslash;");
			htmlEncoderMap.put('Ù', "&Ugrave;");
			htmlEncoderMap.put('Ú', "&Uacute;");
			htmlEncoderMap.put('Û', "&Ucirc;");
			htmlEncoderMap.put('Ü', "&Uuml;");
			htmlEncoderMap.put('Ý', "&Yacute;");
			htmlEncoderMap.put('Þ', "&THORN;");
			htmlEncoderMap.put('ß', "&szlig;");
			htmlEncoderMap.put('à', "&agrave;");
			htmlEncoderMap.put('á', "&aacute;");
			htmlEncoderMap.put('â', "&acirc;");
			htmlEncoderMap.put('ã', "&atilde;");
			htmlEncoderMap.put('ä', "&auml;");
			htmlEncoderMap.put('å', "&aring;");
			htmlEncoderMap.put('æ', "&aelig;");
			htmlEncoderMap.put('ç', "&ccedil;");
			htmlEncoderMap.put('è', "&egrave;");
			htmlEncoderMap.put('é', "&eacute;");
			htmlEncoderMap.put('ê', "&ecirc;");
			htmlEncoderMap.put('ë', "&euml;");
			htmlEncoderMap.put('ì', "&igrave;");
			htmlEncoderMap.put('í', "&iacute;");
			htmlEncoderMap.put('î', "&icirc;");
			htmlEncoderMap.put('ï', "&iuml;");
			htmlEncoderMap.put('ð', "&eth;");
			htmlEncoderMap.put('ñ', "&ntilde;");
			htmlEncoderMap.put('ò', "&ograve;");
			htmlEncoderMap.put('ó', "&oacute;");
			htmlEncoderMap.put('ô', "&ocirc;");
			htmlEncoderMap.put('õ', "&otilde;");
			htmlEncoderMap.put('ö', "&ouml;");
			htmlEncoderMap.put('ø', "&oslash;");
			htmlEncoderMap.put('ù', "&ugrave;");
			htmlEncoderMap.put('ú', "&uacute;");
			htmlEncoderMap.put('û', "&ucirc;");
			htmlEncoderMap.put('ü', "&uuml;");
			htmlEncoderMap.put('ý', "&yacute;");
			htmlEncoderMap.put('þ', "&thorn;");
			htmlEncoderMap.put('ÿ', "&yuml;");
		}
		return htmlEncoderMap;
	}

	public static final boolean equalsAnyIgnoreCase(String a, String... b) {
		for (int i = 0; i < b.length; i++)
			if (a.equalsIgnoreCase(b[i]))
				return true;
		return false;
	}

	public static final boolean isNullOrEmpty(Object obj) {
		return obj == null || ((String) obj).isEmpty();
	}

	public static final String urlEncode(final CharSequence input) {
		if (input == null || input.length() == 0)
			return "";
		else {
			int l = input.length();
			StringBuilder builder = new StringBuilder(l);
			for (int i = 0; i < l; i++) {
				char c = input.charAt(i);
				if (Character.isLetterOrDigit(c))
					builder.append(c);
				else {
					builder.append("%" +
							Integer.toHexString(c));
				}
			}
			return builder.toString();
		}
	}

	public static final String htmlEncode(CharSequence input) {
		if (input == null || input.length() == 0)
			return "";
		else {
			int l = input.length();
			StringBuilder builder = new StringBuilder(l);
			Map<Character, String> encoderMap = buildHtmlEncoderMap();
			for (int i = 0; i < l; i++) {
				Character c = input.charAt(i);
				String encodedChar = encoderMap.get(c);
				if (encodedChar == null)
					builder.append(c);
				else
					builder.append(encodedChar);
			}
			return builder.toString();
		}
	}

	public static final String concat(Object... objs) {
		StringBuilder builder = new StringBuilder(512);
		for (int i = 0; i < objs.length; i++)
			builder.append(objs[i].toString());
		return builder.toString();
	}

}
