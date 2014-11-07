package com.memoir.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.memoir.R;

public class AddNewEntry extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_new_entry);

		getActionBar().setTitle("Add a");
		Button addTrip = ((Button) this.findViewById(R.id.addTrip));
		Button addRestaurent = ((Button) this.findViewById(R.id.addRestaurent));
		Button addHotel = ((Button) this.findViewById(R.id.addHotel));
		Button addPlace = ((Button) this.findViewById(R.id.addPlace));
		Button addFlight = ((Button) this.findViewById(R.id.addFlight));

		addTrip.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				Intent intent = new Intent(AddNewEntry.this, AddTrip.class);
				startActivity(intent);
			}
		});
		addRestaurent.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				Intent intent = new Intent(AddNewEntry.this,
						AddRestaurent.class);
				startActivity(intent);
			}
		});
		addHotel.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				Intent intent = new Intent(AddNewEntry.this, AddHotel.class);
				startActivity(intent);
			}
		});
		addPlace.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				Intent intent = new Intent(AddNewEntry.this, AddPlace.class);
				startActivity(intent);
			}
		});
		addFlight.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				Intent intent = new Intent(AddNewEntry.this, AddFlight.class);
				startActivity(intent);
			}
		});

	}

}
