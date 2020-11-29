package com.oopcows.trackandtrigger.helpers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.oopcows.trackandtrigger.R;
import com.oopcows.trackandtrigger.dashboard.DashboardActivity;

public class CustomAlarmReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "my channel id";
    private NotificationChannel channel;
    private NotificationManager notificationManager;

    private static int notID = 0;

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void onReceive(Context context, Intent intent) {
        try {
            sendSMSAndNotify(context, intent);
        } catch (Exception e) {
            Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void sendSMSAndNotify(Context context, Intent intent){
        Bundle bundle = intent.getExtras();
        SmsManager smsManager = SmsManager.getDefault();

        String smsText = bundle.getString("alarm_message");
        String smsNumber = bundle.getString("number");

        smsManager.sendTextMessage(smsNumber, null, smsText, null, null);

        createNotificationChannel(context);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Track and Trigger")
                .setContentText(smsText)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
        notificationManager.notify(notID++, builder.build());
    }
}