package com.malfaang.e_culture_tool_a;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Locale;


public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        loadSetting();
        loadLocale();
    }
    private void loadSetting(){
        Preference mtpref = (Preference) findPreference("Language");
        mtpref.setOnPreferenceChangeListener((preference, o) -> {
            String items= (String)o;
            if(preference.getKey().equals("Language")){
                switch (items){
                    case "Italiano":
                        setLocale("it");
                        break;
                    case "Inglese":
                        setLocale("en");
                        break;
                    case "Tedesco":
                        setLocale("de");
                        break;
                    case "Spagnolo":
                        setLocale("es");
                        break;
                    case "Francese":
                        setLocale("fr");
                        break;
                    case "Giapponese":
                        setLocale("ja");
                        break;
                    case "Russo":
                        setLocale("ru");
                        break;
                    case "Auto":
                        setLocale(String.valueOf(Resources.getSystem().getConfiguration().locale));
                        break;
                }
            }
            getActivity().recreate();
            return true;
        });

    }

    public void loadLocale() {
        String langPref = "Language";
        SharedPreferences prefs = getContext().getSharedPreferences("CommonPrefs",
                Activity.MODE_PRIVATE);
        String language = prefs.getString(langPref, "");
        setLocale(language);

    }


    public void saveLocale(String lang) {
        String langPref = "Language";
        SharedPreferences prefs = getContext().getSharedPreferences("CommonPrefs",
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(langPref, lang);
        editor.commit();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getActivity().recreate();
        loadSetting();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadSetting();
    }

    public void setLocale(String lang) {
        if (lang.equalsIgnoreCase(""))
            return;
        saveLocale(lang);
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = new Locale(lang.toLowerCase());
        res.updateConfiguration(conf,dm);
    }
}
