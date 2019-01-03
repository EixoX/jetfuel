package com.eixox.adapters;

public class DoubleDiv10000000Adapter extends DoubleAdapter {

	@Override
	public Double parse(String source) {
		return source == null || source.isEmpty()
				? 0.0
				: Double.parseDouble(source) / 10000000.0;
	}

	@Override
	public String format(Double source) {
		return Long.toString((long) Math.round(source * 10000000.0));
	}

}
