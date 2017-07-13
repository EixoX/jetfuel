package com.eixox.adapters;

public class EnumAdapter<T extends Enum<?>> extends Adapter<T> {

	public EnumAdapter(Class<T> dataType) {
		super(dataType);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public T parse(String source) {
		return (T) (source == null || source.isEmpty() ? null : Enum.valueOf((Class<? extends Enum>) this.dataType, source));
	}

}
