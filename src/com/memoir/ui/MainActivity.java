package com.memoir.ui;

import android.app.ActionBar;
import android.content.Intent;
import android.content.Loader;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.memoir.R;
import com.memoir.ui.fragment.FlightFragment;
import com.memoir.ui.fragment.HotelFragment;
import com.memoir.ui.fragment.PlaceFragment;
import com.memoir.ui.fragment.RestaurantFragment;
import com.memoir.ui.fragment.TripFragment;

public class MainActivity extends FragmentActivity {
	public ViewPager mViewPager;

	private enum Page {
		All, TRIP, RESTAURENT, PLACE, HOTEL, FLIGHT
	}

	private static Page[] pageValues = Page.values();

	private static final int LOADER_ID = 1;

	private final ContentObserver Observer = new ContentObserver(new Handler()) {
		@Override
		public void onChange(boolean selfChange) {

			Loader<Object> loader = getLoaderManager().getLoader(1);
			if (loader != null) {
				loader.forceLoad();
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_pager);

		ActionBar actionBar = getActionBar();
		actionBar.show();

		final FragmentActivity fragmentActivity = this;

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(new FragmentStatePagerAdapter(
				getSupportFragmentManager()) {
			@Override
			public Fragment getItem(int i) {
				Fragment fragment = null;

				switch (pageValues[i]) {
				case All:
					fragment = new TripFragment();
					break;

				case TRIP:
					fragment = new TripFragment();
					break;

				case RESTAURENT:
					fragment = new RestaurantFragment();
					break;
				case PLACE:
					fragment = new PlaceFragment();
					break;

				case HOTEL:
					fragment = new HotelFragment();
					break;

				case FLIGHT:
					fragment = new FlightFragment();
					break;
				}
				return fragment;

			}

			@Override
			public int getItemPosition(Object object) {
				return POSITION_NONE;
			}

			@Override
			public int getCount() {
				return pageValues.length;
			}

			@Override
			public CharSequence getPageTitle(int i) {
				switch (pageValues[i]) {
				case All:
					return fragmentActivity.getString(R.string.all);
				case TRIP:
					return fragmentActivity.getString(R.string.trip);
				case RESTAURENT:
					return fragmentActivity.getString(R.string.restaurent);
				case PLACE:
					return fragmentActivity.getString(R.string.place);
				case HOTEL:
					return fragmentActivity.getString(R.string.hotel);
				case FLIGHT:
					return fragmentActivity.getString(R.string.flight);
				default:
					return null;
				}
			}

			@Override
			public void notifyDataSetChanged() {
				// TODO Auto-generated method stub
				super.notifyDataSetChanged();
			}

			@Override
			public boolean isViewFromObject(View view, Object object) {
				// TODO Auto-generated method stub
				return super.isViewFromObject(view, object);
			}

			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				// TODO Auto-generated method stub
				super.destroyItem(container, position, object);
			}

		});

		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {

				// mViewPager.getAdapter().notifyDataSetChanged();
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// mViewPager.getAdapter().notifyDataSetChanged();

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// mViewPager.getAdapter().notifyDataSetChanged();

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		if (id == R.id.action_plus_add) {
			Intent intent = new Intent(MainActivity.this, AddNewEntry.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		this.getContentResolver().unregisterContentObserver(Observer);
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
}
