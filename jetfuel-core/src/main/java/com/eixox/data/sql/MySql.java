package com.eixox.data.sql;

import java.util.Properties;

public class MySql extends Database {

	public MySql(String url, Properties properties) {
		super(url, properties);
	}

	@Override
	public DatabaseCommand createCommand() {
		return new MySqlCommand(this);
	}
	

	@Override
	public final char getNamePrefix() {
		return '`';
	}

	@Override
	public final char getNameSuffix() {
		return '`';
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
