package com.memoir.fragment;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.memoir.R;
import com.memoir.adapter.MemoirCursorAdapter;
import com.memoir.adapter.MemoirCursorAdapter.MemoirQuery;
import com.memoir.model.Memoir.Memoirs;
import com.memoir.ui.DetailTripViewActivity;
import com.memoir.ui.DetailViewActivity;

public class TripFragment extends Fragment implements LoaderCallbacks<Cursor> {

	private MemoirCursorAdapter cursorAdapter;
	private Cursor curs;
	private ListView listView;
	// private String trip_name;

	private static final int LOADER_ID = 1;

	private final ContentObserver Observer = new ContentObserver(new Handler()) {
		@Override
		public void onChange(boolean selfChange) {

			android.support.v4.content.Loader<Object> loader = getLoaderManager()
					.getLoader(1);
			if (loader != null) {
				loader.forceLoad();
			}

		}
	};

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		if (id == LOADER_ID) {

			return new CursorLoader(getActivity(), Memoirs.CONTENT_URI,
					MemoirQuery.PROJECTION, null, null, null);
		}
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, final Cursor cur) {
		cursorAdapter.changeCursor(cur);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// if (cur.moveToFirst()) {

				String trip_name = cur.getString(MemoirQuery.TYPE);

				// }
				int s_id = cur.getInt(MemoirQuery._ID);
				if (trip_name.equals(trip_name)) {
					Intent intent = new Intent(getActivity(),
							DetailTripViewActivity.class);
					Bundle mBundle = new Bundle();
					mBundle.putInt("id", s_id);
					intent.putExtras(mBundle);
					startActivity(intent);
				} else {
					Intent intent = new Intent(getActivity(),
							DetailViewActivity.class);
					Bundle mBundle = new Bundle();
					mBundle.putInt("id", s_id);
					intent.putExtras(mBundle);
					startActivity(intent);
				}

			}

		});

		switch (loader.getId()) {
		case LOADER_ID:
			cursorAdapter.changeCursor(cur);
		}

	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		cursorAdapter.changeCursor(null);

	}

	@Override
	public void onDestroy() {
		getActivity().getContentResolver().unregisterContentObserver(Observer);
		super.onDestroy();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_main, container,
				false);

		return rootView;
	}

	@Override
	public void onStart() {
		super.onStart();
		initialize();
	}

	private void initialize() {
		listView = (ListView) getView().findViewById(R.id.listview_home);
		listView.setItemsCanFocus(true);
		cursorAdapter = new MemoirCursorAdapter(getActivity());
		// curs = getActivity().getContentResolver().query(Memoirs.CONTENT_URI,
		// MemoirQuery.PROJECTION, stringSelection(), stringArgument(),
		// getOrderBy());

		// cursorAdapter = new MemoirCursorAdapter(getActivity(), curs);

		listView.setAdapter(cursorAdapter);

		getActivity().getLoaderManager().initLoader(LOADER_ID, null, this);

		getActivity().getContentResolver().registerContentObserver(
				Memoirs.CONTENT_URI, true, Observer);

	}
}
