package com.memoir.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.memoir.model.Flight.Flights;
import com.memoir.model.Hotel.Hotels;
import com.memoir.model.Memoir.Memoirs;
import com.memoir.model.Place.Places;
import com.memoir.model.Restaurent.Restaurents;
import com.memoir.model.Trip.Trips;

public class MemoirContentProvider extends ContentProvider {
	private static final int TRIP = 100;
	private static final int PLACE = 101;
	private static final int RESTAURENT = 102;
	private static final int HOTEL = 103;
	private static final int FLIGHT = 104;
	private static final int MEMOIR = 105;

	private static UriMatcher uriMatcher = buildUriMatcher();

	private static UriMatcher buildUriMatcher() {
		final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
		final String authority = DatabaseHelper.CONTENT_AUTHORITY;

		matcher.addURI(authority, "trip", TRIP);
		matcher.addURI(authority, "place", PLACE);
		matcher.addURI(authority, "restaurent", RESTAURENT);
		matcher.addURI(authority, "hotel", HOTEL);
		matcher.addURI(authority, "flight", FLIGHT);
		matcher.addURI(authority, "memoir", MEMOIR);

		return matcher;
	}

	private DatabaseHelper databaseHelper;

	@Override
	public boolean onCreate() {
		databaseHelper = new DatabaseHelper(getContext());
		return true;
	}

	@Override
	public String getType(Uri uri) {
		final int match = uriMatcher.match(uri);
		switch (match) {
		case TRIP:
			return Trips.CONTENT_TYPE;
		case PLACE:
			return Places.CONTENT_TYPE;
		case RESTAURENT:
			return Restaurents.CONTENT_TYPE;
		case HOTEL:
			return Hotels.CONTENT_TYPE;
		case FLIGHT:
			return Flights.CONTENT_TYPE;
		case MEMOIR:
			return Memoirs.CONTENT_TYPE;
		default:
			throw new UnsupportedOperationException("Unknown uri: " + uri);
		}
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		final SQLiteDatabase db = databaseHelper.getReadableDatabase();
		final SelectionBuilder builder = buildSelection(uri);

		Cursor cursor = builder.where(selection, selectionArgs).query(db,
				projection, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);

		return cursor;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		final SQLiteDatabase db = databaseHelper.getReadableDatabase();
		final int match = uriMatcher.match(uri);

		switch (match) {
		case TRIP:
			db.insertOrThrow(com.memoir.provider.DatabaseHelper.Tables.TRIP,
					null, values);
			getContext().getContentResolver().notifyChange(uri, null, false);
			return Trips.buildTripUri(values.getAsString(Trips.Name));
		case RESTAURENT:
			db.insertOrThrow(
					com.memoir.provider.DatabaseHelper.Tables.RESTAURENT, null,
					values);
			getContext().getContentResolver().notifyChange(uri, null, false);
			return Restaurents.buildRestaurentUri(values
					.getAsString(Restaurents.Name));
		case HOTEL:
			db.insertOrThrow(com.memoir.provider.DatabaseHelper.Tables.HOTEL,
					null, values);
			getContext().getContentResolver().notifyChange(uri, null, false);
			return Hotels.buildHotelUri(values.getAsString(Hotels.Name));
		case PLACE:
			db.insertOrThrow(com.memoir.provider.DatabaseHelper.Tables.PLACE,
					null, values);
			getContext().getContentResolver().notifyChange(uri, null, false);
			return Places.buildPlaceUri(values.getAsString(Places.Name));
		case FLIGHT:
			db.insertOrThrow(com.memoir.provider.DatabaseHelper.Tables.FLIGHT,
					null, values);
			getContext().getContentResolver().notifyChange(uri, null, false);
			return Flights.buildFlightUri(values.getAsString(Flights.Name));
		case MEMOIR:
			db.insertOrThrow(com.memoir.provider.DatabaseHelper.Tables.MEMOIR,
					null, values);
			getContext().getContentResolver().notifyChange(uri, null, false);
			return Memoirs.buildUri(values.getAsString(Memoirs.Name));
		default:
			throw new UnsupportedOperationException("Unknown uri: " + uri);
		}
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		if (uri == DatabaseHelper.BASE_CONTENT_URI) {
			deleteDatabase();
			getContext().getContentResolver().notifyChange(uri, null, false);
			return 1;
		}

		final SQLiteDatabase db = databaseHelper.getWritableDatabase();
		final SelectionBuilder builder = buildSelection(uri);
		int retVal = builder.where(selection, selectionArgs).delete(db);
		getContext().getContentResolver().notifyChange(uri, null, false);

		return retVal;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		return 0;
	}

	private SelectionBuilder buildSelection(Uri uri) {
		final SelectionBuilder builder = new SelectionBuilder();
		final int match = uriMatcher.match(uri);
		switch (match) {
		case RESTAURENT:
			return builder
					.table(com.memoir.provider.DatabaseHelper.Tables.RESTAURENT);
		case FLIGHT:
			return builder
					.table(com.memoir.provider.DatabaseHelper.Tables.FLIGHT);
		case PLACE:
			return builder
					.table(com.memoir.provider.DatabaseHelper.Tables.PLACE);
		case HOTEL:
			return builder
					.table(com.memoir.provider.DatabaseHelper.Tables.HOTEL);
		case TRIP:
			return builder
					.table(com.memoir.provider.DatabaseHelper.Tables.TRIP);
		case MEMOIR:
			return builder
					.table(com.memoir.provider.DatabaseHelper.Tables.MEMOIR);

		default:
			throw new UnsupportedOperationException("Unknown uri: " + uri);
		}
	}

	private void deleteDatabase() {
		databaseHelper.close();
		Context context = getContext();
		DatabaseHelper.deleteDatabase(context);
		databaseHelper = new DatabaseHelper(getContext());
	}
}
