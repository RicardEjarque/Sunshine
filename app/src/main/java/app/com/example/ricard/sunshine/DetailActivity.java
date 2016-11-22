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
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DetailActivity extends ActionBarActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_detail, new DetailFragment())
                    .commit();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingsViewAct = new Intent(this, SettingsActivity.class);
            startActivity(settingsViewAct);
        }
        if (id == R.id.action_location_view) {
            viewLocation();
        }

        if (id == R.id.menu_item_share) {
            shareLocationWeather();
            Log.d(LOG_TAG, "Couldn't call " + ", no intent found");
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

    public void shareLocationWeather (){

        Intent LocationWeatherAct = new Intent(Intent.ACTION_SEND);
        LocationWeatherAct.setType("text/*");
        Uri LocationWeUri = Uri.parse("test").buildUpon().build();

        LocationWeatherAct.putExtra(Intent.EXTRA_STREAM, LocationWeUri);

        if (LocationWeatherAct.resolveActivity(getPackageManager()) != null) {
            startActivity(LocationWeatherAct);
        } else {
            Log.d(LOG_TAG, "Couldn't call " + ", no intent found");
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class DetailFragment extends Fragment {

        public DetailFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

            Intent intent = getActivity().getIntent();
            if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
                String forecastStr = intent.getStringExtra(Intent.EXTRA_TEXT);
                ((TextView) rootView.findViewById(R.id.textView)).setText(forecastStr);
            }



            return rootView;
        }
    }

    private ShareActionProvider mShareActionProvider;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu resource file.
        getMenuInflater().inflate(R.menu.detail, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        // Return true to display menu
        return true;
    }

    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }


}