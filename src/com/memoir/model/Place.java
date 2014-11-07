package com.memoir.model;

import java.util.Date;

import android.net.Uri;
import android.provider.BaseColumns;

import com.memoir.provider.DatabaseHelper;
import com.memoir.provider.DatabaseHelper.Tables;

public class Place {

	public int id;
	public Date place_date;
	public String place_name;
	public String place_adress;
	public String place_likeOrNot;
	public String place_comments;
	public String image_name;
	public byte[] image;

	interface PlaceColumns {

		String ID = "id";
		String Date = "place_date";
		String Name = "place_name";
		String Adress = "place_adress";
		String LikeOrNot = "place_like";
		String Comment = "place_comment";
		String Image_type = "image_type";
		String ImageUrl = "image_url";

	}

	public static class Places implements PlaceColumns, BaseColumns {
		public static final String CONTENT_TYPE = "vnd.android.cursor.item/vnd.memoir.place";
		public static final Uri CONTENT_URI = DatabaseHelper.BASE_CONTENT_URI
				.buildUpon().appendPath("place").build();
		public static final String BY_Place_Name = Tables.PLACE + "."
				+ Places.Name + " = ?";;

		public static Uri buildPlaceUri(String PlaceId) {
			return CONTENT_URI.buildUpon().appendPath(PlaceId).build();
		}
	}

}
