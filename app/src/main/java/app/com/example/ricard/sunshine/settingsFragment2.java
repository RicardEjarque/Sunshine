package app.com.example.ricard.sunshine;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Created by Ricard on 17/11/2016.
 */

public class settingsFragment2 extends Fragment {

    private final String LOG_TAG = settingsFragment2.class.getSimpleName();
    SettingsSelectedListener mCallback;
    public EditText city;
    public CommonHelpers SettingsManager;

    public settingsFragment2() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.settings_main, container, false);
        city = (EditText) rootView.findViewById(R.id.cityname);
        return rootView;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
        SettingsManager = new CommonHelpers();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.settingsfragment, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Log.e(LOG_TAG, "BACK BUTTON PRESSED 0");
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            Log.e(LOG_TAG, "BACK BUTTON PRESSED");

            if(SettingsManager==null){
                Log.e(LOG_TAG, "I'M NULL!");
            }
            SettingsManager.writePreference(getActivity(),"CITY",city.getText().toString());


            mCallback.onArticleSelected(city.getText().toString());
        }
        return super.onOptionsItemSelected(item);
    }

    //Interface to pass settings variables to main activity
    public interface SettingsSelectedListener {
        void onArticleSelected(String cityName);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (SettingsSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }




}
