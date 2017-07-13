package com.eixox.data.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultsetProcessor<T> {

	public T process(ResultSet rs) throws SQLException;
}
