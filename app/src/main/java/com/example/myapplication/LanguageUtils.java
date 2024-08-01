package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class LanguageUtils {
    private static String selectedLanguage ;

    public static String getSelectedLanguage() {
        return selectedLanguage;
    }

    public static void setSelectedLanguage(String language, Context context) {
        selectedLanguage = language;
        updateLocale(context, language);
    }

    private static void updateLocale(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }
    public static String getSavedLanguage(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("pref", 0);
        return sharedPreferences.getString("selectedLanguage", "en"); // Default language is "en"
    }
}
