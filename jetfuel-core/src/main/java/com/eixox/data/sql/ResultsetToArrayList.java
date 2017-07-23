package com.eixox.data.sql;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import com.eixox.data.DataAspect;

public class ResultsetToArrayList<T> implements ResultsetProcessor<ArrayList<T>> {

	public final DataAspect<T, ?> aspect;
	public final int offset;
	public final int length;

	public ResultsetToArrayList(DataAspect<T, ?> aspect) {
		this(aspect, 0, 0);
	}

	public ResultsetToArrayList(DataAspect<T, ?> aspect, int offset, int length) {
		this.aspect = aspect;
		this.offset = offset;
		this.length = length <= 0 ? Integer.MAX_VALUE : length;
	}

	public ArrayList<T> process(ResultSet rs) throws SQLException {

		int row = 0;

		// skip offset
		while (row < offset && rs.next())
			row++;

		// checks if an empty list should be returned
		if (row < offset)
			return new ArrayList<T>();

		// map columns
		ResultSetMetaData meta = rs.getMetaData();
		int[] ordinals = new int[meta.getColumnCount()];
		for (int i = 0; i < ordinals.length; i++)
			ordinals[i] = aspect.indexOfColumnName(meta.getColumnLabel(i + 1));

		// create list
		ArrayList<T> list = new ArrayList<T>(length > 100 ? 100 : length);
		for (int i = 0; i < length && rs.next(); i++) {
			T entity = aspect.newInstance();
			for (int j = 0; j < ordinals.length; j++) {
				if (ordinals[j] >= 0) {
					Object value = rs.getObject(j + 1);
					if (value != null)
						aspect.setValue(entity, ordinals[j], value);
				}
			}
			list.add(entity);
		}
		return list;

	}

}
