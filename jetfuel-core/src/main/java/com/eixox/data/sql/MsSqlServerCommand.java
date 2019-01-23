package com.eixox.data.sql;

public class MsSqlServerCommand extends DatabaseCommand {

	public MsSqlServerCommand(Database database) {
		super(database);
	}

	@Override
	public DatabaseCommand appendBoolean(Boolean value) {
		return value
				? appendNumber(1)
				: appendNumber(0);
	}

	@Override
	public DatabaseCommand appendByteArray(byte[] value) {
		char[] hexArray = "0123456789ABCDEF".toCharArray();
		super.appendRaw("0x");
		char[] hexChars = new char[value.length * 2];
		for (int j = 0; j < value.length; j++) {
			int v = value[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		super.text.append(hexChars);
		return this;

	}

}
