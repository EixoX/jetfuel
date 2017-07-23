package com.eixox.data.sql;

import java.util.Properties;

public class Postgres extends Database {

	public Postgres(String url, Properties properties) {
		super(url, properties);
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public DatabaseCommand createCommand() {
		return new PostgresCommand(this);
	}
	

	@Override
	public final char getNamePrefix() {
		return '"';
	}

	@Override
	public final char getNameSuffix() {
		return '"';
	}

	@Override
	public final boolean supportsTop() {
		return false;
	}

	@Override
	public final boolean supportsOffset() {
		return true;
	}

	@Override
	public final boolean supportsLimit() {
		return true;
	}

}
