package com.eixox.data.sql;

public class MsSqlServerCommand extends DatabaseCommand {

	public MsSqlServerCommand(Database database) {
		super(database);
	}

	@Override
	public DatabaseCommand appendBoolean(Boolean value) {
		return value ?
				appendNumber(1) :
				appendNumber(0);
	}

}
