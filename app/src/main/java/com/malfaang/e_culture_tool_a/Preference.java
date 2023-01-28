package com.malfaang.e_culture_tool_a;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.util.DisplayMetrics;

import java.util.Locale;

public class Preference extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);
        loadSetting();
    }

    private void loadSetting(){
        ListPreference lp = (ListPreference)findPreference("Language");


        lp.setOnPreferenceChangeListener((preference, o) -> {
            String items= (String) o;
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
                        setLocale("jp");
                        break;
                    case "Russo":
                        setLocale("ru");
                        break;
                    case "Auto":
                        setLocale(Locale.getDefault().getLanguage());
                        break;
                }
            }
            Intent intent = getIntent();
            finish();
            startActivity(intent);
            return true;
        });

    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        saveLocale(lang);
        Locale.setDefault(myLocale);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = new Locale(lang.toLowerCase());
        res.updateConfiguration(conf,dm);
    }

    public void saveLocale(String lang) {
        String langPref = "Language";
        SharedPreferences prefs = getSharedPreferences("CommonPrefs",
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(langPref, lang);
        editor.commit();
    }

    @Override
    protected void onResume() {
        loadSetting();
        super.onResume();
    }

}
