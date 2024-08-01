package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.myapplication.Adepter.SliderAdepter;
import com.example.myapplication.Model.WallpaperModel;

import java.util.ArrayList;
import java.util.Locale;

public class FullWallpaperActivity extends AppCompatActivity {
    ViewPager2 viewPager2;
    ImageView back;
    int image;
    public static ArrayList<WallpaperModel> wallpaperList = new ArrayList<> ();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        loadLocal();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        setContentView (R.layout.activity_full_wallpaper);

        viewPager2 = findViewById (R.id.fullviewpager);

        wallpaperList = new ArrayList<> (MainActivity.wallpaperList);
        Intent intent = getIntent ();
        image = intent.getIntExtra ("image", 0);
        SliderAdepter adapter = new SliderAdepter (wallpaperList, FullWallpaperActivity.this);
        viewPager2.setAdapter (adapter);
        viewPager2.setCurrentItem (image);

        back = findViewById (R.id.clean);
        back.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                finish ();
            }
        });
    }

    public void setLanguage(String languagecode){

        Locale locale = new Locale (languagecode);
        Locale.setDefault (locale);
        Configuration config = new Configuration ();
        config.locale = locale;
        this.getResources ().updateConfiguration (config, this.getResources ().getDisplayMetrics ());
        SharedPreferences.Editor editor = this.getSharedPreferences("Settings", Context.MODE_PRIVATE).edit ();
        editor.putString ("MY_LANG",languagecode);
        editor.apply ();
    }
    public void loadLocal(){
        SharedPreferences sharedPreferences = this.getSharedPreferences ("Settings", Context.MODE_PRIVATE);
        String lang = sharedPreferences.getString ("MY_LANG", " ");
        setLanguage (lang);
    }
}