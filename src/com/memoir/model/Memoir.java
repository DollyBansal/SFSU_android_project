package com.memoir.model;

import java.util.Date;

import android.net.Uri;
import android.provider.BaseColumns;

import com.memoir.provider.DatabaseHelper;
import com.memoir.provider.DatabaseHelper.Tables;

public class Memoir {

	public int id;
	public String type;
	public String trip_name;
	public String name;
	public Date start_date;
	public Date end_date;
	public String address;
	public String flight_from;
	public String flight_to;
	public String likeOrNot;
	public String comments;
	public String image_name;
	public byte[] image;

	interface MemoirColumns {

		String ID = "id";
		String TYPE = "type";
		String TRIP_NAME = "trip_name";
		String Name = "name";
		String Start_Date = "start_date";
		String End_Date = "end_date";
		String Address = "address";
		String FlightFrom = "flight_from";
		String FlightTo = "flight_to";
		String LikeOrNot = "like";
		String Comment = "comment";
		String Image_type = "image_type";
		String ImageUrl = "image_url";

	}

	public static class Memoirs implements MemoirColumns, BaseColumns {
		public static final String CONTENT_TYPE = "vnd.android.cursor.item/vnd.memoir.memoir";
		public static final Uri CONTENT_URI = DatabaseHelper.BASE_CONTENT_URI
				.buildUpon().appendPath("memoir").build();
		public static final String BY_Name = Tables.MEMOIR + "." + Memoirs.Name
				+ " = ?";;
		public static final String BY_ID = Tables.MEMOIR + "." + Memoirs._ID
				+ " = ?";;
		public static final String BY_Type = Tables.MEMOIR + "." + Memoirs.TYPE
				+ " = ?";;
		public static final String BY_Trip_Name = Tables.MEMOIR + "."
				+ Memoirs.TRIP_NAME + " = ?";;

		public static Uri buildUri(String Id) {
			return CONTENT_URI.buildUpon().appendPath(Id).build();
		}
	}

}
