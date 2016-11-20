package app.com.example.ricard.sunshine;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by Ricard on 14/11/2016.
 */

public class forecastFragment extends Fragment {

    public forecastFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        String[] forecastStrings  = {
                "Today-Sunny-88/63",
                "Tomorrow-Sunny-88/63",
                "Tue-Sunny-88/63",
                "Wed-Sunny-88/63",
                "Thu-Sunny-88/63",
                "Fri-Sunny-88/63",
                "Sat-Sunny-88/63",
                "Tue-Sunny-88/63",
                "Wed-Sunny-88/63",
                "Thu-Sunny-88/63",
                "Fri-Sunny-88/63",
                "Sat-Sunny-88/63",
                "Sat-Sunny-88/63"
        };
        ArrayList<String> forecast = new ArrayList<String>(
                Arrays.asList(forecastStrings));
        ArrayAdapter<String> mForecastAdapter;
        mForecastAdapter =
                new ArrayAdapter<String> (
                        getActivity(),
                        R.layout.list_item_forecast,
                        R.id.list_item_forecast_textview,
                        forecast);

        ListView mlistview = (ListView) rootView.findViewById(R.id.listview_forecast);
        mlistview.setAdapter(mForecastAdapter);

        return rootView;
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
        inflater.inflate(R.menu.forecastfragment, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id) {
            case R.id.action_refresh:
                FetchWeatherTask weatherTask = new FetchWeatherTask();
                String city = "Barcelona";
                weatherTask.execute(city, null, null);
                break;
        }

           return super.onOptionsItemSelected(item);
    }

    public class FetchWeatherTask extends AsyncTask <String,Void,Void>{

        private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

        @Override
        protected Void doInBackground(String... params) {

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            String city;
            city = params[0];
            Log.e(LOG_TAG,"algo");
            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are available at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast

                Uri.Builder builder = new Uri.Builder();
                builder.scheme("http")
                    .authority("api.openweathermap.org")
                        .appendPath("data")
                        .appendPath("2.5")
                        .appendPath("forecast")
                        .appendPath("daily")
                        .appendQueryParameter("q",city)
                        .appendQueryParameter("724&cnt","7")
                        .appendQueryParameter("APPID","***REMOVED***")
                        .appendQueryParameter("mode","json")
                        .appendQueryParameter("units","metric");
                String myUrlString = builder.build().toString();
                Log.e(LOG_TAG, myUrlString);
                URL url = new URL(myUrlString);

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();

                Log.v(LOG_TAG, "Forecast JSON string:"+ forecastJsonStr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
               return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }

            }
         return null;
        }

    }
}
