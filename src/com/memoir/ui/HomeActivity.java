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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.memoir.R;
import com.memoir.adapter.MemoirCursorAdapter;
import com.memoir.adapter.MemoirCursorAdapter.MemoirQuery;
import com.memoir.model.Memoir.Memoirs;

public class HomeActivity extends Activity implements LoaderCallbacks<Cursor> {

	private MemoirCursorAdapter cursorAdapter;
	private ListView listView;

	private static final int LOADER_ID = 1;

	private final ContentObserver Observer = new ContentObserver(new Handler()) {
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

		listView = (ListView) findViewById(R.id.listview_home);
		listView.setItemsCanFocus(true);

		Cursor cursor = this.getContentResolver().query(Memoirs.CONTENT_URI,
				MemoirQuery.PROJECTION, null, null,
				MemoirQuery.STARTDATE + " DESC");

		cursorAdapter = new MemoirCursorAdapter(HomeActivity.this, cursor);
		cursorAdapter = new MemoirCursorAdapter(HomeActivity.this);
		listView.setAdapter(cursorAdapter);

		getLoaderManager().initLoader(LOADER_ID, null, this);

		this.getContentResolver().registerContentObserver(Memoirs.CONTENT_URI,
				true, Observer);

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
		if (id == LOADER_ID) {

			return new CursorLoader(this, Memoirs.CONTENT_URI,
					MemoirQuery.PROJECTION, null, null, MemoirQuery.STARTDATE
							+ " DESC");
		}
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, final Cursor cur) {

		switch (loader.getId()) {
		case LOADER_ID:
			cursorAdapter.changeCursor(cur);
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					String trip_type = cur.getString(MemoirQuery.TYPE);

					int s_id = cur.getInt(MemoirQuery._ID);
					String name = cur.getString(MemoirQuery.NAME);
					if (trip_type.equals("TRIP")) {
						Intent intent = new Intent(HomeActivity.this,
								DetailTripViewActivity.class);
						Bundle mBundle = new Bundle();
						mBundle.putInt("id", s_id);
						mBundle.putString("name", name);
						intent.putExtras(mBundle);
						startActivity(intent);
					} else {
						Intent intent = new Intent(HomeActivity.this,
								DetailViewActivity.class);
						Bundle mBundle = new Bundle();
						mBundle.putInt("id", s_id);
						intent.putExtras(mBundle);
						startActivity(intent);
					}

				}

			});

		}

	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		cursorAdapter.changeCursor(null);

	}

	@Override
	protected void onDestroy() {
		this.getContentResolver().unregisterContentObserver(Observer);
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		Log.d("CDA", "onBackPressed Called");
		Intent setIntent = new Intent(Intent.ACTION_MAIN);
		setIntent.addCategory(Intent.CATEGORY_HOME);
		setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(setIntent);
	}

}