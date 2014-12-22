package com.memoir.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentProviderOperation;
import android.content.ContentProviderOperation.Builder;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.model.LatLng;
import com.memoir.R;
import com.memoir.adapter.DialogListViewCursorAdapter;
import com.memoir.adapter.MemoirCursorAdapter.MemoirQuery;
import com.memoir.model.Memoir.Memoirs;
import com.memoir.network.PlaceJSONParser;
import com.memoir.provider.DatabaseHelper;
import com.memoir.utils.DateConversion;

public class AddRestaurant extends Activity implements LocationListener {
	private Button save, saveToTrip;
	private EditText name, comment;
	private TextView date;
	private String s_name, s_date, s_address, s_comment, s_like;
	private Context context = this;
	private DialogListViewCursorAdapter cursorAdapter;
	private DateConversion dateConversion;
	private AutoCompleteTextView address;
	private ArrayAdapter<String> adapter;

	double mLatitude = 0;
	double mLongitude = 0;
	private ArrayList<String> resultList;
	private int delete_id;

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

		address = (AutoCompleteTextView) findViewById(R.id.rest_address);

		// address.requestFocus();

		address.setAdapter(adapter);

		address.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				System.out.println("click on autocomple");

				// network();

			}
		});

		address.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence charEnter, int start,
					int before, int count) {
				if (charEnter.length() > 2)
					network(charEnter);

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				// if (address.getText().length() > 2)
				// network(address.getText());

			}
		});
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

			delete_id = curs.getInt(MemoirQuery._ID);
			String db_name = curs.getString(MemoirQuery.NAME);
			Date startDate = new Date(curs.getLong(MemoirQuery.STARTDATE));
			String db_date = dateConversion.dateToString(startDate);
			String db_address = curs.getString(MemoirQuery.ADDRESS);
			String db_comment = curs.getString(MemoirQuery.COMMENT);

			name.setText(db_name);
			date.setText(db_date);
			address.setText(db_address);
			comment.setText(db_comment);

		}

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

					cursorAdapter = new DialogListViewCursorAdapter(
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

							if (delete_id != 0) {

								getContentResolver().delete(
										Memoirs.CONTENT_URI,
										Memoirs.BY_ID,
										new String[] { String
												.valueOf(delete_id) }

								);
							}

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

	@Override
	public void onLocationChanged(Location location) {
		mLatitude = location.getLatitude();
		mLongitude = location.getLongitude();
		LatLng latLng = new LatLng(mLatitude, mLongitude);

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	public void network(CharSequence charEnter) {
		// Getting Google Play availability status
		int status = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getBaseContext());

		if (status != ConnectionResult.SUCCESS) { // Google Play Services are
													// not available

			int requestCode = 10;
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this,
					requestCode);
			dialog.show();

		} else { // Google Play Services are available

			// Getting LocationManager object from System Service
			// LOCATION_SERVICE
			LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

			// Creating a criteria object to retrieve provider
			Criteria criteria = new Criteria();

			// Getting the name of the best provider
			String provider = locationManager.getBestProvider(criteria, true);

			// Getting Current Location From GPS
			Location location = locationManager.getLastKnownLocation(provider);

			if (location != null) {
				onLocationChanged(location);
			}

			locationManager.requestLocationUpdates(provider, 20000, 0, this);

			StringBuilder sb = new StringBuilder(
					"https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
			sb.append("location=" + mLatitude + "," + mLongitude);
			sb.append("&radius=5000");
			sb.append("&name=@" + charEnter);
			sb.append("&sensor=true");
			sb.append("&key=AIzaSyDpbGqEqIDq9YZDh6nk2ce9i6435EH1N40");

			// Creating a new non-ui thread task to download Google place json
			// data
			PlacesTask placesTask = new PlacesTask();

			// Invokes the "doInBackground()" method of the class PlaceTask
			placesTask.execute(sb.toString());

		}

	}

	/** A method to download json data from url */
	private String downloadUrl(String strUrl) throws IOException {
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(strUrl);

			// Creating an http connection to communicate with url
			urlConnection = (HttpURLConnection) url.openConnection();

			// Connecting to url
			urlConnection.connect();

			// Reading data from url
			iStream = urlConnection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					iStream));

			StringBuffer sb = new StringBuffer();

			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			data = sb.toString();

			br.close();

		} catch (Exception e) {
			Log.d("Exception while downloading url", e.toString());
		} finally {
			iStream.close();
			urlConnection.disconnect();
		}

		return data;
	}

	/** A class, to download Google Places */
	private class PlacesTask extends AsyncTask<String, Integer, String> {

		String data = null;

		// Invoked by execute() method of this object
		@Override
		protected String doInBackground(String... url) {
			try {
				data = downloadUrl(url[0]);
			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}
			return data;
		}

		// Executed after the complete execution of doInBackground() method
		@Override
		protected void onPostExecute(String result) {
			ParserTask parserTask = new ParserTask();
			// Start parsing the Google places in JSON format
			// Invokes the "doInBackground()" method of the class ParseTask
			if (result != null) {
				parserTask.execute(result);
			}
		}

	}

	/** A class to parse the Google Places in JSON format */
	private class ParserTask extends
			AsyncTask<String, Integer, List<HashMap<String, String>>> {

		JSONObject jObject;

		// Invoked by execute() method of this object
		@Override
		protected List<HashMap<String, String>> doInBackground(
				String... jsonData) {

			List<HashMap<String, String>> places = null;
			PlaceJSONParser placeJsonParser = new PlaceJSONParser();

			try {
				jObject = new JSONObject(jsonData[0]);

				/** Getting the parsed data as a List construct */
				places = placeJsonParser.parse(jObject);
				for (int i = 0; i <= places.size(); i++) {

				}

			} catch (Exception e) {
				Log.d("Exception", e.toString());
			}
			return places;
		}

		// Executed after the complete execution of doInBackground() method
		@Override
		protected void onPostExecute(List<HashMap<String, String>> list) {

			resultList = new ArrayList<String>(list.size());

			String array[] = new String[list.size()];

			for (int i = 0; i < list.size(); i++) {

				// Getting a place from the places list
				HashMap<String, String> hmPlace = list.get(i);
				// Getting latitude of the place
				double lat = Double.parseDouble(hmPlace.get("lat"));

				// Getting longitude of the place
				double lng = Double.parseDouble(hmPlace.get("lng"));

				// Getting name
				String name = hmPlace.get("place_name");

				// Getting vicinity
				String vicinity = hmPlace.get("vicinity");

				// Setting the position for the marker
				resultList.add(name + " " + vicinity);

				array[i] = resultList.get(i);

			}

			adapter = new ArrayAdapter<String>(AddRestaurant.this,
					android.R.layout.simple_list_item_1);

			/* Displaying Array elements */
			for (String k : array) {

				adapter.add(k);
			}

			address.setAdapter(adapter);

		}

	}

}
