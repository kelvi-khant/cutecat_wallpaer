package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.Model.LanguageModel;

import java.util.List;
import java.util.Locale;

public class SplaceActivity extends AppCompatActivity {

    ImageView imageView;
    TextView txt1,txt2;
    Animation top,bottom,logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        setContentView(R.layout.activity_splace);

        imageView = findViewById(R.id.imageview);
        txt1 = findViewById(R.id.txt1);
        txt2 = findViewById(R.id.txt2);

        top = AnimationUtils.loadAnimation(this, R.anim.top);
        bottom = AnimationUtils.loadAnimation(this, R.anim.bottom);
        logo = AnimationUtils.loadAnimation(this, R.anim.zoom_animation);

        imageView.setAnimation(logo);
        txt1.setAnimation(bottom);
        txt2.setAnimation(bottom);

        new Handler().postDelayed(() -> {
            if (isLanguageSelected()) {
                // Language is selected, go to MainActivity
                startActivity(new Intent(SplaceActivity.this, MainActivity.class));
            } else {
                // Language is not selected, go to LanguageActivity
                startActivity(new Intent(SplaceActivity.this, LanguageActivity.class));
            }
            finish();
        }, 3000);
    }
    private boolean isLanguageSelected() {
        SharedPreferences preferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        return preferences.contains("MY_LANG");
    }
}
