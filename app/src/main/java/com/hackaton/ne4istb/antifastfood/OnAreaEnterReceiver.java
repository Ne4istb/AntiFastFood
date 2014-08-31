package com.hackaton.ne4istb.antifastfood;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;

public class OnAreaEnterReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String key = LocationManager.KEY_PROXIMITY_ENTERING;
        Boolean entering = intent.getBooleanExtra(key, false);

        if (entering)
            showWarningNotification(context);
    }

    private void showWarningNotification(Context context) {

        Notification.Builder notificationBuilder =  new Notification.Builder(context);

        Intent intent = new Intent(context, SuggestionActivity.class);

        PendingIntent openActivityIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        notificationBuilder
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(context.getString(R.string.run_away))
                .setContentText(context.getString(R.string.suggestion_notification))
                .setVibrate(new long[]{500, 500})
//                .setSound(alarmSound)
                .setContentIntent(openActivityIntent)
                .setAutoCancel(true);

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(1000, notificationBuilder.build());
    }
}




















