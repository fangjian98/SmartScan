package com.freeme.smartscan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;

import androidx.annotation.Nullable;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
    }

    public static class SettingsFragment extends PreferenceFragment {

        private static final String SETTINGS_BORDER_KEY = "settings_border_key";
        private static final String SETTINGS_QRCODE_KEY = "settings_qrcode_key";
        private static final String SETTINGS_CARD_KEY = "settings_card_key";
        private static final String SETTINGS_DOCUMENT_KEY = "settings_document_key";

        private static final String SETTINGS_PRIVACY_POLICY_KEY = "settings_privacy_policy_key";
        private static final String SETTINGS_PERSONAL_INFORMATION_KEY = "settings_personal_information_key";
        private static final String SETTINGS_PARTNERS_SHARED_KEY = "settings_partners_shared_key";
        private static final String SETTINGS_PERMISSION_KEY = "settings_permission_key";
        private static final String SETTINGS_ABOUT_KEY = "settings_about_key";

        private SettingsActivity mActivity;


        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_preferences);
        }

        @Override
        public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
            String key = preference.getKey();

            switch(key){
                case SETTINGS_BORDER_KEY:
                    break;
                case SETTINGS_QRCODE_KEY:
                    startActivity(new Intent(mActivity, GenerateCodeActivity.class));
                    break;
                case SETTINGS_CARD_KEY:
                    //startActivity(new Intent(mActivity, CardActivity.class));
                    break;
                case SETTINGS_DOCUMENT_KEY:
                    //startActivity(new Intent(mActivity, DocumentActivity.class));
                    break;
                case SETTINGS_PRIVACY_POLICY_KEY:
                    break;
                case SETTINGS_PERSONAL_INFORMATION_KEY:
                    break;
                case SETTINGS_PARTNERS_SHARED_KEY:
                    break;
                case SETTINGS_PERMISSION_KEY:
                    break;
                case SETTINGS_ABOUT_KEY:
                    startActivity(new Intent(mActivity, AboutActivity.class));
                    break;
            }
            return super.onPreferenceTreeClick(preferenceScreen, preference);
        }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            mActivity = (SettingsActivity) context;
        }
    }
}