package com.memoir.provider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.memoir.utils.DatabaseUtils;

public class SelectionBuilder {
	// private static final String TAG = makeLogTag(SelectionBuilder.class);

	private static String prefix(String tableName, String column) {
		return DatabaseUtils.prefix(tableName, column);
	}

	private String mTable = null;
	private final Map<String, String> mProjectionMap = new HashMap<String, String>();
	private final StringBuilder mSelection = new StringBuilder();
	private final ArrayList<String> mSelectionArgs = new ArrayList<String>();
	private String mGroupBy = null;

	/**
	 * Reset any internal state, allowing this builder to be recycled.
	 */
	public SelectionBuilder reset() {
		mTable = null;
		mSelection.setLength(0);
		mSelectionArgs.clear();
		return this;
	}

	/**
	 * Append the given selection clause to the internal state. Each clause is
	 * surrounded with parenthesis and combined using {@code AND}.
	 */
	public SelectionBuilder where(String selection, String... selectionArgs) {
		if (TextUtils.isEmpty(selection)) {
			if (selectionArgs != null && selectionArgs.length > 0) {
				throw new IllegalArgumentException(
						"Valid selection required when including arguments=");
			}

			// Shortcut when clause is empty
			return this;
		}

		if (mSelection.length() > 0) {
			mSelection.append(" AND ");
		}

		mSelection.append("(").append(selection).append(")");
		if (selectionArgs != null) {
			Collections.addAll(mSelectionArgs, selectionArgs);
		}

		return this;
	}

	public SelectionBuilder join(String joinType, String leftTableName,
			String leftColumn, String rightTableName, String rightColumn) {
		mTable = leftTableName + " " + joinType + " JOIN " + rightTableName
				+ " ON " + prefix(leftTableName, leftColumn) + " = "
				+ prefix(rightTableName, rightColumn);

		return this;
	}

	public SelectionBuilder table(String table) {
		mTable = table;
		return this;
	}

	private void assertTable() {
		if (mTable == null) {
			throw new IllegalStateException("Table not specified");
		}
	}

	public SelectionBuilder mapToTable(String column, String table) {
		mProjectionMap.put(column, table + "." + column);
		return this;
	}

	public SelectionBuilder map(String fromColumn, String toClause) {
		mProjectionMap.put(fromColumn, toClause + " AS " + fromColumn);
		return this;
	}

	/**
	 * Return selection string for current internal state.
	 * 
	 * @see #getSelectionArgs()
	 */
	public String getSelection() {
		return mSelection.toString();
	}

	/**
	 * Return selection arguments for current internal state.
	 * 
	 * @see #getSelection()
	 */
	public String[] getSelectionArgs() {
		return mSelectionArgs.toArray(new String[mSelectionArgs.size()]);
	}

	private void mapColumns(String[] columns) {
		for (int i = 0; i < columns.length; i++) {
			final String target = mProjectionMap.get(columns[i]);
			if (target != null) {
				columns[i] = target;
			}
		}
	}

	@Override
	public String toString() {
		return "SelectionBuilder[table=" + mTable + ", selection="
				+ getSelection() + ", selectionArgs="
				+ Arrays.toString(getSelectionArgs()) + "]";
	}

	/**
	 * Execute query using the current internal state as {@code WHERE} clause.
	 */
	public Cursor query(SQLiteDatabase db, String[] columns, String orderBy) {
		return query(db, columns, null, orderBy, null);
	}

	/**
	 * Execute query using the current internal state as {@code WHERE} clause.
	 */
	public Cursor query(SQLiteDatabase db, String[] columns, String having,
			String orderBy, String limit) {
		assertTable();
		if (columns != null)
			mapColumns(columns);
		// Log.v(TAG, "query(columns=" + Arrays.toString(columns) + ") " +
		// this);
		return db.query(mTable, columns, getSelection(), getSelectionArgs(),
				mGroupBy, having, orderBy, limit);
	}

	public SelectionBuilder groupBy(String groupBy) {
		this.mGroupBy = groupBy;
		return this;
	}

	/**
	 * Execute update using the current internal state as {@code WHERE} clause.
	 */
	public int update(SQLiteDatabase db, ContentValues values) {
		assertTable();
		// Log.v(TAG, "update() " + this);
		return db.update(mTable, values, getSelection(), getSelectionArgs());
	}

	/**
	 * Execute delete using the current internal state as {@code WHERE} clause.
	 */
	public int delete(SQLiteDatabase db) {
		assertTable();
		// Log.v(TAG, "delete() " + this);
		return db.delete(mTable, getSelection(), getSelectionArgs());
	}
}
