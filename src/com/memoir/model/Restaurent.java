package com.memoir.model;

import java.util.Date;

import android.net.Uri;
import android.provider.BaseColumns;

import com.memoir.provider.DatabaseHelper;
import com.memoir.provider.DatabaseHelper.Tables;

public class Restaurent {

	public int id;
	public Date rest_date;
	public String rest_name;
	public String rest_adress;
	public String rest_likeOrNot;
	public String rest_comments;
	public String image_name;
	public byte[] image;

	interface RestaurentColumns {

		String ID = "id";
		String Date = "rest_date";
		String Name = "rest_name";
		String Adress = "rest_adress";
		String LikeOrNot = "rest_like";
		String Comment = "rest_comment";
		String Image_type = "image_type";
		String ImageUrl = "image_url";

	}

	public static class Restaurents implements RestaurentColumns, BaseColumns {
		public static final String CONTENT_TYPE = "vnd.android.cursor.item/vnd.memoir.restaurent";
		public static final Uri CONTENT_URI = DatabaseHelper.BASE_CONTENT_URI
				.buildUpon().appendPath("restaurent").build();
		public static final String BY_Restaurent_Name = Tables.RESTAURENT + "."
				+ Restaurents.Name + " = ?";;

		public static Uri buildRestaurentUri(String RestaurentId) {
			return CONTENT_URI.buildUpon().appendPath(RestaurentId).build();
		}
	}

}
