package com.hackaton.ne4istb.antifastfood;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

public class OnAreaEnterIntentService extends IntentService {

    public static final String ALARM_INTENT_SERVICE = "OnTrackingAlarmIntentService";

    public OnAreaEnterIntentService() {
        super(ALARM_INTENT_SERVICE);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        processAlarm();
    }

    private void processAlarm() {
        Log.e("GEO", "catch it");
        showWarningNotification();
    }

    private void showWarningNotification() {
        Notification.Builder notificationBuilder =  new Notification.Builder(this);

        PendingIntent openActivityIntent = PendingIntent.getActivity(this, 1, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        notificationBuilder
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Убегай!")
                .setContentText("Ты возле фастфуда. Не вздумай в него зайти!")
                .setVibrate(new long[]{500, 500})
//                .setSound(alarmSound)
                .setContentIntent(openActivityIntent)
                .setAutoCancel(true);

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(0, notificationBuilder.build());
    }
}




















