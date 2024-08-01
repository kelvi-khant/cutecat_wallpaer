package com.example.myapplication.Homefregment_activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.Adepter.FavoriteWallpapersAdapter;
import com.example.myapplication.R;
import com.example.myapplication.Model.WallpaperModel;
import com.example.myapplication.FavDB;

import java.util.ArrayList;

public class FavoritActivity extends AppCompatActivity {

    ImageView back;
    private FavDB favDB;
    private TextView favtext;
    private RecyclerView favWallpaperRV;
    private FavoriteWallpapersAdapter favAdepter;
    private ArrayList<WallpaperModel> favModelArrayList = new ArrayList<> ();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
////            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
////        }
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView (R.layout.activity_favorit);
        back = findViewById (R.id.back);
        back.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                finish ();
            }
        });

        favtext = findViewById (R.id.favtext);
        favWallpaperRV = findViewById (R.id.favRV);
        favWallpaperRV.setLayoutManager (new GridLayoutManager (this, 2));
        favDB = new FavDB (this);
        favModelArrayList = favDB.getAllFavorites ();
        favAdepter = new FavoriteWallpapersAdapter (this,favModelArrayList);
        favWallpaperRV.setAdapter (favAdepter);
    }
}
