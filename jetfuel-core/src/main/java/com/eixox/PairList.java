package com.eixox;

import java.util.ArrayList;

public class PairList<T, G> extends ArrayList<Pair<T, G>> {

	private static final long serialVersionUID = 5642972850101617246L;

	public PairList() {
	}

	public PairList(int capacity) {
		super(capacity);
	}

	public final synchronized PairList<T, G> add(T key, G label) {
		this.add(new Pair<T, G>(key, label));
		return this;
	}

	public final synchronized int indexOfKey(Object key) {
		int s = size();
		for (int i = 0; i < s; i++)
			if (key.equals(get(i).key))
				return i;
		return -1;
	}

	public final synchronized G getLabel(Object key) {
		int ordinal = indexOfKey(key);
		return ordinal >= 0
				? get(ordinal).label
				: null;
	}

}
