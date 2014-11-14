package com.memoir.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentProviderOperation;
import android.content.ContentProviderOperation.Builder;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.memoir.R;
import com.memoir.model.Restaurent.Restaurents;
import com.memoir.provider.DatabaseHelper;

public class AddRestaurent extends Activity {
	Button save, saveToTrip, edit_date;
	EditText name, address, comment, like;
	TextView date;
	String s_name, s_date, s_address, s_comment, s_like;
	private Context context = this;
	Date myDate;
	SimpleDateFormat sdf;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_restaurent);
		save = (Button) findViewById(R.id.rest_save);
		saveToTrip = (Button) findViewById(R.id.rest_saveToTrip);

		name = (EditText) findViewById(R.id.rest_name);
		date = (TextView) findViewById(R.id.rest_date);
		address = (EditText) findViewById(R.id.rest_address);
		like = (EditText) findViewById(R.id.rest_likeOrNot);
		comment = (EditText) findViewById(R.id.rest_comment);

		edit_date = (Button) findViewById(R.id.rest_edit_date_time);

		sdf = new SimpleDateFormat("E, dd/MM/yyyy,  HH:mm:ss");
		String currentDateandTime = sdf.format(new Date());
		date.setText(currentDateandTime);
		// private final DateFormat mDateFormat = DateFormat
		// .getDateInstance(DateFormat.MEDIUM);
		// Date date = new Date(
		// cursor.getLong(RestaurentQuery.Date));
		// date.setText(mDateFormat.format(date).toUpperCase());
		save.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				s_name = name.getText().toString();
				s_date = date.getText().toString();
				s_address = address.getText().toString();
				s_like = like.getText().toString();
				s_comment = comment.getText().toString();

				final ContentResolver resolver = getContentResolver();
				ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();
				Builder productBuilder = ContentProviderOperation
						.newInsert(Restaurents.CONTENT_URI);
				productBuilder.withValue(Restaurents.ID, 1);
				productBuilder.withValue(Restaurents.Name, s_name);
				productBuilder.withValue(Restaurents.Adress, s_address);
				productBuilder.withValue(Restaurents.Date, new Date().getTime());
				productBuilder.withValue(Restaurents.LikeOrNot, s_like);
				productBuilder.withValue(Restaurents.Comment, s_comment);
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
				Intent intent = new Intent(AddRestaurent.this,
						HomeActivity.class);
				startActivity(intent);
			}
		});
		saveToTrip.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {

			}
		});
		edit_date.setOnClickListener(new View.OnClickListener() {

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
						dialog.dismiss();
					}
				});
				reset.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {

					}
				});

				set.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						String strDateTime = dp.getYear() + "-"
								+ (dp.getMonth() + 1) + "-"
								+ dp.getDayOfMonth() + " "
								+ tp.getCurrentHour() + ":"
								+ tp.getCurrentMinute();

						Toast.makeText(AddRestaurent.this,
								"User selected " + strDateTime + "Time",
								Toast.LENGTH_LONG).show();

						dialog.dismiss();
					}
				});
				dialog.show();
			}
		});
	}

}
