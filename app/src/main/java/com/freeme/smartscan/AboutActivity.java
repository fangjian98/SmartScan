package com.freeme.smartscan;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

public class AboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.about, new AboutFragment())
                .commit();
    }

    public static class AboutFragment extends PreferenceFragment {

        private static final String ABOUT_UPDATES_KEY = "about_updates_key";
        private static final String ABOUT_POLICY_KEY = "about_policy_key";
        private static final String ABOUT_USER_AGREEMENT_KEY = "about_user_agreement_key";

        private AboutActivity mActivity;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.about_preferences);
        }

        @Override
        public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
            String key = preference.getKey();

            switch(key){
                case ABOUT_UPDATES_KEY:
                    break;
                case ABOUT_POLICY_KEY:
                    break;
                case ABOUT_USER_AGREEMENT_KEY:
                    break;
            }
            return super.onPreferenceTreeClick(preferenceScreen, preference);
        }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            mActivity = (AboutActivity) context;
        }
    }
}