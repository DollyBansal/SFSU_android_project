package com.memoir.model;

import java.util.Date;

import android.net.Uri;
import android.provider.BaseColumns;

import com.memoir.provider.DatabaseHelper;
import com.memoir.provider.DatabaseHelper.Tables;

public class Trip {

	public int id;
	public Date trip_date;
	public String trip_name;
	public String trip_adress;
	public String trip_likeOrNot;
	public String trip_comments;
	public String image_name;
	public byte[] image;

	interface TripColumns {

		String ID = "id";
		String Date = "trip_date";
		String Name = "trip_name";
		String Adress = "trip_adress";
		String LikeOrNot = "trip_like";
		String Comment = "trip_comment";
		String Image_type = "image_type";
		String ImageUrl = "image_url";

	}

	public static class Trips implements TripColumns, BaseColumns {
		public static final String CONTENT_TYPE = "vnd.android.cursor.item/vnd.memoir.trip";
		public static final Uri CONTENT_URI = DatabaseHelper.BASE_CONTENT_URI
				.buildUpon().appendPath("trip").build();
		public static final String BY_Trip_Name = Tables.TRIP + "."
				+ Trips.Name + " = ?";;

		public static Uri buildTripUri(String TripId) {
			return CONTENT_URI.buildUpon().appendPath(TripId).build();
		}
	}

}
