package com.eixox.restrictions;

import java.util.regex.Pattern;

public class RegexRestriction implements Restriction {

	public final Pattern pattern;

	public RegexRestriction(String regex) {
		this.pattern = Pattern.compile(regex);
	}

	public RegexRestriction(String regex, int flags) {
		this.pattern = Pattern.compile(regex, flags);
	}

	public RegexRestriction(Regex regex) {
		this(regex.pattern());
	}

	public synchronized final RestrictionResult validate(Object input) {
		String s = input == null
				? ""
				: input.toString();
		return s.isEmpty()
				? new RestrictionResult(true, "")
				: pattern.matcher(s).matches()
						? new RestrictionResult(true, "")
						: new RestrictionResult(false, "Não obedece à expressão regular");
	}

	@Override
	public String toString() {
		return "Regex(" + this.pattern.toString() + ")";
	}
}
