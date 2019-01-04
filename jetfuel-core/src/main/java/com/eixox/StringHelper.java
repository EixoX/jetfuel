package com.eixox;

public final class StringHelper {

	private StringHelper() {
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

	public static final boolean isNullOrEmpty(String str) {
		return str == null || str.isEmpty();
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
					builder.append("%");
					builder.append(Integer.toHexString(c));
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
			for (int i = 0; i < l; i++) {
				char c = input.charAt(i);
				switch (c) {
				case '"':
					builder.append("&quot;");
					break; // quotation mark
				case '\'':
					builder.append("&apos;");
					break;// apostrophe
				case '&':
					builder.append("&amp;");
					break;// ampersand
				case '<':
					builder.append("&lt;");
					break;// less-than
				case '>':
					builder.append("&gt;");
					break;// greater-than
				case '¡':
					builder.append("&iexcl;");
					break;// inverted exclamation mark
				case '¢':
					builder.append("&cent;");
					break;// cent
				case '£':
					builder.append("&pound;");
					break;// pound
				case '¤':
					builder.append("&curren;");
					break;// currency
				case '¥':
					builder.append("&yen;");
					break;// yen
				case '¦':
					builder.append("&brvbar;");
					break;// broken vertical bar
				case '§':
					builder.append("&sect;");
					break;// section
				case '¨':
					builder.append("&uml;");
					break;// spacing diaeresis
				case '©':
					builder.append("&copy;");
					break;// copyright
				case 'ª':
					builder.append("&ordf;");
					break;// feminine ordinal indicator
				case '«':
					builder.append("&laquo;");
					break;// angle quotation mark (left)
				case '¬':
					builder.append("&not;");
					break;// negation
				case '®':
					builder.append("&reg;");
					break;// registered trademark
				case '¯':
					builder.append("&macr;");
					break;// spacing macron
				case '°':
					builder.append("&deg;");
					break;// degree
				case '±':
					builder.append("&plusmn;");
					break;// plus-or-minus
				case '²':
					builder.append("&sup2;");
					break;// superscript 2
				case '³':
					builder.append("&sup3;");
					break;// superscript 3
				case '´':
					builder.append("&acute;");
					break;// spacing acute
				case 'µ':
					builder.append("&micro;");
					break;// micro
				case '¶':
					builder.append("&para;");
					break;// paragraph
				case '·':
					builder.append("&middot;");
					break;// middle dot
				case '¸':
					builder.append("&cedil;");
					break;// spacing cedilla
				case '¹':
					builder.append("&sup1;");
					break;// superscript 1
				case 'º':
					builder.append("&ordm;");
					break;// masculine ordinal indicator
				case '»':
					builder.append("&raquo;");
					break;// angle quotation mark (right)
				case '¼':
					builder.append("&frac14;");
					break;// fraction 1/4
				case '½':
					builder.append("&frac12;");
					break;// fraction 1/2
				case '¾':
					builder.append("&frac34;");
					break;// fraction 3/4
				case '¿':
					builder.append("&iquest;");
					break;// inverted question mark
				case '×':
					builder.append("&times;");
					break;// multiplication
				case '÷':
					builder.append("&divide;");
					break;// division
				case 'À':
					builder.append("&Agrave;");
					break;// capital a, grave accent
				case 'Á':
					builder.append("&Aacute;");
					break;// capital a, acute accent
				case 'Â':
					builder.append("&Acirc;");
					break;// capital a, circumflex accent
				case 'Ã':
					builder.append("&Atilde;");
					break;// capital a, tilde
				case 'Ä':
					builder.append("&Auml;");
					break;// capital a, umlaut mark
				case 'Å':
					builder.append("&Aring;");
					break;// capital a, ring
				case 'Æ':
					builder.append("&AElig;");
					break;// capital ae
				case 'Ç':
					builder.append("&Ccedil;");
					break;// capital c, cedilla
				case 'È':
					builder.append("&Egrave;");
					break;// capital e, grave accent
				case 'É':
					builder.append("&Eacute;");
					break;// capital e, acute accent
				case 'Ê':
					builder.append("&Ecirc;");
					break;// capital e, circumflex accent
				case 'Ë':
					builder.append("&Euml;");
					break;// capital e, umlaut mark
				case 'Ì':
					builder.append("&Igrave;");
					break;// capital i, grave accent
				case 'Í':
					builder.append("&Iacute;");
					break;// capital i, acute accent
				case 'Î':
					builder.append("&Icirc;");
					break;// capital i, circumflex accent
				case 'Ï':
					builder.append("&Iuml;");
					break;// capital i, umlaut mark
				case 'Ð':
					builder.append("&ETH;");
					break;// capital eth, Icelandic
				case 'Ñ':
					builder.append("&Ntilde;");
					break;// capital n, tilde
				case 'Ò':
					builder.append("&Ograve;");
					break;// capital o, grave accent
				case 'Ó':
					builder.append("&Oacute;");
					break;// capital o, acute accent
				case 'Ô':
					builder.append("&Ocirc;");
					break;// capital o, circumflex accent
				case 'Õ':
					builder.append("&Otilde;");
					break;// capital o, tilde
				case 'Ö':
					builder.append("&Ouml;");
					break;// capital o, umlaut mark
				case 'Ø':
					builder.append("&Oslash;");
					break;// capital o, slash
				case 'Ù':
					builder.append("&Ugrave;");
					break;// capital u, grave accent
				case 'Ú':
					builder.append("&Uacute;");
					break;// capital u, acute accent
				case 'Û':
					builder.append("&Ucirc;");
					break;// capital u, circumflex accent
				case 'Ü':
					builder.append("&Uuml;");
					break;// capital u, umlaut mark
				case 'Ý':
					builder.append("&Yacute;");
					break;// capital y, acute accent
				case 'Þ':
					builder.append("&THORN;");
					break;// capital THORN, Icelandic
				case 'ß':
					builder.append("&szlig;");
					break;// small sharp s, German
				case 'à':
					builder.append("&agrave;");
					break;// small a, grave accent
				case 'á':
					builder.append("&aacute;");
					break;// small a, acute accent
				case 'â':
					builder.append("&acirc;");
					break;// small a, circumflex accent
				case 'ã':
					builder.append("&atilde;");
					break;// small a, tilde
				case 'ä':
					builder.append("&auml;");
					break;// small a, umlaut mark
				case 'å':
					builder.append("&aring;");
					break;// small a, ring
				case 'æ':
					builder.append("&aelig;");
					break;// small ae
				case 'ç':
					builder.append("&ccedil;");
					break;// small c, cedilla
				case 'è':
					builder.append("&egrave;");
					break;// small e, grave accent
				case 'é':
					builder.append("&eacute;");
					break;// small e, acute accent
				case 'ê':
					builder.append("&ecirc;");
					break;// small e, circumflex accent
				case 'ë':
					builder.append("&euml;");
					break;// small e, umlaut mark
				case 'ì':
					builder.append("&igrave;");
					break;// small i, grave accent
				case 'í':
					builder.append("&iacute;");
					break;// small i, acute accent
				case 'î':
					builder.append("&icirc;");
					break;// small i, circumflex accent
				case 'ï':
					builder.append("&iuml;");
					break;// small i, umlaut mark
				case 'ð':
					builder.append("&eth;");
					break;// small eth, Icelandic
				case 'ñ':
					builder.append("&ntilde;");
					break;// small n, tilde
				case 'ò':
					builder.append("&ograve;");
					break;// small o, grave accent
				case 'ó':
					builder.append("&oacute;");
					break;// small o, acute accent
				case 'ô':
					builder.append("&ocirc;");
					break;// small o, circumflex accent
				case 'õ':
					builder.append("&otilde;");
					break;// small o, tilde
				case 'ö':
					builder.append("&ouml;");
					break;// small o, umlaut mark
				case 'ø':
					builder.append("&oslash;");
					break;// small o, slash
				case 'ù':
					builder.append("&ugrave;");
					break;// small u, grave accent
				case 'ú':
					builder.append("&uacute;");
					break;// small u, acute accent
				case 'û':
					builder.append("&ucirc;");
					break;// small u, circumflex accent
				case 'ü':
					builder.append("&uuml;");
					break;// small u, umlaut mark
				case 'ý':
					builder.append("&yacute;");
					break;// small y, acute accent
				case 'þ':
					builder.append("&thorn;");
					break;// small thorn, Icelandic
				case 'ÿ':
					builder.append("&yuml;");
					break;// small y, umlaut mark
				default:
					builder.append(c);
				}
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
