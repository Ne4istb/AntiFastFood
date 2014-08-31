package com.hackaton.ne4istb.antifastfood;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class SuggestionActivity extends Activity {

    private SuggestionAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);

        setTitle(R.string.suggestions_title);

        mAdapter = new SuggestionAdapter(this);

        setItems();

        ListView listView = (ListView) findViewById(R.id.fragment_list);
        listView.setAdapter(mAdapter);
    }

    private void setItems() {

        SuggestionsDownloader downloader = new SuggestionsDownloader();
        downloader.execute();
        List<SuggestionRecord> suggestions = null;

        try {
            suggestions = downloader.get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < suggestions.size(); i++)
            mAdapter.add(suggestions.get(i));
    }

    private List<SuggestionRecord> getSuggestions() {
        return null;
    }

    private class SuggestionsDownloader extends AsyncTask<Object, Object, List<SuggestionRecord>> {

        String[] categories = new String[]{
                "50aa9e744b90af0d42d5de0e", //Health Food Store
                "52f2ab2ebcbc57f1066b8b1c", //Fruit & Vegetable Store
                "4bf58dd8d48988d1d3941735", //Vegetarian / Vegan Restaurant
                "52f2ab2ebcbc57f1066b8b16", //Fishing Store
        };

        @Override
        protected List<SuggestionRecord> doInBackground(Object[] params) {

            Location location = getLastLocation();
            if (location == null)
                return null;

            double longitude = location.getLongitude();
            double latitude = location.getLatitude();

            List<SuggestionRecord> suggestions = new ArrayList<SuggestionRecord>();

            for (int i = 0; i < categories.length; i++) {
                suggestions.addAll(getSugestionsByCategoryId(latitude, longitude, categories[i]));
            }

            Collections.sort(suggestions, new Comparator<SuggestionRecord>() {
                @Override
                public int compare(SuggestionRecord lhs, SuggestionRecord rhs) {
                    if (lhs.getDistance() == rhs.getDistance())
                        return 0;
                    if (lhs.getDistance() > rhs.getDistance())
                        return 1;

                    return -1;
                }
            });

            return suggestions;
        }

        private List<SuggestionRecord> getSugestionsByCategoryId(double latitude, double longitude, String categoryId) {

            String urlString = CurrentLocationListener.FORSQUARE_SEARCH_URL
                    + "&ll=" + Double.toString(latitude)
                    + "," + Double.toString(longitude)
                    + "&radius=3000"
                    + "&categoryId=" + categoryId;

            List<SuggestionRecord> suggestions = new ArrayList<SuggestionRecord>();

            JSONObject entity = HttpHelper.HttpGet(urlString);

            if (entity != null) {
                try {
                    suggestions = parseResult(entity);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return suggestions;
        }

        private List<SuggestionRecord> parseResult(JSONObject entity) throws
                IOException, JSONException {

            List<SuggestionRecord> suggestions = new ArrayList<SuggestionRecord>();

            JSONObject responseJson = entity.getJSONObject("response");
            JSONArray venuesJson = responseJson.getJSONArray("venues");

            for (int i = 0; i < venuesJson.length(); i++) {
                JSONObject venueJson = venuesJson.getJSONObject(i);

                String name = venueJson.getString("name");

                String url = "";
                if (venueJson.has("url"))
                    url = venueJson.getString("url");

                JSONObject locationJSON = venueJson.getJSONObject("location");

                String address = "";
                if (locationJSON.has("address"))
                    address = locationJSON.getString("address");

                Double latitude = locationJSON.getDouble("lat");
                Double longitude = locationJSON.getDouble("lng");
                Integer distance = locationJSON.getInt("distance");

                suggestions.add(new SuggestionRecord(name, address, url, new Coordinate(latitude, longitude), distance));
            }

            return suggestions;
        }

        private Location getLastLocation() {

            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            Location gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (gpsLocation != null)
                return gpsLocation;

            Location networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (networkLocation != null)
                return networkLocation;

            return null;
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.suggestion, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
