package com.example.a310270843.alarm_test;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class  MainReciever extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        String text = intent.getStringExtra("text");
        MainActivity.log("recieved:"+text);
        Notification noti = new Notification();
        noti.tickerText = text;
        MainActivity.nm.notify(0, noti);
    }
}
