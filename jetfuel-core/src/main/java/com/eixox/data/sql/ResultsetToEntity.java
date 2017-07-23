package com.eixox.data.sql;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.eixox.data.DataAspect;

public class ResultsetToEntity<T> implements ResultsetProcessor<T> {

	public final DataAspect<T, ?> aspect;
	public final int offset;

	public ResultsetToEntity(DataAspect<T, ?> aspect) {
		this(aspect, 0);
	}

	public ResultsetToEntity(DataAspect<T, ?> aspect, int offset) {
		this.aspect = aspect;
		this.offset = offset;
	}

	public T process(ResultSet rs) throws SQLException {

		int row = 0;

		// skip offset
		while (row < offset && rs.next())
			row++;

		// checks if an empty element should be returned
		if (!rs.next())
			return null;

		// map columns
		ResultSetMetaData meta = rs.getMetaData();
		int[] ordinals = new int[meta.getColumnCount()];
		for (int i = 0; i < ordinals.length; i++)
			ordinals[i] = aspect.indexOfColumnName(meta.getColumnLabel(i + 1));

		// create entity
		T entity = aspect.newInstance();
		for (int j = 0; j < ordinals.length; j++) {
			if (ordinals[j] >= 0) {
				Object value = rs.getObject(j + 1);
				if (value != null)
					aspect.setValue(entity, ordinals[j], value);
			}
		}
		return entity;

	}

}
