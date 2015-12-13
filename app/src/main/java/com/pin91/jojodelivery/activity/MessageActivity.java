package com.pin91.jojodelivery.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.pin91.jojodelivery.R;


public class MessageActivity extends Activity {
	MediaPlayer player = null;
	Vibrator vibrator = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.message_alert);

		Bundle bundle = getIntent().getExtras();
		Uri notification = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		player = MediaPlayer.create(this, notification);
		player.setLooping(true);
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		TextView textView = (TextView) findViewById(R.id.showAlertMessage);
		String text = bundle.getString("default");
	
		if (text != null && !text.equalsIgnoreCase("")) {
			textView.setText(Html.fromHtml(text));
			vibrator.vibrate(500);
			player.start();
		}
	}

	public void stop(View view) {
		vibrator.cancel();
		player.stop();
		Intent intent = new Intent(this, JojoDeliveryActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();

	}

}
