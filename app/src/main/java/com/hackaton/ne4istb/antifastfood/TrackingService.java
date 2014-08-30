package com.hackaton.ne4istb.antifastfood;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.widget.Toast;

import java.util.Calendar;

public class TrackingService {

    Context context;

    public TrackingService(Context context) {
        this.context = context;
    }

    public void Register() {

        Intent alarmService = new Intent(context, OnTrackingAlarmIntentService.class);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);

        PendingIntent intent = PendingIntent.getService(context, 0, alarmService, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_FIFTEEN_MINUTES, intent);
    }

}
