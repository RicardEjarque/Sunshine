package app.com.example.ricard.sunshine;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.content.Context;

/**
 * Created by ejarque on 18/11/2016.
 */

public class CommonHelpers {

    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor sharedPreferencesEditor;

    public void writePreference (AppCompatActivity activity, String preference, String pValue) {

        if (sharedPreferences==null){

            sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
            sharedPreferencesEditor = sharedPreferences.edit();

        }

       sharedPreferencesEditor.putString(preference, pValue);
       sharedPreferencesEditor.commit();


    }

    public String readPreference (AppCompatActivity activity, String preference) {

        if (sharedPreferences==null){

            sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
        }
        String pValue = "";
        sharedPreferences.getString(preference, pValue);
        return pValue;

    }


}
