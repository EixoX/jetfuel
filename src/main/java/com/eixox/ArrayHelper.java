package com.eixox;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * A generic array helper than builds, copies and splices arrays.
 * 
 * @author Rodrigo Portela
 *
 */
public class ArrayHelper {

	@SuppressWarnings("unchecked")
	public static synchronized final <T> ArrayList<T[]> splice(T[] source, int... positions) {

		ArrayList<T[]> list = new ArrayList<T[]>(positions.length + 1);
		Class<?> claz = source[0].getClass();

		int prevPos = 0;
		for (int i = 0; i < positions.length; i++) {
			int curSz = positions[i] - prevPos;
			T[] arr = (T[]) Array.newInstance(claz, curSz);
			for (int j = 0; j < curSz; j++) {
				arr[j] = source[prevPos + j];
			}
			list.add(arr);
			prevPos = positions[i];
		}
		int curSz = source.length - prevPos;
		T[] arr = (T[]) Array.newInstance(claz, curSz);
		for (int j = 0; j < curSz; j++) {
			arr[j] = source[prevPos + j];
		}
		list.add(arr);
		return list;
	}

	
}
