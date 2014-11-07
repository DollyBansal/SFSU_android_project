package com.memoir.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.memoir.model.Hotel.Hotels;
import com.memoir.model.Place.Places;
import com.memoir.model.Restaurent.Restaurents;
import com.memoir.model.Trip.Trips;

public class DatabaseHelper extends SQLiteOpenHelper {
	public static final String CONTENT_AUTHORITY = "com.memoir";
	public static final Uri BASE_CONTENT_URI = Uri.parse("content://"
			+ CONTENT_AUTHORITY);
	private Context context;

	public interface Tables {
		String TRIP = "trip";
		String PLACE = "place";
		String RESTAURENT = "restaurent";
		String HOTEL = "hotel";
		String FLIGHT = "flight";

	}

	private static final DatabaseMigration[] MIGRATIONS = new DatabaseMigration[] {

	new DatabaseMigration() {
		@Override
		public void apply(SQLiteDatabase db) {
			String createTripTable = DatabaseBuilder.createTable(Tables.TRIP)
					.withPrimaryKey(Trips._ID).withIntegerColumns(Trips.ID)
					.withTextColumns(Trips.Name).withTextColumns(Trips.Adress)
					.withTextColumns(Trips.LikeOrNot)
					.withTextColumns(Trips.Comment)
					.withIntegerColumns(Trips.Date).buildSQL();

			db.execSQL(createTripTable);
		}

		@Override
		public void revert(SQLiteDatabase db) {
			db.execSQL("DROP TABLE IF EXISTS " + Tables.TRIP);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + Tables.TRIP);

		}

	}, new DatabaseMigration() {
		@Override
		public void apply(SQLiteDatabase db) {
			String createPlaceTable = DatabaseBuilder.createTable(Tables.PLACE)
					.withPrimaryKey(Places._ID).withIntegerColumns(Places.ID)
					.withTextColumns(Places.Name)
					.withTextColumns(Places.Adress)
					.withTextColumns(Places.LikeOrNot)
					.withTextColumns(Places.Comment)
					.withIntegerColumns(Places.Date).buildSQL();

			db.execSQL(createPlaceTable);
		}

		@Override
		public void revert(SQLiteDatabase db) {
			db.execSQL("DROP TABLE IF EXISTS " + Tables.PLACE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + Tables.PLACE);

		}

	}, new DatabaseMigration() {
		@Override
		public void apply(SQLiteDatabase db) {
			String createRestaurentTable = DatabaseBuilder
					.createTable(Tables.RESTAURENT)
					.withPrimaryKey(Restaurents._ID)
					.withIntegerColumns(Restaurents.ID)
					.withTextColumns(Restaurents.Name)
					.withTextColumns(Restaurents.Adress)
					.withTextColumns(Restaurents.LikeOrNot)
					.withTextColumns(Restaurents.Comment)
					.withIntegerColumns(Restaurents.Date).buildSQL();

			db.execSQL(createRestaurentTable);
		}

		@Override
		public void revert(SQLiteDatabase db) {
			db.execSQL("DROP TABLE IF EXISTS " + Tables.RESTAURENT);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + Tables.RESTAURENT);

		}

	}, new DatabaseMigration() {
		@Override
		public void apply(SQLiteDatabase db) {
			String createHotelTable = DatabaseBuilder.createTable(Tables.HOTEL)
					.withPrimaryKey(Hotels._ID).withIntegerColumns(Hotels.ID)
					.withTextColumns(Hotels.Name)
					.withTextColumns(Hotels.Adress)
					.withTextColumns(Hotels.LikeOrNot)
					.withTextColumns(Hotels.Comment)
					.withIntegerColumns(Hotels.Date).buildSQL();

			db.execSQL(createHotelTable);
		}

		@Override
		public void revert(SQLiteDatabase db) {
			db.execSQL("DROP TABLE IF EXISTS " + Tables.HOTEL);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + Tables.HOTEL);

		}
	}

	};

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
