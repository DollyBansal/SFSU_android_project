package com.memoir.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.memoir.R;
import com.memoir.adapter.MemoirCursorAdapter.MemoirQuery;

public class DialigListViewCursorAdapter extends CursorAdapter {

	public DialigListViewCursorAdapter(Context context, Cursor c) {
		super(context, c);
	}

	public DialigListViewCursorAdapter(Context context) {
		super(context, null, 0);
	}

	@Override
	public void bindView(View view, Context arg1, Cursor cursor) {
		TextView name = (TextView) view.findViewById(R.id.dialog_listview_item);
		String s_name = cursor.getString(MemoirQuery.NAME);
		name.setText(s_name);

	}

	@Override
	public View newView(Context arg0, Cursor arg1, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View retView = inflater.inflate(R.layout.dialog_listvie_items, parent,
				false);
		return retView;
	}

}
