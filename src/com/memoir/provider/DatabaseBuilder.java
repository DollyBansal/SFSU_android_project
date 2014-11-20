package com.memoir.provider;

import java.util.ArrayList;
import java.util.List;

public class DatabaseBuilder {
	public enum ColumnType {
		TEXT, BLOB, INTEGER, REAL
	}

	public static DatabaseBuilder createTable(String tableName) {
		return new DatabaseBuilder(tableName);
	}

	private String tableName;
	private List<String> columns;

	public DatabaseBuilder(String tableName) {
		this.tableName = tableName;
		columns = new ArrayList<String>();
	}

	public DatabaseBuilder withPrimaryKey(String columName) {
		return withColumn(columName, "INTEGER PRIMARY KEY AUTOINCREMENT");
	}

	public DatabaseBuilder withTextColumns(String... columnNames) {
		return withColumns(columnNames, ColumnType.TEXT);
	}

	public DatabaseBuilder withBlobColumns(String... columnNames) {
		return withColumns(columnNames, ColumnType.BLOB);
	}

	public DatabaseBuilder withIntegerColumns(String... columnNames) {
		return withColumns(columnNames, ColumnType.INTEGER);
	}

	public DatabaseBuilder withRealColumns(String... columnNames) {
		return withColumns(columnNames, ColumnType.REAL);
	}

	public DatabaseBuilder withColumns(String[] columnNames, ColumnType type) {
		for (String column : columnNames)
			withColumn(column, type);

		return this;
	}

	public DatabaseBuilder withColumn(String columnName, ColumnType type) {
		return withColumn(columnName, type.toString());
	}

	public DatabaseBuilder withColumn(String columnName, String attributes) {
		columns.add(columnName + " " + attributes);
		return this;
	}

	public String buildSQL() {
		StringBuilder sql = new StringBuilder();

		sql.append("CREATE TABLE " + tableName + " (");

		for (String column : columns) {
			sql.append(column + ",");
		}

		// remove last ,
		sql.deleteCharAt(sql.length() - 1);

		sql.append(");");

		return sql.toString();
	}
}