package com.memoir.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentProviderOperation.Builder;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.memoir.R;
import com.memoir.model.Restaurent.Restaurents;
import com.memoir.provider.DatabaseHelper.Tables;

public class AddRestaurent extends Activity implements LoaderCallbacks<Cursor> {
	Button save, saveToTrip;
	EditText name, address, comment, like;
	String s_name, s_address, s_comment, s_like;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_restaurent);
		save = (Button) findViewById(R.id.rest_save);
		saveToTrip = (Button) findViewById(R.id.rest_saveToTrip);

		name = (EditText) findViewById(R.id.rest_name);
		address = (EditText) findViewById(R.id.rest_address);
		like = (EditText) findViewById(R.id.rest_likeOrNot);
		comment = (EditText) findViewById(R.id.rest_comment);
		s_name = name.getText().toString();
		s_address = name.getText().toString();
		s_address = name.getText().toString();
		s_comment = name.getText().toString();

		save.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {

				ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();
				/*Builder RestaurentDeleteBuilder = ContentProviderOperation
						.newDelete(Restaurents.CONTENT_URI);
				operations.add(RestaurentDeleteBuilder.build());*/

				Builder productBuilder = ContentProviderOperation
						.newInsert(Restaurents.CONTENT_URI);
				productBuilder.withValue(Restaurents.Name, s_name);
				operations.add(productBuilder.build());
			}
		});
		saveToTrip.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {

			}
		});
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub

		// getContentResolver().insert(Uri, ContentValues);
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub

	}

	public ArrayList<ContentProviderOperation> getOperation(int id) {
		ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();
		Builder RestaurentDeleteBuilder = ContentProviderOperation
				.newDelete(Restaurents.CONTENT_URI);
		operations.add(RestaurentDeleteBuilder.build());

		Builder productBuilder = ContentProviderOperation
				.newInsert(Restaurents.CONTENT_URI);
		productBuilder.withValue(Restaurents.Name, s_name);
		operations.add(productBuilder.build());
		return operations;

	}

}
