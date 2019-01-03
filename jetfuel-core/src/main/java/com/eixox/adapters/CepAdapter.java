package com.eixox.adapters;

public class CepAdapter extends IntegerAdapter {

	@Override
	public Integer parse(String source) {
		if (source == null || source.isEmpty())
			return 0;
		else
			return Integer.parseInt(source);
	}

	@Override
	public String format(Integer source) {
		if (source == null)
			return "";
		else
			return formatCep(source);
	}

	public static final String formatCep(int source) {
		return String.format("%5d", source / 1000) + "-" + String.format("%3d", source % 1000);
	}

}
