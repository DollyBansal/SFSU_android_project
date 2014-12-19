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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.memoir.R;
import com.memoir.adapter.DialogListViewCursorAdapter;
import com.memoir.adapter.MemoirCursorAdapter.MemoirQuery;
import com.memoir.model.Memoir.Memoirs;
import com.memoir.provider.DatabaseHelper;
import com.memoir.utils.DateConversion;

public class AddFlight extends Activity {
	private Button save, saveToTrip;
	private EditText name, from, destination, comment;
	private TextView date;
	private String s_name, s_date, s_from, s_destination, s_comment, s_like;
	private Context context = this;
	private DialogListViewCursorAdapter cursorAdapter;
	private DateConversion dateConversion;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_flight);
		dateConversion = new DateConversion();
		getActionBar().setTitle(
				getResources().getString(R.string.add__tittle_flight));

		save = (Button) findViewById(R.id.flight_save);
		saveToTrip = (Button) findViewById(R.id.flight_saveToTrip);

		name = (EditText) findViewById(R.id.flight_name);
		date = (TextView) findViewById(R.id.flight_date);
		from = (EditText) findViewById(R.id.flight_from);
		destination = (EditText) findViewById(R.id.flight_destination);
		comment = (EditText) findViewById(R.id.flight_comment);

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
		date.setText(strDateTime);

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

			String db_name = curs.getString(MemoirQuery.NAME);
			Date startDate = new Date(curs.getLong(MemoirQuery.STARTDATE));
			String db_date = dateConversion.dateToString(startDate);
			String db_from = curs.getString(MemoirQuery.FLIGHT_FROM);
			String db_to = curs.getString(MemoirQuery.FLIGHT_TO);
			String db_comment = curs.getString(MemoirQuery.COMMENT);

			name.setText(db_name);
			date.setText(db_date);
			from.setText(db_from);
			destination.setText(db_to);
			comment.setText(db_comment);

		}

		// save to database
		save.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				s_name = name.getText().toString();
				s_date = date.getText().toString();
				s_from = from.getText().toString();
				s_destination = destination.getText().toString();
				s_comment = comment.getText().toString();
				if (s_name.length() == 0) {
					Toast.makeText(getApplicationContext(),
							"Please enter name ", Toast.LENGTH_LONG).show();

				} else if (s_from.length() == 0) {
					Toast.makeText(getApplicationContext(),
							"Please enter flight from ", Toast.LENGTH_LONG)
							.show();

				} else if (s_destination.length() == 0) {
					Toast.makeText(getApplicationContext(),
							"Please enter flight destination ",
							Toast.LENGTH_LONG).show();

				} else if (s_comment.length() == 0) {
					Toast.makeText(getApplicationContext(),
							"Please enter comment ", Toast.LENGTH_LONG).show();

				} else {

					Date startDate = dateConversion.stringToDate(s_date);
					long sDate = startDate.getTime();

					final ContentResolver resolver = getContentResolver();
					ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();
					Builder productBuilder = ContentProviderOperation
							.newInsert(Memoirs.CONTENT_URI);
					productBuilder.withValue(Memoirs.ID, 1);
					productBuilder.withValue(Memoirs.Name, s_name);
					productBuilder.withValue(Memoirs.TYPE, "FLIGHT");
					productBuilder.withValue(Memoirs.Start_Date, sDate);
					productBuilder.withValue(Memoirs.FlightFrom, s_from);
					productBuilder.withValue(Memoirs.FlightTo, s_destination);
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
					Intent intent = new Intent(AddFlight.this,
							HomeActivity.class);
					startActivity(intent);
				}
			}
		});

		// save to trip and database
		saveToTrip.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {

				s_name = name.getText().toString();
				s_date = date.getText().toString();
				s_from = from.getText().toString();
				s_destination = destination.getText().toString();
				s_comment = comment.getText().toString();
				if (s_name.length() == 0) {
					Toast.makeText(getApplicationContext(),
							"Please enter name ", Toast.LENGTH_LONG).show();

				} else if (s_from.length() == 0) {
					Toast.makeText(getApplicationContext(),
							"Please enter flight from ", Toast.LENGTH_LONG)
							.show();

				} else if (s_destination.length() == 0) {
					Toast.makeText(getApplicationContext(),
							"Please enter flight destination ",
							Toast.LENGTH_LONG).show();

				} else if (s_comment.length() == 0) {
					Toast.makeText(getApplicationContext(),
							"Please enter comment ", Toast.LENGTH_LONG).show();

				} else {

					final Dialog dialog = new Dialog(context);

					dialog.setContentView(R.layout.custom_add_to_trip_dialog_box);
					dialog.setTitle("Add to Trip");
					ListView listView = (ListView) dialog
							.findViewById(R.id.listView_add_to_trip);

					final Cursor curs = getContentResolver().query(
							Memoirs.CONTENT_URI, MemoirQuery.PROJECTION,
							Memoirs.BY_Type,
							new String[] { String.valueOf("TRIP") }, null);

					cursorAdapter = new DialogListViewCursorAdapter(
							AddFlight.this, curs);
					listView.setAdapter(cursorAdapter);
					listView.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {

							TextView textView = (TextView) view
									.findViewById(R.id.dialog_listview_item);
							String addtoTrip = textView.getText().toString();
							s_name = name.getText().toString();
							s_date = date.getText().toString();

							s_comment = comment.getText().toString();

							Date startDate = dateConversion
									.stringToDate(s_date);
							long sDate = startDate.getTime();

							final ContentResolver resolver = getContentResolver();
							ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();
							Builder productBuilder = ContentProviderOperation
									.newInsert(Memoirs.CONTENT_URI);
							productBuilder.withValue(Memoirs.ID, 1);
							productBuilder.withValue(Memoirs.Name, s_name);
							productBuilder.withValue(Memoirs.TYPE, "FLIGHT");
							productBuilder.withValue(Memoirs.TRIP_NAME,
									addtoTrip);
							productBuilder.withValue(Memoirs.Start_Date, sDate);
							productBuilder
									.withValue(Memoirs.FlightFrom, s_from);
							productBuilder.withValue(Memoirs.FlightTo,
									s_destination);
							productBuilder.withValue(Memoirs.LikeOrNot, s_like);
							productBuilder
									.withValue(Memoirs.Comment, s_comment);

							operations.add(productBuilder.build());
							try {
								resolver.applyBatch(
										DatabaseHelper.CONTENT_AUTHORITY,
										operations);

							} catch (RemoteException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (OperationApplicationException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							// start Home Activity

							int s_id = curs.getInt(MemoirQuery._ID);
							Intent intent = new Intent(AddFlight.this,
									DetailTripViewActivity.class);

							Bundle mBundle = new Bundle();
							mBundle.putInt("id", s_id);
							mBundle.putString("name", addtoTrip);
							intent.putExtras(mBundle);
							startActivity(intent);

						}
					});
					Button ok = (Button) dialog
							.findViewById(R.id.add_to_trip_ok);

					Button cancel = (Button) dialog
							.findViewById(R.id.add_to_trip_cancel);
					cancel.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {

							dialog.dismiss();
						}
					});

					ok.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {

							dialog.dismiss();
						}
					});
					dialog.show();

				}
			}
		});

		// save date
		date.setOnClickListener(new View.OnClickListener() {

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

						date.setText(strDateTime);

						dialog.dismiss();
					}
				});
				dialog.show();
			}
		});
	}

}
