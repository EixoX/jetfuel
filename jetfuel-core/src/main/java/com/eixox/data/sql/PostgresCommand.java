package com.eixox.data.sql;

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

}
