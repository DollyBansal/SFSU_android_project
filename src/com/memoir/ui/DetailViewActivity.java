package com.memoir.ui;

import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;

import com.memoir.R;
import com.memoir.adapter.DetailViewCursorAdapter;
import com.memoir.adapter.MemoirCursorAdapter.MemoirQuery;
import com.memoir.model.Memoir.Memoirs;

public class DetailViewActivity extends Activity implements
		LoaderCallbacks<Cursor> {
	DetailViewCursorAdapter cursorAdapter;
	private static final int LOADER_ID = 1;
	ListView listView;

	private final ContentObserver Observer = new ContentObserver(new Handler()) {
		@Override
		public void onChange(boolean selfChange) {

			Loader<Object> loader = getLoaderManager().getLoader(1);
			if (loader != null) {
				loader.forceLoad();
			}

		}
	};

	private Context context = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_view);

		listView = (ListView) findViewById(R.id.listview_detailview);
		listView.setItemsCanFocus(true);

		cursorAdapter = new DetailViewCursorAdapter(DetailViewActivity.this);

		listView.setAdapter(cursorAdapter);

		getLoaderManager().initLoader(LOADER_ID, null, this);

		this.getContentResolver().registerContentObserver(Memoirs.CONTENT_URI,
				true, Observer);

	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
		Bundle bundle1 = getIntent().getExtras();
		int datas = bundle1.getInt("id");
		return new CursorLoader(this, Memoirs.CONTENT_URI,
				MemoirQuery.PROJECTION, Memoirs.BY_ID,
				new String[] { String.valueOf(datas) }, MemoirQuery.STARTDATE
						+ " DESC");
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, final Cursor cur) {
		cursorAdapter.changeCursor(cur);
		/*
		 * listView.setOnItemClickListener(new OnItemClickListener() {
		 * 
		 * @Override public void onItemClick(AdapterView<?> parent, View view,
		 * int position, long id) {
		 * 
		 * int s_id = cur.getInt(MemoirQuery._ID);
		 * 
		 * Intent intent = new Intent(DetailViewActivity.this,
		 * DetailTripViewActivity.class); Bundle mBundle = new Bundle();
		 * mBundle.putInt("id", s_id); intent.putExtras(mBundle);
		 * startActivity(intent); }
		 * 
		 * }
		 * 
		 * );
		 */

	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		cursorAdapter.changeCursor(null);

	}

	/*
	 * @Override public void onBackPressed() { Intent intent = new
	 * Intent(DetailViewActivity.this, HomeActivity.class);
	 * startActivity(intent); }
	 */

}