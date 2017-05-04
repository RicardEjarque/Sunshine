package app.com.example.ricard.sunshine;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import app.com.example.ricard.sunshine.data.WeatherContract;


public class MainActivity extends AppCompatActivity implements forecastFragment.Callback {


    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private String mLocation;
    private boolean mTwoPane;
    private String DETAILFRAGMENT_TAG = "DFTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLocation = Utility.getPreferredLocation(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(findViewById(R.id.weather_detail_container)!=null){
            mTwoPane = true;

            if (savedInstanceState == null) {
                String locationSetting = Utility.getPreferredLocation(this);
                Uri dateUri = WeatherContract.WeatherEntry.buildWeatherLocationWithDate(locationSetting,System.currentTimeMillis());
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.weather_detail_container, detailFragment.newInstance(dateUri.toString()), DETAILFRAGMENT_TAG)
                        .commit();
            }

        } else{
            mTwoPane = false;
        }
        //if (savedInstanceState == null) {
        //    getSupportFragmentManager().beginTransaction()
        //            .add(R.id.container, new forecastFragment(), FORECASTFRAGMENT_TAG)
        //            .commit();
        //}

    }

    @Override
    public void onResume(){

        String location = Utility.getPreferredLocation(this);
        if(location != null && !location.equals(mLocation) ){
            forecastFragment ff = (forecastFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_forecast);
            if(null != ff) {
                ff.onLocationChanged();
            }
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

    public void onItemSelected(Uri dateUri){
        if (!mTwoPane){
            Intent intent = new Intent(this, DetailActivity.class).setData(dateUri);
            startActivity(intent);
        }else{


            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.weather_detail_container, detailFragment.newInstance(dateUri.toString()), DETAILFRAGMENT_TAG)
                    .commit();
        }
    }

}