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


public class MainActivity extends AppCompatActivity         {


    private final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new forecastFragment())
                    .commit();
        }

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