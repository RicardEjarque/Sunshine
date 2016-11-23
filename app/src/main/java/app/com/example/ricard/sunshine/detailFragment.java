package app.com.example.ricard.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Ricard on 23/11/2016.
 */

public class detailFragment extends Fragment {
    public detailFragment() {
    }

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private String forecastStr;
    private String FORECAST_SHARE_HASHTAG = "\u0020#SunshineApp";


    private Intent createShareForecastIntent(){
        Intent LocationWeatherAct = new Intent(Intent.ACTION_SEND);

        LocationWeatherAct.setAction(Intent.ACTION_SEND);
        LocationWeatherAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        LocationWeatherAct.setType("text/plain");
        LocationWeatherAct.putExtra(Intent.EXTRA_TEXT,forecastStr+FORECAST_SHARE_HASHTAG);
        return LocationWeatherAct;
    }

    public void shareLocationWeather() {


        Intent LocationWeatherAct = createShareForecastIntent();

        if (LocationWeatherAct.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(LocationWeatherAct);
        } else {
            Log.d(LOG_TAG, "Couldn't call " + ", no intent found");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.detail, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.action_share);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_share) {
            shareLocationWeather();
            Log.e(LOG_TAG, "Couldn't call " + ", no intent found");
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            forecastStr = intent.getStringExtra(Intent.EXTRA_TEXT);
            ((TextView) rootView.findViewById(R.id.textView)).setText(forecastStr);
        }


        return rootView;
    }


    private ShareActionProvider mShareActionProvider;


    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }
}

