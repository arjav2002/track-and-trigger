package com.oopcows.trackandtrigger.helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Toast;

public class CustomAlarmReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        try {
            sendSMS(intent);
        } catch (Exception e) {
            Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void sendSMS(Intent intent){
        Bundle bundle = intent.getExtras();
        SmsManager smsManager = SmsManager.getDefault();

        String smsText = bundle.getString("alarm_message");
        String smsNumber = bundle.getString("number");

        smsManager.sendTextMessage(smsNumber, null, smsText, null, null);

    }
}