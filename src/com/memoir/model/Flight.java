package com.memoir.model;

import java.util.Date;

import android.net.Uri;
import android.provider.BaseColumns;

import com.memoir.provider.DatabaseHelper;
import com.memoir.provider.DatabaseHelper.Tables;

public class Flight {

	public int id;
	public Date flight_date;
	public String flight_name;
	public String flight_from;
	public String flight_to;
	public String flight_likeOrNot;
	public String flight_comments;
	public String image_name;
	public byte[] image;

	interface FlightColumns {

		String ID = "id";
		String Date = "flight_date";
		String Name = "flight_name";
		String Flight_From = "flight_from";
		String Flight_To = "flight_to";
		String LikeOrNot = "flight_like";
		String Comment = "flight_comment";
		String Image_type = "image_type";
		String ImageUrl = "image_url";

	}

	public static class Flights implements FlightColumns, BaseColumns {
		public static final String CONTENT_TYPE = "vnd.android.cursor.item/vnd.memoir.flight";
		public static final Uri CONTENT_URI = DatabaseHelper.BASE_CONTENT_URI
				.buildUpon().appendPath("flight").build();
		public static final String BY_Flight_Name = Tables.FLIGHT + "."
				+ Flights.Name + " = ?";;

		public static Uri buildFlightUri(String FlightId) {
			return CONTENT_URI.buildUpon().appendPath(FlightId).build();
		}
	}

}
