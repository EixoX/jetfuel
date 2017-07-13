package com.eixox.adapters;

public class DoubleDiv100Adapter extends DoubleAdapter {

	public Double parse(String source) {
		return source == null || source.isEmpty()
				? 0.0
				: Double.parseDouble(source) / 100.0;
	}

	public String format(Double source) {
		return Long.toString((long) Math.round(source * 100.0));
	}

}
