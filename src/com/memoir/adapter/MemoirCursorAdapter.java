package com.memoir.adapter;

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
import com.memoir.utils.DateConversion;

public class MemoirCursorAdapter extends CursorAdapter {
	private TextView name, t_date, like, comment;
	private String s_name, s_date, s_like, s_comment;
	DateConversion dateConversion;

	public MemoirCursorAdapter(Context context, Cursor c) {
		super(context, c);
	}

	public MemoirCursorAdapter(Context context) {
		super(context, null, 0);
	}

	public interface MemoirQuery {
		String[] PROJECTION = DatabaseUtils.prefixProjection(Tables.MEMOIR,
				Memoirs._ID, Memoirs.ID, Memoirs.Name, Memoirs.Address,
				Memoirs.Start_Date, Memoirs.End_Date, Memoirs.LikeOrNot,
				Memoirs.Comment, Memoirs.FlightFrom, Memoirs.FlightTo,
				Memoirs.TYPE, Memoirs.TRIP_NAME);
		int _ID = 0;
		int ID = 1;
		int NAME = 2;
		int ADDRESS = 3;
		int STARTDATE = 4;
		int ENDDATE = 5;
		int LIKE = 6;
		int COMMENT = 7;
		int FLIGHT_FROM = 8;
		int FLIGHT_TO = 9;
		int TYPE = 10;
		int TRIP_NAME = 11;

	}

	@Override
	public void bindView(View view, Context arg1, Cursor cursor) {
		populateView(view, cursor);
	}

	private void populateView(View view, Cursor cursor) {
		dateConversion = new DateConversion();
		s_name = cursor.getString(MemoirQuery.NAME);
		name = (TextView) view.findViewById(R.id.list_name);
		name.setText(s_name);

		t_date = (TextView) view.findViewById(R.id.list_date);
		Date startDate = new Date(cursor.getLong(MemoirQuery.STARTDATE));
		s_date = dateConversion.dateToString(startDate);
		t_date.setText(s_date);

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
