package com.hackaton.ne4istb.antifastfood;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import java.util.ArrayList;

public class CurrentLocationListener implements LocationListener {

    public static final int UPDATE_PERIOD = 30 * 1000;
    private final LocationManager locationManager;
    Context context;

    public CurrentLocationListener(Context context) {
        this.context = context;

        locationManager = (LocationManager)
                context.getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void onLocationChanged(Location location) {

        String message = Double.toString(location.getLatitude()) + ' ' +Double.toString(location.getLongitude());
        setDebugNotification(message);

        ArrayList<Coordinate> fastFoodLocations = getFastFoodLocations();

        for (int i = 0 ; i < fastFoodLocations.size(); i++){
            setAlertOnLocationEnter(fastFoodLocations.get(i));
        }

    }

    private void setAlertOnLocationEnter(Coordinate coordinate) {
        PendingIntent onAreaEnterIntent =
                PendingIntent.getService(context, 0, new Intent(context, OnAreaEnterIntentService.class), PendingIntent.FLAG_UPDATE_CURRENT);

        locationManager.addProximityAlert(coordinate.latitude, coordinate.longtitude, 50, UPDATE_PERIOD * 3, onAreaEnterIntent);
    }

    private ArrayList getFastFoodLocations() {

        ArrayList<Coordinate> coordinates = new ArrayList<Coordinate>();

        coordinates.add(new Coordinate(50.2345, 50.5432));
        coordinates.add(new Coordinate(40.2345, 50.5432));
        coordinates.add(new Coordinate(50.0045, 45.5432));

        return coordinates;
    }

    private void setDebugNotification(String message) {
        Notification.Builder notificationBuilder =  new Notification.Builder(context);

        PendingIntent openActivityIntent = PendingIntent.getActivity(context, 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        notificationBuilder
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("recheck")
                .setContentText(message)
                .setVibrate(new long[]{500, 500})
                .setSound(alarmSound)
                .setContentIntent(openActivityIntent)
                .setAutoCancel(true);

        NotificationManager nm = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        nm.notify(0, notificationBuilder.build());
    }

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    private class Coordinate {

        private double latitude;
        private double longtitude;

        public Coordinate(double latitude, double longtitude) {
            this.latitude = latitude;
            this.longtitude = longtitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongtitude() {
            return longtitude;
        }
    }
}