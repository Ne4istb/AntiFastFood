package com.hackaton.ne4istb.antifastfood;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;


public class SuggestionActivity extends Activity {

    private SuggestionAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);

        mAdapter = new SuggestionAdapter(this);

        setItems();

        ListView listView = (ListView) findViewById(R.id.fragment_list);
        listView.setAdapter(mAdapter);
    }

    private void setItems() {
        mAdapter.add(new SuggestionRecord("Test name 1", "Address 1", "Site 1", new Coordinate(50.354, 23.245)));
        mAdapter.add(new SuggestionRecord("Test name 2", "Address 2", "Site 2", new Coordinate(50.354, 23.245)));
        mAdapter.add(new SuggestionRecord("Test name 3", "Address 3", "Site 3", new Coordinate(50.354, 23.245)));
        mAdapter.add(new SuggestionRecord("Test name 4", "Address 4", "Site 4", new Coordinate(50.354, 23.245)));
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
