package com.hackaton.ne4istb.antifastfood;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CurrentLocationListener implements LocationListener {

    public static final int UPDATE_PERIOD = 15 * 60 * 1000;
    private static final String PROX_ALERT_INTENT = "TEST";
    public static final String FORSQUARE_SEARCH_URL = "https://api.foursquare.com/v2/venues/search?client_id=YQURAAEAW4SVCMUM4TZVPDQZRNDFU2SG4CVW0SXCKNMQ2321&client_secret=VGCVXSSKNWGIQLHMOOB5VLP1DO55ZDMMW1EI2A0KELMMWYMG&v=20140832%20";
    private LocationManager locationManager;

    Context context;

    public CurrentLocationListener(Context context) {
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void onLocationChanged(Location location) {

//        String message = Double.toString(location.getLatitude()) + ' ' + Double.toString(location.getLongitude());
//        setDebugNotification(message);

        new ForsquareAsyncTask().execute(location.getLatitude(),location.getLongitude());
    }

    public class ForsquareAsyncTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {

            Double latitude = (Double) params[0];
            Double longitude = (Double) params[1];

            List<Coordinate> fastFoodLocations = getFastFoodLocations(latitude, longitude);

            for (int i = 0; i < fastFoodLocations.size(); i++) {
                setAlertOnLocationEnter(fastFoodLocations.get(i));
            }

            return null;
        }

        private List<Coordinate> getFastFoodLocations(Double latitude, Double longitude) {

            List<Coordinate> coordinates = new ArrayList<Coordinate>();

            String urlString = FORSQUARE_SEARCH_URL + "&ll=" + Double.toString(latitude) + "," + Double.toString(longitude) + "&radius=3000&categoryId=4bf58dd8d48988d16e941735";

            JSONObject entity = HttpHelper.HttpGet(urlString);

            if (entity != null) {
                try {
                    coordinates = parseResult(entity);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return coordinates;
        }

        private List<Coordinate> parseResult(JSONObject entity) throws IOException, JSONException {

            List<Coordinate> coordinates = new ArrayList<Coordinate>();

            JSONObject responseJson = entity.getJSONObject("response");
            JSONArray venuesJson = responseJson.getJSONArray("venues");

            for (int i = 0; i < venuesJson.length(); i++) {
                JSONObject venueJson = venuesJson.getJSONObject(i);
                JSONObject locationJSON = venueJson.getJSONObject("location");
                Double latitude = locationJSON.getDouble("lat");
                Double longitude = locationJSON.getDouble("lng");

                coordinates.add(new Coordinate(latitude, longitude));
            }

            return coordinates;
        }

        private void setAlertOnLocationEnter(Coordinate coordinate) {

            Intent intent = new Intent(PROX_ALERT_INTENT);
            PendingIntent proximityIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

            locationManager.addProximityAlert(coordinate.getLatitude(), coordinate.getLongtitude(), 100, CurrentLocationListener.UPDATE_PERIOD*2, proximityIntent);

            IntentFilter filter = new IntentFilter(PROX_ALERT_INTENT);
            context.registerReceiver(new OnAreaEnterReceiver(), filter);
        }
    }

    private void setDebugNotification(String message) {
        Notification.Builder notificationBuilder = new Notification.Builder(context);

        PendingIntent openActivityIntent = PendingIntent.getActivity(context, 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        notificationBuilder
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("recheck")
                .setContentText(message)
                .setVibrate(new long[]{500, 500})
//                .setSound(alarmSound)
                .setContentIntent(openActivityIntent)
                .setAutoCancel(true);

        NotificationManager nm = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        nm.notify(1, notificationBuilder.build());
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
}
