package com.memoir.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.memoir.model.Memoir.Memoirs;

public class DatabaseHelper extends SQLiteOpenHelper {
	public static final String CONTENT_AUTHORITY = "com.memoir";
	public static final Uri BASE_CONTENT_URI = Uri.parse("content://"
			+ CONTENT_AUTHORITY);
	private Context context;

	public interface Tables {

		String MEMOIR = "memoir";

	}

	private static final DatabaseMigration[] MIGRATIONS = new DatabaseMigration[] {

	new DatabaseMigration() {
		@Override
		public void apply(SQLiteDatabase db) {
			String createTable = DatabaseBuilder.createTable(Tables.MEMOIR)
					.withPrimaryKey(Memoirs._ID).withIntegerColumns(Memoirs.ID)
					.withTextColumns(Memoirs.Name)
					.withTextColumns(Memoirs.TYPE)
					.withTextColumns(Memoirs.TRIP_NAME)
					.withTextColumns(Memoirs.Address)
					.withTextColumns(Memoirs.FlightFrom)
					.withTextColumns(Memoirs.FlightTo)
					.withTextColumns(Memoirs.LikeOrNot)
					.withTextColumns(Memoirs.Comment)
					.withIntegerColumns(Memoirs.Start_Date)
					.withIntegerColumns(Memoirs.End_Date).buildSQL();

			db.execSQL(createTable);
		}

		@Override
		public void revert(SQLiteDatabase db) {
			db.execSQL("DROP TABLE IF EXISTS " + Tables.MEMOIR);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + Tables.MEMOIR);

		}

	} };

	private static final String DATABASE_NAME = "memoir.db";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, MIGRATIONS.length);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		for (DatabaseMigration migration : MIGRATIONS) {
			migration.apply(db);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// deleteDatabase(context);
		// onCreate(db);

		for (int i = oldVersion; i < newVersion; i++) {
			MIGRATIONS[i].apply(db);
		}

	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// deleteDatabase(context);
		// onCreate(db);

		for (int i = oldVersion; i > newVersion; i++) {
			MIGRATIONS[i - 1].revert(db);
		}

	}

	public static void deleteDatabase(Context context) {
		context.deleteDatabase(DATABASE_NAME);
	}
}
