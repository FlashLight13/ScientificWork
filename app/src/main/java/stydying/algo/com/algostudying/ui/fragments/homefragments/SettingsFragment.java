package stydying.algo.com.algostudying.ui.fragments.homefragments;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import stydying.algo.com.algostudying.R;

/**
 * Created by Anton on 19.07.2015.
 */
public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.game_settings_preferences);
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
    }
}
