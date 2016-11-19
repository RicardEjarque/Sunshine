package app.com.example.ricard.sunshine;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;



public class MainActivity extends AppCompatActivity
        implements settingsFragment.SettingsSelectedListener {

    public String postalCode = "08027";
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    public void onArticleSelected(String sentPostalCode) {
        // Save the postal code into the variable
        postalCode = sentPostalCode;
        Log.v(LOG_TAG, "Your selected postalCode is:");
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction =  fragmentManager.beginTransaction();
            transaction.replace(R.id.container, new settingsFragment());
            transaction.addToBackStack(null);
            transaction.commit();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            return true;
        }

        if (id == android.R.id.home){
            Log.e(LOG_TAG, "BACK BUTTON PRESSED FROM MAIN");
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        super.onBackPressed();
    }



}