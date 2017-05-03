package app.com.example.ricard.sunshine;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import app.com.example.ricard.sunshine.data.WeatherContract;

/**
 * Created by Ricard on 23/11/2016.
 */

public class detailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public detailFragment() {
    }
    private static final int DETAILS_LOADER = 1;

    private ViewHolder viewHolder;

    public static class ViewHolder{
        public final ImageView iconView;
        public final TextView dateView;
        public final TextView date2View;
        public final TextView forecastView;
        public final TextView highView;
        public final TextView lowView;
        public final TextView humidityView;
        public final TextView windView;
        public final TextView pressureView;




        public ViewHolder(View view){
            iconView = (ImageView) view.findViewById(R.id.list_item_icon);
            dateView = (TextView) view.findViewById(R.id.list_item_date_textview);
            date2View = (TextView) view.findViewById(R.id.list_item_date2_textview);
            forecastView = (TextView) view.findViewById(R.id.list_item_forecast_textview);
            highView = (TextView) view.findViewById(R.id.list_item_high_textview);
            lowView = (TextView) view.findViewById(R.id.list_item_low_textview);
            humidityView = (TextView) view.findViewById(R.id.list_item_humidity_textview);
            windView = (TextView) view.findViewById(R.id.list_item_wind_textview);
            pressureView = (TextView) view.findViewById(R.id.list_item_pressure_textview);
        }

    }

    private static final String[] FORECAST_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
            WeatherContract.WeatherEntry.COLUMN_PRESSURE,
            WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
            WeatherContract.WeatherEntry.COLUMN_DEGREES,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID
    };

    static final int COL_WEATHER_ID = 0;
    static final int COL_WEATHER_DATE = 1;
    static final int COL_WEATHER_DESC = 2;
    static final int COL_WEATHER_MAX_TEMP = 3;
    static final int COL_WEATHER_MIN_TEMP = 4;
    static final int COL_WEATHER_HUMIDITY = 5;
    static final int COL_WEATHER_PRESSURE = 6;
    static final int COL_WEATHER_WIND_SPEED = 7;
    static final int COL_WEATHER_DEGREES = 8;
    static final int COL_WEATHER_CONDITION_ID = 9;

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private String forecastStr;
    private String FORECAST_SHARE_HASHTAG = "\u0020#SunshineApp";
    private String mForecast;


    private Intent createShareForecastIntent(){
        Intent LocationWeatherAct = new Intent(Intent.ACTION_SEND);

        LocationWeatherAct.setAction(Intent.ACTION_SEND);
        LocationWeatherAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        LocationWeatherAct.setType("text/plain");
        LocationWeatherAct.putExtra(Intent.EXTRA_TEXT,mForecast+FORECAST_SHARE_HASHTAG);
        //Log.e(LOG_TAG,"paso porel createShareForecastIntent");
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

        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        // Fetch and store ShareActionProvider
        if(mShareActionProvider!=null && mForecast!=null) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }
         else {
                        Log.e(LOG_TAG, "Share Action Provider is null?");
        }
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
        viewHolder = new ViewHolder(rootView);

        Intent intent = getActivity().getIntent();
        if (intent != null) {
            forecastStr = intent.getDataString();

        }
        //mTextView = (TextView) rootView.findViewById(R.id.textView);


        return rootView;
    }

    @Override
    public Loader onCreateLoader(int i, Bundle bundle) {
       Intent intent = getActivity().getIntent();
        if(intent == null){
            return null;
        }

        return new CursorLoader(getActivity(),
                intent.getData(),
                FORECAST_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if(!cursor.moveToFirst()){
            return;
        }



        boolean isMetric = Utility.isMetric(getActivity());



        // Read weather icon ID from cursor
        int weatherId = cursor.getInt(COL_WEATHER_CONDITION_ID);
        // Use placeholder image for now


        viewHolder.iconView.setImageResource(Utility.getArtResourceForWeatherCondition(weatherId));

        // Read date from cursor
        Long dateinmillis = cursor.getLong(COL_WEATHER_DATE);
        String day = Utility.getDayName(getActivity(),dateinmillis);
        String monthDay = Utility.getFormattedMonthDay(getActivity(),dateinmillis);

        viewHolder.dateView.setText(day);
        viewHolder.date2View.setText(monthDay);

        // Read weather forecast from cursor
        String forecast = cursor.getString(COL_WEATHER_DESC);

        viewHolder.forecastView.setText(forecast);



        // Read high temperature from cursor
        double high = cursor.getDouble(COL_WEATHER_MAX_TEMP);

        viewHolder.highView.setText(Utility.formatTemperature(getActivity(),high, isMetric));

        // Read low temperature from cursor
        double low = cursor.getDouble(COL_WEATHER_MIN_TEMP);

        viewHolder.lowView.setText(Utility.formatTemperature(getActivity(),low, isMetric));

        double humidity = cursor.getDouble(COL_WEATHER_HUMIDITY);
        double pressure = cursor.getDouble(COL_WEATHER_PRESSURE);
        float windSpeed = cursor.getFloat(COL_WEATHER_WIND_SPEED);
        float degrees = cursor.getFloat(COL_WEATHER_DEGREES);

        viewHolder.windView.setText(Utility.getFormattedWind(getActivity(),windSpeed,degrees));
        viewHolder.humidityView.setText(String.format(getActivity().getString(R.string.format_humidity),humidity));
        viewHolder.pressureView.setText(String.format(getActivity().getString(R.string.format_pressure),pressure));

        //TextView detailTextView = (TextView) getView().findViewById(R.id.textView);
        //mForecast = String.format("%s - %s - %s/%s", dateString, cursor.getString(COL_WEATHER_DESC),high, low);
        //detailTextView.setText(mForecast);

        if(mShareActionProvider !=null){
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAILS_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    private ShareActionProvider mShareActionProvider;


    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }
}

