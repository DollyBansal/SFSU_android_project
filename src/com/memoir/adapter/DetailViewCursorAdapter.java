package com.memoir.adapter;

import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.memoir.R;
import com.memoir.adapter.MemoirCursorAdapter.MemoirQuery;
import com.memoir.ui.AddFlight;
import com.memoir.ui.AddHotel;
import com.memoir.ui.AddPlace;
import com.memoir.ui.AddRestaurant;
import com.memoir.ui.AddTrip;
import com.memoir.utils.DateConversion;

public class DetailViewCursorAdapter extends CursorAdapter {
	TextView name, start_date, end_date, address, flight_to, flight_from,
			likeOrNot, comment;
	String s_name, s_start_date, s_end_date, s_address, s_flight_to,
			s_flight_from, s_comment, s_likeOrNot;
	String s_id;
	String type;
	DateConversion dateConversion;

	public DetailViewCursorAdapter(Context context, Cursor c) {
		super(context, c);
	}

	public DetailViewCursorAdapter(Context context) {

		super(context, null, 0);
	}

	private Context getContext() {
		return mContext;
	}

	@Override
	public void bindView(View view, Context arg1, Cursor cursor) {
		populateView(view, cursor);

	}

	private void populateView(View view, final Cursor cursor) {
		dateConversion = new DateConversion();

		name = (TextView) view.findViewById(R.id.name_detail);
		s_name = cursor.getString(MemoirQuery.NAME);
		name.setText(s_name);

		start_date = (TextView) view.findViewById(R.id.start_date_detail);
		Date startDate = new Date(cursor.getLong(MemoirQuery.STARTDATE));
		s_start_date = dateConversion.dateToString(startDate);
		start_date.setText(s_start_date);

		end_date = (TextView) view.findViewById(R.id.end_date_detail);
		Date endDate = new Date(cursor.getLong(MemoirQuery.ENDDATE));
		s_end_date = dateConversion.dateToString(endDate);
		end_date.setText(s_end_date);

		address = (TextView) view.findViewById(R.id.address_detail);
		s_address = cursor.getString(MemoirQuery.ADDRESS);
		address.setText(s_address);
		if (s_address == null) {
			android.view.ViewGroup.LayoutParams layoutParams_a = address
					.getLayoutParams();
			layoutParams_a.width = 0;
			layoutParams_a.height = 0;
			address.setLayoutParams(layoutParams_a);
		}

		flight_from = (TextView) view.findViewById(R.id.flight_from_detail);
		s_flight_from = cursor.getString(MemoirQuery.FLIGHT_FROM);
		flight_from.setText(s_flight_from);

		if (s_flight_from == null) {
			android.view.ViewGroup.LayoutParams layoutParams_ff = flight_from
					.getLayoutParams();
			layoutParams_ff.width = 0;
			layoutParams_ff.height = 0;
			flight_from.setLayoutParams(layoutParams_ff);

		}

		flight_to = (TextView) view.findViewById(R.id.flight_to_detail);
		s_flight_to = cursor.getString(MemoirQuery.FLIGHT_TO);
		flight_to.setText(s_flight_to);

		if (s_flight_to == null) {
			android.view.ViewGroup.LayoutParams layoutParams_ft = flight_from
					.getLayoutParams();
			layoutParams_ft.width = 0;
			layoutParams_ft.height = 0;
			flight_from.setLayoutParams(layoutParams_ft);
		}

		likeOrNot = (TextView) view.findViewById(R.id.like_detail);
		s_likeOrNot = cursor.getString(MemoirQuery.LIKE);
		likeOrNot.setText(s_likeOrNot);

		if (s_likeOrNot == null) {
			android.view.ViewGroup.LayoutParams layoutParams_l = flight_from
					.getLayoutParams();
			layoutParams_l.width = 0;
			layoutParams_l.height = 0;
			flight_from.setLayoutParams(layoutParams_l);
		}

		comment = (TextView) view.findViewById(R.id.comment_detail);
		s_comment = cursor.getString(MemoirQuery.COMMENT);
		comment.setText(s_comment);

		type = cursor.getString(MemoirQuery.TYPE);

		if (s_comment == null) {
			android.view.ViewGroup.LayoutParams layoutParams_c = flight_from
					.getLayoutParams();
			layoutParams_c.width = 0;
			layoutParams_c.height = 0;
			flight_from.setLayoutParams(layoutParams_c);
		}

		ImageButton btn = (ImageButton) view.findViewById(R.id.imageButton1);
		int pos = cursor.getPosition();
		btn.setTag(pos);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Integer pos = (Integer) v.getTag();

				System.out.println("poss:: " + pos);

				Intent intent = null;
				if (type.equals(getContext().getResources().getString(
						R.string.restaurent))) {
					intent = new Intent(getContext(), AddRestaurant.class);

				} else if (type.equals(getContext().getResources().getString(
						R.string.trip))) {
					intent = new Intent(getContext(), AddTrip.class);

				} else if (type.equals(getContext().getResources().getString(
						R.string.place))) {
					intent = new Intent(getContext(), AddPlace.class);

				} else if (type.equals(getContext().getResources().getString(
						R.string.hotel))) {
					intent = new Intent(getContext(), AddHotel.class);

				} else if (type.equals(getContext().getResources().getString(
						R.string.flight))) {
					intent = new Intent(getContext(), AddFlight.class);

				}

				s_id = cursor.getString(MemoirQuery.ID);
				// Bundle mBundle = new Bundle();
				// mBundle.putString("id", s_id);
				// intent.putExtras(mBundle);
				getContext().startActivity(intent);

			}
		});
		cursor.moveToNext();
	}

	@Override
	public View newView(Context arg0, Cursor arg1, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View retView = inflater.inflate(R.layout.detail_view_listview, parent,
				false);
		return retView;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		return super.getView(arg0, arg1, arg2);
	}

}
