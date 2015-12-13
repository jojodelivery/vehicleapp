/**
 * 
 */
package com.pin91.jojovehicleapp.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * @author Debdutta Bhattacharya
 * 
 */
public class DTSCommonUtil {

	public static boolean disConnectIncomingCall = false;
	public static boolean inComingCall = false;
	public static boolean callConnectionDissconnected = false;
	private static ProgressDialog progressBar;

	public static void showProgressBar(Context context) {
		progressBar = new ProgressDialog(context);
		progressBar.setCancelable(false);
		progressBar.setMessage("Please Wait ...");
		// progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

		progressBar.show();
	}

	public static String showDateInFormat(String date) {
		DateFormat dateFormatter = new SimpleDateFormat("MMMM, yyyy");
		String dateValue = null;
		Date d = null;
		if (date != null) {
			try {
				d = stringToDateConversion_1(date);
				dateValue = dateFormatter.format(d);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return dateValue;
		} else {
			return null;
		}

	}

	public static Date stringToDateConversion_1(String date) {
		SimpleDateFormat formatter = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
		if (date != null && date != "") {
			Date d = null;
			try {
				d = formatter.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return d;
		} else {
			return null;
		}
	}

	public static Date stringToDateConversion(String date) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		if (date != null && date != "") {
			Date d = null;
			try {
				d = formatter.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return d;
		} else {
			return null;
		}
	}
	public static String stringToDateConversion_3(String date) {
		Date d=stringToDateConversion_2(date);
		String dateValue;
		if (date != null && date != "") {
			DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
			dateValue = dateFormatter.format(d);
			return dateValue;
		} else {
			return null;
		}
	}
	
	public static Date stringToDateConversion_2(String date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		if (date != null && date != "") {
			Date d = null;
			try {
				d = formatter.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return d;
		} else {
			return null;
		}
	}
	public static void showClosableProgressBar(Context context) {
		progressBar = new ProgressDialog(context);
		progressBar.setCancelable(true);
		progressBar.setMessage("Please Wait ...");
		// progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

		progressBar.show();
	}

	public static void closeProgressBar() {
		if (progressBar != null)
			progressBar.cancel();
	}
	
	public static void showAlertMessage(String title, String message,
			Context context) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);

		alertDialogBuilder.setTitle(title);

		alertDialogBuilder.setMessage(message).setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

		AlertDialog alertDialog = alertDialogBuilder.create();

		alertDialog.show();
	}

}
