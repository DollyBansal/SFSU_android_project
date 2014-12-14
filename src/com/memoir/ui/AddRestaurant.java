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
import com.memoir.adapter.DialigListViewCursorAdapter;
import com.memoir.adapter.MemoirCursorAdapter.MemoirQuery;
import com.memoir.model.Memoir.Memoirs;
import com.memoir.provider.DatabaseHelper;
import com.memoir.utils.DateConversion;

public class AddRestaurant extends Activity {
	Button save, saveToTrip, edit_date;
	EditText name, address, comment;
	TextView date;
	String s_name, s_date, s_address, s_comment, s_like;
	private Context context = this;
	private DialigListViewCursorAdapter cursorAdapter;
	private DateConversion dateConversion;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_restaurent);
		dateConversion = new DateConversion();
		getActionBar().setTitle(
				getResources().getString(R.string.add__tittle_restaurant));
		save = (Button) findViewById(R.id.rest_save);
		saveToTrip = (Button) findViewById(R.id.rest_saveToTrip);

		name = (EditText) findViewById(R.id.rest_name);
		date = (TextView) findViewById(R.id.rest_date);
		address = (EditText) findViewById(R.id.rest_address);
		comment = (EditText) findViewById(R.id.rest_comment);

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
				s_like = ((Spinner) parent).getSelectedItem().toString();

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		edit_date = (Button) findViewById(R.id.rest_edit_date_time);

		Calendar c = Calendar.getInstance();
		String strDateTime = (c.get(Calendar.MONTH) + 1) + "/"
				+ c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.YEAR)
				+ " " + c.get(Calendar.HOUR_OF_DAY) + ":"
				+ c.get(Calendar.MINUTE);
		date.setText(strDateTime);

		// save to database
		save.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				s_name = name.getText().toString();
				s_date = date.getText().toString();
				s_address = address.getText().toString();
				s_comment = comment.getText().toString();

				if (s_name.length() == 0) {
					Toast.makeText(getApplicationContext(),
							"Please enter name ", Toast.LENGTH_LONG).show();

				} else if (s_address.length() == 0) {
					Toast.makeText(getApplicationContext(),
							"Please enter address ", Toast.LENGTH_LONG).show();

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
					productBuilder.withValue(Memoirs.TYPE, getResources()
							.getString(R.string.restaurent));
					productBuilder.withValue(Memoirs.Address, s_address);
					productBuilder.withValue(Memoirs.Start_Date, sDate);
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
					Intent intent = new Intent(AddRestaurant.this,
							HomeActivity.class);
					startActivity(intent);
				}

			}
		});

		// on button click to save in a particular trip
		saveToTrip.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {

				s_name = name.getText().toString();
				s_date = date.getText().toString();
				s_address = address.getText().toString();
				s_comment = comment.getText().toString();

				if (s_name.length() == 0) {
					Toast.makeText(getApplicationContext(),
							"Please enter name ", Toast.LENGTH_LONG).show();

				} else if (s_address.length() == 0) {
					Toast.makeText(getApplicationContext(),
							"Please enter address ", Toast.LENGTH_LONG).show();

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

					cursorAdapter = new DialigListViewCursorAdapter(
							AddRestaurant.this, curs);
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
							s_address = address.getText().toString();
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
							productBuilder.withValue(
									Memoirs.TYPE,
									getResources().getString(
											R.string.restaurent));
							productBuilder.withValue(Memoirs.TRIP_NAME,
									addtoTrip);
							productBuilder
									.withValue(Memoirs.Address, s_address);
							productBuilder.withValue(Memoirs.Start_Date, sDate);
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

							int s_id = curs.getInt(MemoirQuery._ID);
							Intent intent = new Intent(AddRestaurant.this,
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

		// edit date and time in dialogBox
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
