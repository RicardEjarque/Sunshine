package app.com.example.ricard.sunshine;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;



public class MainActivity extends AppCompatActivity
        implements settingsFragment.SettingsSelectedListener {

    public CommonHelpers SettingsManager;

    public String cityName = "Barcelona";
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    public void onArticleSelected(String cityName) {
        // Save the postal code into the variable
        //postalCode = sentPostalCode;

        cityName = SettingsManager.readPreference(this, "CITY");
        Log.v(LOG_TAG, "Your selected city is:"+ cityName);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new forecastFragment())
                    .commit();
        }
        SettingsManager = new CommonHelpers();
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

        final Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.actionSettingsFragment);
        if(currentFragment!= null){
        Log.e(LOG_TAG, String.valueOf(currentFragment.onOptionsItemSelected(item)));}
        if (currentFragment != null && currentFragment.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_settings:
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction =  fragmentManager.beginTransaction();
                transaction.replace(R.id.container, new settingsFragment());
                transaction.addToBackStack(null);
                transaction.commit();
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                return true;
            case android.R.id.home:
                Log.e(LOG_TAG, "BACK BUTTON PRESSED FROM MAIN");
                onBackPressed();
                return true;
            default:
                break;
        }

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        super.onBackPressed();
    }



}