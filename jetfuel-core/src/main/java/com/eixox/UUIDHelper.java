package com.eixox;

import java.security.SecureRandom;
import java.util.Calendar;
import java.util.UUID;

public final class UUIDHelper {

	public static final SecureRandom RANDOM = new SecureRandom();

	private UUIDHelper() {

	}

	public static final synchronized UUID generateTimebased() {
		long hi = Calendar.getInstance().getTimeInMillis();
		long lo = RANDOM.nextLong();
		return new UUID(hi, lo);

	}
}
