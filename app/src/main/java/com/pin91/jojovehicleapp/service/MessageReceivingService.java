package com.pin91.jojovehicleapp.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.pin91.jojovehicleapp.activity.JojoDeliveryActivity;
import com.pin91.jojovehicleapp.activity.MessageActivity;


public class MessageReceivingService extends Service {

    public void sendToApp(Bundle extras, Context context) {
        if(extras.getString("default")==null ||(extras.getString("default")!=null && extras.getString("default").equals(""))){

        }else {
            Intent intent = new Intent(context, MessageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtras(extras);
            context.startActivity(intent);
        }
    }

    public void onCreate() {
        super.onCreate();
    }

    // Notification Manager Code
    /*
	 * protected void postNotification(Intent intentAction, Context context){
	 * 
	 * final NotificationManager mNotificationManager = (NotificationManager)
	 * context.getSystemService(Context.NOTIFICATION_SERVICE);
	 * 
	 * final PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
	 * intentAction, Notification.DEFAULT_LIGHTS |
	 * Notification.FLAG_AUTO_CANCEL); final Notification notification = new
	 * NotificationCompat.Builder(context).setSmallIcon(R.drawable.ic_launcher)
	 * .setContentTitle("Message Received!") .setContentText("")
	 * .setContentIntent(pendingIntent) .setAutoCancel(true) .getNotification();
	 * 
	 * mNotificationManager.notify(R.string.notification_no, notification); }
	 */

    public IBinder onBind(Intent arg0) {
        return null;
    }

}