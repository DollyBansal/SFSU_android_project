package com.memoir.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.memoir.R;
import com.memoir.adapter.RestaurentCursorAdapter;
import com.memoir.adapter.RestaurentCursorAdapter.RestaurentQuery;
import com.memoir.model.Restaurent.Restaurents;

public class HomeActivity extends Activity implements LoaderCallbacks<Cursor> {

	RestaurentCursorAdapter cursorAdapter;

	private static final int Restaurent_LOADER_ID = 1;

	private final ContentObserver restaurentObserver = new ContentObserver(
			new Handler()) {
		@Override
		public void onChange(boolean selfChange) {

			Loader<Object> loader = getLoaderManager().getLoader(1);
			if (loader != null) {
				loader.forceLoad();
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ActionBar actionBar = getActionBar();
		actionBar.show();

		ListView listView = (ListView) findViewById(R.id.listview_home);
		listView.setItemsCanFocus(true);

		Cursor cursor = this.getContentResolver().query(
				Restaurents.CONTENT_URI, RestaurentQuery.PROJECTION, null,
				null, null);

		cursorAdapter = new RestaurentCursorAdapter(HomeActivity.this, cursor);
		listView.setAdapter(cursorAdapter);

		getLoaderManager().initLoader(Restaurent_LOADER_ID, null, this);

		this.getContentResolver().registerContentObserver(
				Restaurents.CONTENT_URI, true, restaurentObserver);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		if (id == R.id.action_plus_add) {
			Intent intent = new Intent(HomeActivity.this, AddNewEntry.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		if (id == Restaurent_LOADER_ID) {

			return new CursorLoader(this, Restaurents.CONTENT_URI,
					RestaurentQuery.PROJECTION, null, null, null);
		}
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cur) {

		switch (loader.getId()) {
		case Restaurent_LOADER_ID:
			cursorAdapter.changeCursor(cur);
		}

	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		// cursorAdapter.changeCursor(null);

	}

	@Override
	protected void onDestroy() {
		this.getContentResolver().unregisterContentObserver(restaurentObserver);
		super.onDestroy();
	}

}
