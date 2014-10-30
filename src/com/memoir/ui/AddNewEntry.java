package com.memoir.ui;

import com.memoir.R;

import android.app.Activity;
import android.os.Bundle;

public class AddNewEntry extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_new_entry);
		
		getActionBar().setTitle("Add a");
	}

}
