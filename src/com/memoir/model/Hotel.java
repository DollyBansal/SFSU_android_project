package com.memoir.model;

import java.util.Date;

import android.net.Uri;
import android.provider.BaseColumns;

import com.memoir.provider.DatabaseHelper;
import com.memoir.provider.DatabaseHelper.Tables;

public class Hotel {

	public int id;
	public Date hotel_date;
	public String hotel_name;
	public String hotel_adress;
	public String hotel_likeOrNot;
	public String hotel_comments;
	public String image_name;
	public byte[] image;

	interface HotelColumns {

		String ID = "id";
		String Date = "hotel_date";
		String Name = "hotel_name";
		String Adress = "hotel_adress";
		String LikeOrNot = "hotel_like";
		String Comment = "hotel_comment";
		String Image_type = "image_type";
		String ImageUrl = "image_url";

	}

	public static class Hotels implements HotelColumns, BaseColumns {
		public static final String CONTENT_TYPE = "vnd.android.cursor.item/vnd.memoir.hotel";
		public static final Uri CONTENT_URI = DatabaseHelper.BASE_CONTENT_URI
				.buildUpon().appendPath("hotel").build();
		public static final String BY_Hotel_Name = Tables.HOTEL + "."
				+ Hotels.Name + " = ?";;

		public static Uri buildHotelUri(String HotelId) {
			return CONTENT_URI.buildUpon().appendPath(HotelId).build();
		}
	}

}
