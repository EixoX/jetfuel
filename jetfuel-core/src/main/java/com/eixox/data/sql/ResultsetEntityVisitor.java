package com.eixox.data.sql;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.eixox.Visitor;
import com.eixox.data.DataAspect;

public class ResultsetEntityVisitor<T> implements ResultsetProcessor<Void> {

	public final DataAspect<T, ?> aspect;
	public final int offset;
	public final int length;
	public final Visitor<T> visitor;

	public ResultsetEntityVisitor(DataAspect<T, ?> aspect, Visitor<T> visitor) {
		this(aspect, 0, 0, visitor);
	}

	public ResultsetEntityVisitor(DataAspect<T, ?> aspect, int offset, int length, Visitor<T> visitor) {
		this.aspect = aspect;
		this.offset = offset;
		this.length = length <= 0
				? Integer.MAX_VALUE
				: length;
		this.visitor = visitor;
	}

	public Void process(ResultSet rs) throws SQLException {

		int row = 0;

		// skip offset
		while (row < offset && rs.next())
			row++;

		// checks if an empty list should be returned
		if (row < offset)
			return null;

		// map columns
		ResultSetMetaData meta = rs.getMetaData();
		int[] ordinals = new int[meta.getColumnCount()];
		for (int i = 0; i < ordinals.length; i++)
			ordinals[i] = aspect.indexOfColumnName(meta.getColumnLabel(i + 1));

		// create list
		for (int i = 0; i < length && rs.next(); i++) {
			T entity = aspect.newInstance();
			for (int j = 0; j < ordinals.length; j++) {
				if (ordinals[j] >= 0) {
					Object value = rs.getObject(j + 1);
					if (value != null)
						aspect.setValue(entity, ordinals[j], value);
				}
			}
			visitor.visit(entity);
		}

		return null;

	}

}
