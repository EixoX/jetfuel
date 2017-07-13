package com.eixox.adapters;

import java.util.UUID;

public class UUIDBase36Adapter extends UUIDAdapter {

	@Override
	public UUID parse(String source) {
		if (source == null || source.isEmpty())
			return null;

		throw new RuntimeException("Not implemented");
	}

	@Override
	public String format(UUID source) {
		if (source == null)
			return "";

		long hi = source.getMostSignificantBits();
		long lo = source.getLeastSignificantBits();

		return Long.toString(hi, 36) + Long.toString(lo, 36);

	}

}
