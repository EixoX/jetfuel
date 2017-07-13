package com.eixox;

public class OptionList extends PairList<String, String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1603437290268474978L;

	public PairList<String, String> add(Object key, String label) {
		return super.add(key.toString(), label);
	}

}
