package app.com.example.ricard.sunshine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static android.util.Log.e;


/**
 * Created by Ricard on 14/11/2016.
 */

public class forecastFragment extends Fragment {

    public forecastFragment() {
    }

    private ArrayAdapter<String> mForecastAdapter;
    private final String LOG_TAG = settingsFragment2.class.getSimpleName();
    public String[] weatherForecast;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);



        mForecastAdapter =
                new ArrayAdapter<String> (
                        getActivity(),
                        R.layout.list_item_forecast,
                        R.id.list_item_forecast_textview,
                        new ArrayList<String>());

        ListView mlistview = (ListView) rootView.findViewById(R.id.listview_forecast);
        mlistview.setAdapter(mForecastAdapter);

        mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CharSequence text = mForecastAdapter.getItem(position);
                int duration = Toast.LENGTH_SHORT;
                //Toast toast = Toast.makeText(getActivity(), text, duration);
                //toast.show();
                Intent detailsViewAct = new Intent(getActivity(), DetailActivity.class).putExtra(Intent.EXTRA_TEXT, text);
                startActivity(detailsViewAct);
            }
        });
        return rootView;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                // Add this line in order for this fragment to handle menu events.
                setHasOptionsMenu(true);


           }

    @Override
    public void onStart(){
        updatewWeather();
        super.onStart();
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.forecastfragment, menu);

    }

    private void updatewWeather(){
        FetchWeatherTask weatherTask = new FetchWeatherTask();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        String city = sharedPref.getString(getString(R.string.location_preference_key),getString(R.string.location_preference_default_value));
        weatherTask.execute(city);
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
                updatewWeather();
            break;
        }

           return super.onOptionsItemSelected(item);
    }

    public class FetchWeatherTask extends AsyncTask <String,Void,String[]>{

        private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

        public String[] weatherForecastParsed;

        @Override
        protected String[] doInBackground(String... params) {

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            final String SCHEME = "http";
            final String AUTHORITY = "api.openweathermap.org";
            final String DATA = "data";
            final String FORECAST = "forecast";
            final String FREQUENCY = "daily";
            final String CITY = "q";
            final String CITY_VALUE = params[0];
            final String NUM_DAYS = "724&cnt";
            final String NUM_DAYS_VALUE = "7";
            final String APPID = "APPID";
            final String APPID_VALUE = "***REMOVED***";
            final String MODE = "mode";
            final String MODE_VALUE = "json";
            final String UNITS = "units";
            final String UNITS_VALUE = "metric";

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are available at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast

                Uri.Builder builder = new Uri.Builder();
                builder.scheme(SCHEME)
                    .authority(AUTHORITY)
                        .appendPath(DATA)
                        .appendPath("2.5")
                        .appendPath(FORECAST)
                        .appendPath(FREQUENCY)
                        .appendQueryParameter(CITY,CITY_VALUE)
                        .appendQueryParameter(NUM_DAYS,NUM_DAYS_VALUE)
                        .appendQueryParameter(APPID,APPID_VALUE)
                        .appendQueryParameter(MODE,MODE_VALUE)
                        .appendQueryParameter(UNITS,UNITS_VALUE);
                String myUrlString = builder.build().toString();

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


            } catch (IOException e) {
                e(LOG_TAG, "Error ", e);
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
                        e(LOG_TAG, "Error closing stream", e);
                    }
                }
                int days = 7;
                try {
                    weatherForecastParsed = getWeatherDataFromJson(forecastJsonStr, days);

                }catch (JSONException e){
                    e(LOG_TAG, "Unable to get list!!!");
                }

            }

         return weatherForecastParsed;
        }

        @Override
        protected void onPostExecute(String[] strings) {
            if(strings != null) {

                mForecastAdapter.clear();
                mForecastAdapter.addAll(strings);
            }

        }

    }

    /* The date/time conversion code is going to be moved outside the asynctask later,
 * so for convenience we're breaking it out into its own method now.
 */
    private String getReadableDateString(long time){
        // Because the API returns a unix timestamp (measured in seconds),
        // it must be converted to milliseconds in order to be converted to valid date.
        SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
        return shortenedDateFormat.format(time);
    }

    /**
     * Prepare the weather high/lows for presentation.
     */
    private String formatHighLows(double high, double low) {
        // For presentation, assume the user doesn't care about tenths of a degree.
        long roundedHigh = Math.round(high);
        long roundedLow = Math.round(low);

        String highLowStr = roundedHigh + "/" + roundedLow;
        return highLowStr;
    }

    private double[] convertUnits(double high, double low) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String units = sharedPref.getString(getString(R.string.temp_units_preference_key),getString(R.string.temp_units_preference_default_value));

        double [] convertedUnits = {high, low};
        convertedUnits[0]= high;
        convertedUnits[1]= low;

        if (!(units.equals(getString(R.string.temp_units_preference_default_value)))){

            if (units.equals(R.string.temp_units_imperial_value)){
                        convertedUnits[0]=high*9/5+32;
                        convertedUnits[1]=low*9/5+32;
                } else if (units.equals(R.string.temp_units_metric_value)){
                    convertedUnits[0]=(high-32)*5/9;
                    convertedUnits[1]=(low-32)*5/9;
                } else {
                    Log.e(LOG_TAG, "Unrecongnized units!");
                }

        }

        return convertedUnits;
    }

    /**
     * Take the String representing the complete forecast in JSON Format and
     * pull out the data we need to construct the Strings needed for the wireframes.
     *
     * Fortunately parsing is easy:  constructor takes the JSON string and converts it
     * into an Object hierarchy for us.
     */
    private String[] getWeatherDataFromJson(String forecastJsonStr, int numDays)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String OWM_LIST = "list";
        final String OWM_WEATHER = "weather";
        final String OWM_TEMPERATURE = "temp";
        final String OWM_MAX = "max";
        final String OWM_MIN = "min";
        final String OWM_DESCRIPTION = "main";

        JSONObject forecastJson = new JSONObject(forecastJsonStr);
        JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);

        // OWM returns daily forecasts based upon the local time of the city that is being
        // asked for, which means that we need to know the GMT offset to translate this data
        // properly.

        // Since this data is also sent in-order and the first day is always the
        // current day, we're going to take advantage of that to get a nice
        // normalized UTC date for all of our weather.

        Time dayTime = new Time();
        dayTime.setToNow();

        // we start at the day returned by local time. Otherwise this is a mess.
        int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);

        // now we work exclusively in UTC
        dayTime = new Time();

        String[] resultStrs = new String[numDays];
        for(int i = 0; i < weatherArray.length(); i++) {
            // For now, using the format "Day, description, hi/low"
            String day;
            String description;
            String highAndLow;

            // Get the JSON object representing the day
            JSONObject dayForecast = weatherArray.getJSONObject(i);

            // The date/time is returned as a long.  We need to convert that
            // into something human-readable, since most people won't read "1400356800" as
            // "this saturday".
            long dateTime;
            // Cheating to convert this to UTC time, which is what we want anyhow
            dateTime = dayTime.setJulianDay(julianStartDay+i);
            day = getReadableDateString(dateTime);

            // description is in a child array called "weather", which is 1 element long.
            JSONObject weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
            description = weatherObject.getString(OWM_DESCRIPTION);

            // Temperatures are in a child object called "temp".  Try not to name variables
            // "temp" when working with temperature.  It confuses everybody.
            JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
            double high = temperatureObject.getDouble(OWM_MAX);
            double low = temperatureObject.getDouble(OWM_MIN);

            double highlow[] = convertUnits(high,low);
            highAndLow = formatHighLows(highlow[0], highlow[1]);
            resultStrs[i] = "   " + day + " - " + description + " - " + highAndLow;
        }


        return resultStrs;

    }
}
