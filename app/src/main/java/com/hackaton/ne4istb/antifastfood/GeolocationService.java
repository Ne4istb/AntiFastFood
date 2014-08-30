package com.hackaton.ne4istb.antifastfood;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationStatusCodes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GeolocationService extends Service implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener
//        LocationClient.OnAddGeofencesResultListener,
//        LocationClient.OnRemoveGeofencesResultListener
{

    public static final String EXTRA_REQUEST_IDS = "requestId";
    public static final String EXTRA_GEOFENCE = "geofence";
    public static final String EXTRA_ACTION = "action";

    private List<Geofence> mGeofenceListsToAdd = new ArrayList<Geofence>();
    private List<String> mGeofenceListsToRemove = new ArrayList<String>();
    private LocationClient mLocationClient;
    private Action mAction;

    public static enum Action implements Serializable {ADD, REMOVE};

    @Override
    public void onCreate() {
        super.onCreate();
        mLocationClient = new LocationClient(this, this, this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        // Check Google Play Service Available


        mLocationClient = new LocationClient(this, this, this);
        mLocationClient.connect();

        return super.onStartCommand(intent, flags, startId);
    }

    private void setDebugNotification(String message) {
        Notification.Builder notificationBuilder =  new Notification.Builder(getApplicationContext());

        PendingIntent openActivityIntent = PendingIntent.getActivity(this, 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        notificationBuilder
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("recheck")
                .setContentText(message)
                .setVibrate(new long[]{500, 500})
                .setSound(alarmSound)
                .setContentIntent(openActivityIntent)
                .setAutoCancel(true);

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(0, notificationBuilder.build());
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("GEO", "Location client connected");

        Location location = mLocationClient.getLastLocation();

        String message = Double.toString(location.getLatitude()) + ' ' +Double.toString(location.getAltitude());
        setDebugNotification(message);
    }

    @Override
    public void onDisconnected() {
        mLocationClient.disconnect();
        Log.d("GEO", "Location client disconnected");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e("GEO", "Location client connection failed: " + connectionResult.getErrorCode());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Log.d("GEO", "Location service destroyed");
        super.onDestroy();
    }
}