package com.memoir.provider;

import android.database.sqlite.SQLiteDatabase;

interface DatabaseMigration {

	void apply(SQLiteDatabase db);

	void revert(SQLiteDatabase db);

	void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);

}
