package com.eixox.data.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.eixox.data.schema.DatabaseSchema;
import com.eixox.data.schema.DatabaseSchemaColumn;
import com.eixox.data.schema.DatabaseSchemaTable;

public class MsSqlServer extends Database {

	public MsSqlServer(String url, Properties properties) {
		super(url, properties);
	}

	@Override
	public final char getNamePrefix() {
		return '[';
	}

	@Override
	public final char getNameSuffix() {
		return ']';
	}

	@Override
	public final boolean supportsTop() {
		return true;
	}

	@Override
	public final boolean supportsOffset() {
		return false;
	}

	@Override
	public final boolean supportsLimit() {
		return false;
	}

	@Override
	public DatabaseCommand createCommand() {
		return new MsSqlServerCommand(this);
	}

	@Override
	public DatabaseSchema readSchema() {

		DatabaseCommand command = createCommand()
				.appendRaw("SELECT ")
				.appendRaw("	DB_NAME() as db_name,")
				.appendRaw("	so.object_id as table_id,")
				.appendRaw("	so.name as table_name,")
				.appendRaw("	so.type_desc as table_type,")
				.appendRaw("	so.create_date as created_at,")
				.appendRaw("	sc.name as column_name,")
				.appendRaw("	sc.column_id as column_ordinal,")
				.appendRaw("	sc.max_length,")
				.appendRaw("	sc.precision,")
				.appendRaw("	sc.scale,")
				.appendRaw("	sc.collation_name,")
				.appendRaw("	sc.is_nullable,")
				.appendRaw("	sc.is_identity,")
				.appendRaw("	sc.is_computed,")
				.appendRaw("	st.name as type_name")
				.appendRaw(" FROM")
				.appendRaw(" 	sys.objects so")
				.appendRaw(" INNER JOIN")
				.appendRaw(" 	sys.columns sc ON sc.object_id = so.object_id")
				.appendRaw(" INNER JOIN")
				.appendRaw("	sys.types st ON st.user_type_id = sc.user_type_id")
				.appendRaw(" WHERE")
				.appendRaw("	so.type_desc = 'USER_TABLE'")
				.appendRaw(" ORDER BY")
				.appendRaw("	so.name, ")
				.appendRaw("	sc.column_id ");

		return command.executeQuery(new ResultsetProcessor<DatabaseSchema>() {

			public DatabaseSchema process(ResultSet rs) throws SQLException {

				DatabaseSchema dbSchema = new DatabaseSchema(rs.getString("db_name"));

				while (rs.next()) {
					String tableName = rs.getString("table_name");
					DatabaseSchemaTable tableSchema = dbSchema.get(tableName);
					if (tableSchema == null) {
						tableSchema = new DatabaseSchemaTable(tableName);
						dbSchema.members.add(tableSchema);
					}
					DatabaseSchemaColumn colSchema = new DatabaseSchemaColumn(rs.getString("column_name"));
					colSchema.column_ordinal = rs.getInt("column_ordinal");
					colSchema.is_identity = rs.getBoolean("is_identity");
					colSchema.is_nullable = rs.getBoolean("is_nullable");
					colSchema.max_length = rs.getInt("max_length");
					colSchema.precision = rs.getInt("precision");
					colSchema.scale = rs.getInt("scale");
					tableSchema.members.add(colSchema);
				}

				return dbSchema;
			}
		});

	}
}
