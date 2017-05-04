/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package app.com.example.ricard.sunshine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class DetailActivity extends ActionBarActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private String mLocation;
    private String DETAILFRAGMENT_TAG = "DFTAG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocation = Utility.getPreferredLocation(this);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {

            Bundle arguments = new Bundle();
            arguments.putString("uriDate", getIntent().getData().toString());
            detailFragment fragment = new detailFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.weather_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        }

    }

    @Override
    public void onResume(){

        String location = Utility.getPreferredLocation(this);
        if(location != null && !location.equals(mLocation) ){

            detailFragment df = (detailFragment)getSupportFragmentManager().findFragmentByTag(DETAILFRAGMENT_TAG);
            if(null != df) {
                df.onLocationChanged(location);
            }

            mLocation = Utility.getPreferredLocation(this);
        }
        super.onResume();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id) {
            case R.id.action_settings:
                Intent settingsViewAct = new Intent(this, SettingsActivity.class);
                startActivity(settingsViewAct);
                break;

            case R.id.action_location_view:
                viewLocation();
                break;

        }



        return super.onOptionsItemSelected(item);
    }


    public void viewLocation (){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String city = sharedPref.getString(getString(R.string.location_preference_key),getString(R.string.location_preference_default_value));

        Uri geoLocation = Uri.parse("geo:0,0?").buildUpon()
                .appendQueryParameter("q", city)
                .build();
        Log.e(LOG_TAG, geoLocation.toString());
        Intent locationViewAct = new Intent(Intent.ACTION_VIEW);
        locationViewAct.setData(geoLocation);
        if (locationViewAct.resolveActivity(getPackageManager()) != null) {
            startActivity(locationViewAct);
        } else {
            Log.d(LOG_TAG, "Couldn't call " + city + ", no intent found");
        }
    }

}