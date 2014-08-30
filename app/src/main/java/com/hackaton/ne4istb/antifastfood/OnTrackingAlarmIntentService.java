package com.hackaton.ne4istb.antifastfood;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

public class OnTrackingAlarmIntentService extends IntentService {

    public static final String ALARM_INTENT_SERVICE = "OnTrackingAlarmIntentService";

    public OnTrackingAlarmIntentService() {
        super(ALARM_INTENT_SERVICE);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        processAlarm();
    }

    private void processAlarm() {

        Intent geolocationService = new Intent(new TrackingService(this).context, GeolocationService.class);
        new TrackingService(this).context.startService(geolocationService);
    }


}




















