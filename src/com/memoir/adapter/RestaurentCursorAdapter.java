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
import com.memoir.model.Restaurent.Restaurents;
import com.memoir.provider.DatabaseHelper.Tables;
import com.memoir.utils.DatabaseUtils;

public class RestaurentCursorAdapter extends CursorAdapter {
	private TextView name, t_date, like, comment;
	private String s_name, s_date, s_like, s_comment;
	private final DateFormat mDateFormat = DateFormat
			.getDateInstance(DateFormat.MEDIUM);

	public RestaurentCursorAdapter(Context context, Cursor c) {
		super(context, c);
	}

	public interface RestaurentQuery {
		String[] PROJECTION = DatabaseUtils.prefixProjection(Tables.RESTAURENT,
				Restaurents._ID, Restaurents.ID, Restaurents.Name,
				Restaurents.Adress, Restaurents.Date, Restaurents.LikeOrNot,
				Restaurents.Comment);

		int ID = 1;
		int NAME = 2;
		int ADDRESS = 3;
		int DATE = 4;
		int LIKE = 5;
		int COMMENT = 6;

	}

	@Override
	public void bindView(View view, Context arg1, Cursor cursor) {
		populateView(view, cursor);
	}

	private void populateView(View view, Cursor cursor) {
		s_name = cursor.getString(RestaurentQuery.NAME);
		name = (TextView) view.findViewById(R.id.list_name);
		name.setText(s_name);

		Date date = new Date(cursor.getLong(RestaurentQuery.DATE));
		t_date = (TextView) view.findViewById(R.id.list_date);
		t_date.setText(mDateFormat.format(date));

		s_like = cursor.getString(RestaurentQuery.NAME);
		like = (TextView) view.findViewById(R.id.list_like);
		like.setText(s_like);

		s_comment = cursor.getString(RestaurentQuery.NAME);
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
