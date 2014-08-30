package com.hackaton.ne4istb.antifastfood;

import android.app.IntentService;
import android.content.Intent;

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
//        Intent geolocationService = new Intent(new TrackingService(this).context, GeolocationService2.class);
//        new TrackingService(this).context.startService(geolocationService);
    }


}




















