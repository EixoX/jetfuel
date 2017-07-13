package com.eixox.data.sql;

public class MySqlCommand extends DatabaseCommand {

	public MySqlCommand(Database database) {
		super(database);
	}


	@Override
	public DatabaseCommand appendString(String value) {
		text.append('\'');
		int stringLength = value.length();
		for (int i = 0; i < stringLength; ++i) {
			char c = value.charAt(i);
			switch (c) {
			case 0: /* Must be escaped for 'mysql' */
				text.append('\\');
				text.append('0');
				break;
			case '\n': /* Must be escaped for logs */
				text.append('\\');
				text.append('n');
				break;
			case '\r':
				text.append('\\');
				text.append('r');
				break;
			case '\\':
				text.append('\\');
				text.append('\\');
				break;
			case '\'':
				text.append('\'');
				text.append('\'');
				break;
			case '\032': /* This gives problems on Win32 */
				text.append('\\');
				text.append('Z');
				break;
			case '\u00a5':
			case '\u20a9':
				// escape characters interpreted as backslash by mysql
				// fall through
				break;
			default:
				text.append(c);
			}
		}
		text.append('\'');
		return this;
	}

}
