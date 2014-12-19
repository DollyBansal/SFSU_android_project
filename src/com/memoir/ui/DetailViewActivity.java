package com.memoir.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.memoir.R;
import com.memoir.adapter.DetailViewCursorAdapter;
import com.memoir.adapter.MemoirCursorAdapter.MemoirQuery;
import com.memoir.model.Memoir.Memoirs;

public class DetailViewActivity extends Activity implements
		LoaderCallbacks<Cursor> {
	private DetailViewCursorAdapter cursorAdapter;
	private static final int LOADER_ID = 1;
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

		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int pos, long id) {

				final Dialog dialog = new Dialog(context);
				dialog.setTitle(cur.getString(MemoirQuery.NAME));
				// dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.edit_dialog_box);

				TextView edit = (TextView) dialog
						.findViewById(R.id.dialog_edit);
				TextView delete = (TextView) dialog
						.findViewById(R.id.dialog_delete);

				edit.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {

						dialog.dismiss();

						String type = cur.getString(MemoirQuery.TYPE);
						int s_id = cur.getInt(MemoirQuery._ID);

						Intent intent = null;
						if (type.equals(getResources().getString(
								R.string.restaurent))) {
							intent = new Intent(DetailViewActivity.this,
									AddRestaurant.class);

						} else if (type.equals(DetailViewActivity.this
								.getResources().getString(R.string.trip))) {
							intent = new Intent(DetailViewActivity.this,
									AddTrip.class);

						} else if (type.equals(DetailViewActivity.this
								.getResources().getString(R.string.place))) {
							intent = new Intent(DetailViewActivity.this,
									AddPlace.class);

						} else if (type.equals(DetailViewActivity.this
								.getResources().getString(R.string.hotel))) {
							intent = new Intent(DetailViewActivity.this,
									AddHotel.class);

						} else if (type.equals(DetailViewActivity.this
								.getResources().getString(R.string.flight))) {
							intent = new Intent(DetailViewActivity.this,
									AddFlight.class);

						}

						Bundle mBundle = new Bundle();
						mBundle.putInt("idd", s_id);
						intent.putExtras(mBundle);
						startActivity(intent);

					}
				});

				delete.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {

						Builder alert = new AlertDialog.Builder(
								DetailViewActivity.this);
						alert.setMessage("Are you sure you want to delete");

						alert.setPositiveButton("Ok",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {

										int s_id = cur.getInt(MemoirQuery._ID);
										getContentResolver().delete(
												Memoirs.CONTENT_URI,
												Memoirs.BY_ID,
												new String[] { String
														.valueOf(s_id) });

									}
								});
						alert.show();
						dialog.dismiss();
					}
				});
				dialog.show();
				return true;
			}
		});
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		cursorAdapter.changeCursor(null);

	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(DetailViewActivity.this, HomeActivity.class);
		startActivity(intent);
	}

}