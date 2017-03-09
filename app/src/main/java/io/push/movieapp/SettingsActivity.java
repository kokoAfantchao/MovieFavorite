package io.push.movieapp;



import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.v7.app.AppCompatActivity;

import java.util.List;


public class SettingsActivity extends AppCompatActivity {

    public  static  String PREF_SORT_TYPE_KEY="order_key";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Display the fragment as the main content.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }


    public static class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {

        @Override
        public void onStart() {
            super.onStart();
            getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.pref_generals);
            PreferenceScreen preferenceScreen = getPreferenceScreen();
            SharedPreferences sharedPreferences = preferenceScreen.getSharedPreferences();

            for(int i=0; i<preferenceScreen.getPreferenceCount(); i++){
                Preference preference = preferenceScreen.getPreference(i);
                if(!(preference instanceof  CheckBoxPreference)){
                    String string = sharedPreferences.getString(preference.getKey(), "");
                    changePreferenceSummary(preference,string);
                }

            }

        }

        @Override
        public void onStop() {
            super.onStop();
            getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);

        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            Preference preference = findPreference(key);
            if(preference!= null){
                if (!(preference instanceof CheckBoxPreference)){
                    String stringValue = sharedPreferences.getString(preference.getKey(), "");
                    changePreferenceSummary(preference,stringValue);
                }
            }
        }

        private void changePreferenceSummary(Preference preference, String stringValue) {

            if(preference instanceof  ListPreference){
                ListPreference listPreference = (ListPreference) preference;
                int indexOfValue = listPreference.findIndexOfValue(stringValue);
                if(indexOfValue>=0){
                    listPreference.setSummary(listPreference.getEntries()[indexOfValue]);
                }
            }
        }
    }
}