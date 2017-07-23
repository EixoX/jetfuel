package com.eixox.test.adapters;

import java.util.UUID;

import org.junit.Test;

import com.eixox.UUIDHelper;

public class UUIDTests {

	@Test
	public void testTimebased() {

		for (int i = 0; i < 1000; i++) {
			/*long hi = 0L;
			long lo = new Date().getTime();
			UUID uid = new UUID(lo, hi);
			*/
			UUID uid = UUIDHelper.generateTimebased();
			System.out.println(uid);
		}
	}
}
