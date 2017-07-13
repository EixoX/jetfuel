package com.eixox.data.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.eixox.data.Column;

public class PostgresCommand extends DatabaseCommand {

	public PostgresCommand(Database database) {
		super(database);
	}

	@Override
	protected DatabaseCommand appendLike(String name, Object value) {
		appendName(name);
		text.append(" ILIKE ");
		appendValue(value);
		return this;
	}

	@Override
	protected DatabaseCommand appendNotLike(String name, Object value) {
		appendName(name);
		text.append(" NOT ILIKE ");
		appendValue(value);
		return this;
	}

	@Override
	public Object executeInsertAndScopeIdentity(Connection conn, final Column identity) {
		try {
			return executeInsert(conn, new ResultsetProcessor<Object>() {
				public Object process(ResultSet rs) throws SQLException {
					return rs.next()
							? rs.getObject(identity.getColumnName())
							: null;
				}
			});
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
