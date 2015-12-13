package com.pin91.jojodelivery.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.pin91.jojodelivery.R;


public class JojoDeliveryActivity extends Activity {
	 public static Boolean inBackground = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.main_splash_screen);

		Thread background = new Thread() {
			public void run() {

				try {
					sleep(2 * 1000);
					Intent i = new Intent(getBaseContext(), LoginActivity.class);
					startActivity(i);
					finish();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		background.start();
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();

	}

	@Override
	public void onBackPressed() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				JojoDeliveryActivity.this);

		alertDialogBuilder.setTitle("ALERT");

		alertDialogBuilder
				.setMessage("Do you want to Exit?")
				.setCancelable(false)
				.setPositiveButton("YES",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
								Intent launchNextActivity = null;
								launchNextActivity = new Intent(
										Intent.ACTION_MAIN);
								launchNextActivity
										.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								launchNextActivity
										.addCategory(Intent.CATEGORY_HOME);

								startActivity(launchNextActivity);
								finish();
								System.exit(0);
							}
						})
				.setNegativeButton("NO", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						dialog.cancel();
					}
				});
		AlertDialog alertDialog = alertDialogBuilder.create();

		alertDialog.show();

	}

}
