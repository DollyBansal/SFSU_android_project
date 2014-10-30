package com.memoir.utils;

import java.util.ArrayList;
import java.util.List;

public class DatabaseUtils {

	public static String prefix(String tableName, String column) {
		if (tableName == null || column == null) {
			throw new IllegalArgumentException("Params can't be null");
		}
		StringBuffer joined = new StringBuffer();
		joined.append(tableName);
		joined.append(".");
		joined.append(column);
		return joined.toString();
	}

	public static String[] prefixProjection(String tableName, String... columns) {
		String[] prefixedProjections = new String[columns.length];

		if (tableName == null || columns == null) {
			throw new IllegalArgumentException("Params can't be null");
		}
		for (int i = 0; i < columns.length; i++) {
			prefixedProjections[i] = prefix(tableName, columns[i]);
		}

		return prefixedProjections;
	}

	public static ProjectionBuilder projectionBuilder() {
		return new ProjectionBuilder();
	}

	public static class ProjectionBuilder {

		private final List<String> projections = new ArrayList<String>();

		public ProjectionBuilder fromTable(String tableName, String... columns) {
			for (String projection : prefixProjection(tableName, columns)) {
				projections.add(projection);
			}

			return this;
		}

		public String[] build() {
			return projections.toArray(new String[0]);
		}

	}
}
