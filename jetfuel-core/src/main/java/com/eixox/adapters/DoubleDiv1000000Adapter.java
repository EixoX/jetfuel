package com.eixox.adapters;

public class DoubleDiv1000000Adapter extends DoubleAdapter {

	public Double parse(String source) {
		return source == null || source.isEmpty()
				? 0.0
				: Double.parseDouble(source) / 1000000.0;
	}

	public String format(Double source) {
		return Long.toString((long) Math.round(source * 1000000.0));
	}

}
