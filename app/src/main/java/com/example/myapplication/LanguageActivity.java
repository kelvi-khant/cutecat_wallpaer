package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.Adepter.LanguageAdapter;
import com.example.myapplication.Fragment.HomeFragment;
import com.example.myapplication.Model.LanguageModel;
import java.util.ArrayList;
import java.util.List;
public class LanguageActivity extends AppCompatActivity {
    RecyclerView languageRV;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ImageView back;
    AppCompatButton setbtn;
    List<LanguageModel> languageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        getWindow().setStatusBarColor(Color.TRANSPARENT);

        setContentView(R.layout.activity_language);
        setbtn = findViewById (R.id.setbtn);
        setbtn.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                LanguageAdapter.setbtn ();
            }
        });

        back = findViewById (R.id.back);
        back.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (LanguageActivity.this,MainActivity.class);
                startActivity (intent);
            }
        });

        sharedPreferences = getSharedPreferences("pref", 0);
        editor = sharedPreferences.edit();

        languageList.add(new LanguageModel(R.drawable.flag_english, "en", "English"));
        languageList.add(new LanguageModel(R.drawable.flag_india, "hi", "Hindi"));
        languageList.add(new LanguageModel(R.drawable.flag_india, "gu", "Gujarati"));
        languageList.add(new LanguageModel(R.drawable.flag_spanish, "es", "Spanish"));
        languageList.add(new LanguageModel(R.drawable.flag_indonacia, "in", "Indonacia"));
        languageList.add(new LanguageModel(R.drawable.flag_france, "fr", "France"));
        languageList.add(new LanguageModel(R.drawable.flag_tamil, "ta", "Tamil"));
        languageList.add(new LanguageModel(R.drawable.flag_telugu, "te", "Telugu"));

        languageRV = findViewById(R.id.recyclerView);

        LanguageAdapter languageAdapter = new LanguageAdapter(this, languageList);
        languageRV.setLayoutManager(new LinearLayoutManager(this));
        languageRV.setItemAnimator(new DefaultItemAnimator());
        languageRV.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        languageRV.setAdapter(languageAdapter);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent (LanguageActivity.this,MainActivity.class);
        startActivity (intent);
    }
}

