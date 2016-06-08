package fr.xgouchet.packageexplorer.ui.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import fr.xgouchet.packageexplorer.R;
import fr.xgouchet.packageexplorer.core.Preferences;

/**
 * @author Xavier Gouchet
 */
public class AppFiltersPrefsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_filters);
        setHasOptionsMenu(true);

        getPreferenceManager().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
        onSharedPreferenceChanged(getPreferenceManager().getSharedPreferences(), Preferences.KEY_SORT_CRITERIA);

    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);

        if (preference instanceof ListPreference) {
            ListPreference listPref = (ListPreference) preference;
            listPref.setSummary(listPref.getEntry());
        }
    }
}
