package com.memoir.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentProviderOperation;
import android.content.ContentProviderOperation.Builder;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.format.Time;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.memoir.R;
import com.memoir.adapter.MemoirCursorAdapter.MemoirQuery;
import com.memoir.model.Memoir.Memoirs;
import com.memoir.provider.DatabaseHelper;
import com.memoir.utils.DateConversion;

public class AddTrip extends Activity {
	private Button save;
	private EditText name, comment;
	private TextView start_date, end_date;
	private String s_name, s_start_date, s_end_date, s_comment, s_like;
	private Context context = this;
	private DateConversion dateConversion;
	private int delete_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_trip);
		dateConversion = new DateConversion();
		getActionBar().setTitle(
				getResources().getString(R.string.add__tittle_trip));
		save = (Button) findViewById(R.id.trip_save);

		name = (EditText) findViewById(R.id.trip_name);
		start_date = (TextView) findViewById(R.id.trip_start_date);
		end_date = (TextView) findViewById(R.id.trip_end_date);
		comment = (EditText) findViewById(R.id.trip_comment);

		final Spinner dropdown = (Spinner) findViewById(R.id.spinner);
		String[] items = new String[] {
				getResources().getString(R.string.i_liked_it),
				getResources().getString(R.string.i_didnt_like_it) };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, items);
		dropdown.setAdapter(adapter);

		dropdown.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				s_like = ((Spinner) parent).getSelectedItem().toString();

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		Calendar c = Calendar.getInstance();
		String strDateTime = (c.get(Calendar.MONTH) + 1) + "/"
				+ c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.YEAR)
				+ " " + c.get(Calendar.HOUR_OF_DAY) + ":"
				+ c.get(Calendar.MINUTE);
		start_date.setText(strDateTime);
		end_date.setText(strDateTime);

		Bundle bundle1 = getIntent().getExtras();
		int datas = 0;
		if (bundle1 != null) {
			datas = bundle1.getInt("idd");
		}

		if (datas != 0) {
			Cursor curs = this.getContentResolver().query(Memoirs.CONTENT_URI,
					MemoirQuery.PROJECTION, Memoirs.BY_ID,
					new String[] { String.valueOf(datas) }, null);
			curs.moveToFirst();

			delete_id = curs.getInt(MemoirQuery._ID);
			String db_name = curs.getString(MemoirQuery.NAME);
			Date startDate = new Date(curs.getLong(MemoirQuery.STARTDATE));
			String db_s_date = dateConversion.dateToString(startDate);
			Date endDate = new Date(curs.getLong(MemoirQuery.ENDDATE));
			String db_e_date = dateConversion.dateToString(endDate);
			String db_comment = curs.getString(MemoirQuery.COMMENT);

			name.setText(db_name);
			start_date.setText(db_s_date);
			end_date.setText(db_e_date);
			comment.setText(db_comment);

		}

		// save to database
		save.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				s_name = name.getText().toString();
				s_start_date = start_date.getText().toString();
				s_end_date = end_date.getText().toString();
				s_comment = comment.getText().toString();

				if (s_name.length() == 0) {
					Toast.makeText(getApplicationContext(),
							"Please enter name ", Toast.LENGTH_LONG).show();

				} else if (s_comment.length() == 0) {
					Toast.makeText(getApplicationContext(),
							"Please enter comment ", Toast.LENGTH_LONG).show();

				} else {

					Date startDate = dateConversion.stringToDate(s_start_date);
					long sDate = startDate.getTime();
					Date endDate = dateConversion.stringToDate(s_end_date);
					long eDate = endDate.getTime();

					if (delete_id != 0) {

						getContentResolver().delete(Memoirs.CONTENT_URI,
								Memoirs.BY_ID,
								new String[] { String.valueOf(delete_id) }

						);
					}
					final ContentResolver resolver = getContentResolver();
					ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();
					Builder productBuilder = ContentProviderOperation
							.newInsert(Memoirs.CONTENT_URI);
					productBuilder.withValue(Memoirs.ID, 1);
					productBuilder.withValue(Memoirs.Name, s_name);
					productBuilder.withValue(Memoirs.TYPE, getResources()
							.getString(R.string.trip));
					productBuilder.withValue(Memoirs.Start_Date, sDate);
					productBuilder.withValue(Memoirs.End_Date, eDate);
					productBuilder.withValue(Memoirs.LikeOrNot, s_like);
					productBuilder.withValue(Memoirs.Comment, s_comment);
					operations.add(productBuilder.build());
					try {
						resolver.applyBatch(DatabaseHelper.CONTENT_AUTHORITY,
								operations);

					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (OperationApplicationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// start Home Activity
					Intent intent = new Intent(AddTrip.this, HomeActivity.class);
					startActivity(intent);
				}
			}
		});

		// start date
		start_date.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {

				final Dialog dialog = new Dialog(context);

				dialog.setContentView(R.layout.custom_date_time_dialogbox);
				dialog.setTitle("Set Date & Time");
				final TimePicker tp = (TimePicker) dialog
						.findViewById(R.id.timePicker1);
				final DatePicker dp = (DatePicker) dialog
						.findViewById(R.id.datePicker1);
				Button set = (Button) dialog.findViewById(R.id.set);
				Button reset = (Button) dialog.findViewById(R.id.reset);
				Button cancel = (Button) dialog.findViewById(R.id.cancel);
				cancel.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Time now = new Time();
						now.setToNow();
						tp.setCurrentHour(now.hour);
						tp.setCurrentMinute(now.minute);

						// update the DatePicker
						dp.updateDate(now.year, now.month, now.monthDay);
						dialog.dismiss();
					}
				});
				reset.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Time now = new Time();
						now.setToNow();
						tp.setCurrentHour(now.hour);
						tp.setCurrentMinute(now.minute);

						// update the DatePicker
						dp.updateDate(now.year, now.month, now.monthDay);
					}
				});

				set.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						String strDateTime = (dp.getMonth() + 1) + "/"
								+ dp.getDayOfMonth() + "/" + dp.getYear() + " "
								+ tp.getCurrentHour() + ":"
								+ tp.getCurrentMinute();

						start_date.setText(strDateTime);

						dialog.dismiss();
					}
				});
				dialog.show();
			}
		});

		// end date
		end_date.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				final Dialog dialog = new Dialog(context);

				dialog.setContentView(R.layout.custom_date_time_dialogbox);
				dialog.setTitle("Set Date & Time");
				final TimePicker tp = (TimePicker) dialog
						.findViewById(R.id.timePicker1);
				final DatePicker dp = (DatePicker) dialog
						.findViewById(R.id.datePicker1);
				Button set = (Button) dialog.findViewById(R.id.set);
				Button reset = (Button) dialog.findViewById(R.id.reset);
				Button cancel = (Button) dialog.findViewById(R.id.cancel);
				cancel.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Time now = new Time();
						now.setToNow();
						tp.setCurrentHour(now.hour);
						tp.setCurrentMinute(now.minute);

						// update the DatePicker
						dp.updateDate(now.year, now.month, now.monthDay);
						dialog.dismiss();
					}
				});
				reset.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Time now = new Time();
						now.setToNow();
						tp.setCurrentHour(now.hour);
						tp.setCurrentMinute(now.minute);

						// update the DatePicker
						dp.updateDate(now.year, now.month, now.monthDay);
					}
				});

				set.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						String strDateTime = (dp.getMonth() + 1) + "/"
								+ dp.getDayOfMonth() + "/" + dp.getYear() + " "
								+ tp.getCurrentHour() + ":"
								+ tp.getCurrentMinute();

						end_date.setText(strDateTime);

						dialog.dismiss();
					}
				});
				dialog.show();
			}
		});
	}

}
