package com.memoir.ui;

import java.util.Date;

import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.memoir.R;
import com.memoir.adapter.DetailViewCursorAdapter;
import com.memoir.adapter.MemoirCursorAdapter.MemoirQuery;
import com.memoir.model.Memoir.Memoirs;
import com.memoir.utils.DateConversion;

public class DetailTripViewActivity extends Activity implements
		LoaderCallbacks<Cursor> {
	DetailViewCursorAdapter cursorAdapter;
	private static final int ListView_LOADER_ID = 2;
	TextView name, start_date, end_date, likeOrNot, comment;
	String s_name, s_start_date, s_end_date, s_comment, s_likeOrNot;
	String s_id;
	private ListView listView;
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
		setContentView(R.layout.detail_trip_view);
		DateConversion dateConversion = new DateConversion();
		Bundle bundle1 = getIntent().getExtras();
		int datas = bundle1.getInt("id");
		String name1 = bundle1.getString("name");
		Cursor curs = this.getContentResolver().query(Memoirs.CONTENT_URI,
				MemoirQuery.PROJECTION, Memoirs.BY_ID,
				new String[] { String.valueOf(datas) }, null);

		listView = (ListView) findViewById(R.id.listview_detailview_trip);
		listView.setItemsCanFocus(true);
		LayoutInflater inflater = getLayoutInflater();
		ViewGroup header = (ViewGroup) inflater.inflate(
				R.layout.list_view_header, listView, false);
		listView.addHeaderView(header, null, false);

		cursorAdapter = new DetailViewCursorAdapter(DetailTripViewActivity.this);

		listView.setAdapter(cursorAdapter);

		name = (TextView) header.findViewById(R.id.name_detail_trip);
		curs.moveToFirst();
		s_name = curs.getString(MemoirQuery.NAME);
		name.setText(s_name);

		start_date = (TextView) header
				.findViewById(R.id.start_date_detail_trip);
		Date startDate = new Date(curs.getLong(MemoirQuery.STARTDATE));
		s_start_date = dateConversion.dateToString(startDate);
		start_date.setText(s_start_date);

		end_date = (TextView) header.findViewById(R.id.end_date_detail_trip);
		Date endDate = new Date(curs.getLong(MemoirQuery.ENDDATE));
		s_end_date = dateConversion.dateToString(endDate);
		end_date.setText(s_end_date);

		likeOrNot = (TextView) header.findViewById(R.id.like_detail_trip);
		s_likeOrNot = curs.getString(MemoirQuery.LIKE);
		likeOrNot.setText(s_likeOrNot);

		comment = (TextView) header.findViewById(R.id.comment_detail_trip);
		s_comment = curs.getString(MemoirQuery.COMMENT);
		comment.setText(s_comment);

		getLoaderManager().initLoader(ListView_LOADER_ID, null, this);

		this.getContentResolver().registerContentObserver(Memoirs.CONTENT_URI,
				true, Observer);

	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
		Bundle bundle1 = getIntent().getExtras();
		int datas = bundle1.getInt("id");
		String name1 = bundle1.getString("name");
		switch (id) {

		case ListView_LOADER_ID:
			return new CursorLoader(this, Memoirs.CONTENT_URI,
					MemoirQuery.PROJECTION, Memoirs.BY_Trip_Name,
					new String[] { String.valueOf(name1) }, null);
		default:
			return null;
		}

	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, final Cursor cursor) {
		switch (loader.getId()) {

		case ListView_LOADER_ID:
			cursorAdapter.changeCursor(cursor);
			break;
		}

	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		cursorAdapter.changeCursor(null);

	}

	/*
	 * @Override public void onBackPressed() { Intent intent = new
	 * Intent(DetailTripViewActivity.this, HomeActivity.class);
	 * startActivity(intent); }
	 */
}
