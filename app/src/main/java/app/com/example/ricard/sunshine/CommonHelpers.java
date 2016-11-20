package app.com.example.ricard.sunshine;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


/**
 * Created by ejarque on 18/11/2016.
 */

public class CommonHelpers {

    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor sharedPreferencesEditor;
    private final String LOG_TAG = settingsFragment.class.getSimpleName();
    public void CommonHelpers(){

    }

    public void writePreference (Activity activity, String preference, String pValue) {

        if (sharedPreferences==null){

            sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
            sharedPreferencesEditor = sharedPreferences.edit();

        }

       sharedPreferencesEditor.putString(preference, pValue);
       sharedPreferencesEditor.commit();


    }

    public String readPreference (Activity activity, String preference) {

        if (sharedPreferences==null){

            sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
        }
        String defValue = "NOT_FOUND";
        String pValue;
        Log.e(LOG_TAG, "The setting to be modified is:"+preference);
        pValue = sharedPreferences.getString(preference, defValue);
        return pValue;

    }


}
