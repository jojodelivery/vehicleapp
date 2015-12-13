package com.pin91.jojovehicleapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.pin91.jojovehicleapp.service.MessageReceivingService;


public class ExternalReceiver extends BroadcastReceiver {

	public void onReceive(Context context, Intent intent) {
		MessageReceivingService messageReceivingService = new MessageReceivingService();
		if (intent != null) {
			Bundle extras = intent.getExtras();
			messageReceivingService.sendToApp(extras, context);
		}
	}
}
