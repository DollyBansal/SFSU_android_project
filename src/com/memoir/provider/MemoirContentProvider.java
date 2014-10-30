package com.memoir.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class MemoirContentProvider extends ContentProvider {
	private static final int TRIP = 100;
	private static final int PLACE = 101;
	private static final int RESTAURENT = 102;
	private static final int HOTEL = 103;
	private static final int FLIGHT = 104;

	private static UriMatcher uriMatcher = buildUriMatcher();

	private static UriMatcher buildUriMatcher() {
		final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
		final String authority = DatabaseHelper.CONTENT_AUTHORITY;

		matcher.addURI(authority, "trip", TRIP);
		matcher.addURI(authority, "place", PLACE);
		matcher.addURI(authority, "restaurent", RESTAURENT);
		matcher.addURI(authority, "hotel", HOTEL);
		matcher.addURI(authority, "flight", FLIGHT);

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

		default:
			throw new UnsupportedOperationException("Unknown uri: " + uri);
		}
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		if (uri == DatabaseHelper.BASE_CONTENT_URI) {
			// Handle whole database deletes (e.g. when signing out)
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

		default:
			throw new UnsupportedOperationException("Unknown uri: " + uri);
		}
	}

	private void deleteDatabase() {
		// TODO: wait for content provider operations to finish, then tear down
		databaseHelper.close();
		Context context = getContext();
		DatabaseHelper.deleteDatabase(context);
		databaseHelper = new DatabaseHelper(getContext());
	}
}
