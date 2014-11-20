package com.memoir.adapter;

import java.text.DateFormat;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.memoir.R;
import com.memoir.model.Memoir.Memoirs;
import com.memoir.provider.DatabaseHelper.Tables;
import com.memoir.utils.DatabaseUtils;

public class MemoirCursorAdapter extends CursorAdapter {
	private TextView name, t_date, like, comment;
	private String s_name, s_date, s_like, s_comment;
	private final DateFormat mDateFormat = DateFormat
			.getDateInstance(DateFormat.MEDIUM);

	public MemoirCursorAdapter(Context context, Cursor c) {
		super(context, c);
	}

	public interface MemoirQuery {
		String[] PROJECTION = DatabaseUtils.prefixProjection(Tables.MEMOIR,
				Memoirs._ID, Memoirs.ID, Memoirs.Name, Memoirs.Address,
				Memoirs.Start_Date, Memoirs.End_Date, Memoirs.LikeOrNot,
				Memoirs.Comment);

		int ID = 1;
		int NAME = 2;
		int ADDRESS = 3;
		int STARTDATE = 4;
		int ENDDATE = 5;
		int LIKE = 6;
		int COMMENT = 7;

	}

	@Override
	public void bindView(View view, Context arg1, Cursor cursor) {
		populateView(view, cursor);
	}

	private void populateView(View view, Cursor cursor) {
		s_name = cursor.getString(MemoirQuery.NAME);
		name = (TextView) view.findViewById(R.id.list_name);
		name.setText(s_name);

		Date date = new Date(cursor.getLong(MemoirQuery.STARTDATE));
		t_date = (TextView) view.findViewById(R.id.list_date);
		t_date.setText(mDateFormat.format(date));

		s_like = cursor.getString(MemoirQuery.LIKE);
		like = (TextView) view.findViewById(R.id.list_like);
		like.setText(s_like);

		s_comment = cursor.getString(MemoirQuery.COMMENT);
		comment = (TextView) view.findViewById(R.id.list_comment);
		comment.setText(s_comment);

		cursor.moveToNext();

	}

	@Override
	public View newView(Context context, Cursor arg1, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View retView = inflater.inflate(R.layout.home_listview, parent, false);
		return retView;
	}

}
