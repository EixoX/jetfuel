package com.eixox.restrictions;

public class WordRestriction implements Restriction {

	public WordRestriction() {
	}

	public WordRestriction(Word name) {
		// just complying to a constructor pattern;
	}

	public static final boolean isValid(Object obj) {
		if (obj == null)
			return true;

		String str = obj.toString();

		if (str.isEmpty())
			return true;

		int l = str.length();
		for (int i = 0; i < l; i++) {
			char c = str.charAt(i);
			if (!Character.isLetterOrDigit(c))
				if (c != ' ' && c != '\'' && c != '-')
					return false;
		}

		return true;
	}

	public synchronized final RestrictionResult validate(Object input) {
		return isValid(input)
				? new RestrictionResult(true, "")
				: new RestrictionResult(false, "Não é um nome válido.");
	}
	
	@Override
	public String toString() {
		return "Word()";
	}

}
