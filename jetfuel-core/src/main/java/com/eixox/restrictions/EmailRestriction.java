package com.eixox.restrictions;

import java.util.regex.Pattern;

public class EmailRestriction implements Restriction {

	public EmailRestriction() {
	}

	public EmailRestriction(Email email) {
		// just complying to a constructor pattern.
	}

	public static final Pattern rfc2822 = Pattern
			.compile(
					"^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$");

	public static boolean isValid(String email) {
		return email != null && !email.isEmpty() && rfc2822.matcher(email).matches();
	}

	public synchronized final RestrictionResult validate(Object input) {

		String s = input == null
				? ""
				: input.toString();
		return s.isEmpty()
				? new RestrictionResult(true, "")
				: rfc2822.matcher(s).matches()
						? new RestrictionResult(true, "")
						: new RestrictionResult(false, "Não é um e-mail válido.");
	}

	@Override
	public String toString() {
		return "Email()";
	}
}
