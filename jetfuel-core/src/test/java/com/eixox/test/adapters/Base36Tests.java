package com.eixox.test.adapters;

import java.util.UUID;

import org.junit.Test;

public class Base36Tests {

	@Test
	public void testFormats() {

		UUID uid = UUID.randomUUID();

		long hi = uid.getMostSignificantBits();
		long lo = uid.getLeastSignificantBits();

		String shi = Long.toString(hi, 36);
		String slo = Long.toString(lo, 36);

		System.out.println(shi);
		System.out.println(slo);

		System.out.println(shi + slo);

		System.out.println(Long.toString(Long.MAX_VALUE, 36));

	}

}
