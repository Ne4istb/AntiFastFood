package com.hackaton.ne4istb.antifastfood;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;

public class GeolocationService extends Service {

    public static final int UPDATE_RADIUS = 3000;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new CurrentLocationListener(this);

        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, CurrentLocationListener.UPDATE_PERIOD, UPDATE_RADIUS, locationListener);

        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, CurrentLocationListener.UPDATE_PERIOD, UPDATE_RADIUS, locationListener);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
