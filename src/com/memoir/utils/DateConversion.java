package com.memoir.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.net.ParseException;

public class DateConversion {

	public Date stringToDate(String s_date) {
		// String dtStart = "2010-10-15T09:27:37Z";
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm",
				Locale.getDefault());
		Date date = null;
		try {

			try {
				date = (Date) format.parse(s_date);
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}

	public String dateToString(Date date) {
		SimpleDateFormat dateformat1 = new SimpleDateFormat("yyyy/MM/dd HH:mm",
				Locale.getDefault());
		String datetime = null;
		try {
			// Date date = new Date();
			datetime = dateformat1.format(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return datetime;
	}

}
