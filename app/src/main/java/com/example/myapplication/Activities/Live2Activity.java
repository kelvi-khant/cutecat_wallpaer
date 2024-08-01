package com.example.myapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.myapplication.Adepter.FullWallViewAdapter;
import com.example.myapplication.Constant;
import com.example.myapplication.Model.WallpaperLiveModel;
import com.example.myapplication.R;
import com.example.myapplication.SharedPreferencesManager;
import com.example.myapplication.liveWallpaper;

import java.io.IOException;
import java.util.ArrayList;

public class Live2Activity extends AppCompatActivity {
    Activity activity = Live2Activity.this;
    String TAG = Live2Activity.class.getSimpleName();

    ViewPager2 viewPager;
    ImageView iv_backgroundFull;
    AppCompatButton btn_apply;

    ArrayList<WallpaperLiveModel> wallpaperModelArrayList = new ArrayList<WallpaperLiveModel> ();
    int selectPosition = 0;

    FullWallViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live2);

        initVal();
        setLocalData();
        setOnClickListener();

    }

    private void initVal() {

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        iv_backgroundFull = findViewById(R.id.iv_backgroundFull);
        viewPager = findViewById(R.id.viewPager);
        btn_apply = findViewById(R.id.btn_apply);

    }

    private void setLocalData() {

        selectPosition = getIntent().getIntExtra(Constant.POS1, 0);

        wallpaperModelArrayList = Constant.LiveWallArrayList;

        Glide.with(getApplicationContext()).asBitmap().load(wallpaperModelArrayList.get(selectPosition).getFull_img()).transition(BitmapTransitionOptions.withCrossFade()).into(new CustomTarget<Bitmap> () {
            public void onLoadCleared(Drawable drawable) {
            }

            public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                iv_backgroundFull.setImageBitmap(Constant.blurImage(activity, bitmap));
            }
        });
        ObjectAnimator.ofFloat(iv_backgroundFull, View.ALPHA, new float[]{0.6f, 1.0f}).setDuration(1000).start();

        adapter = new FullWallViewAdapter(activity, wallpaperModelArrayList);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(selectPosition, false);
        viewPager.setClipToPadding(false);
        viewPager.setClipChildren(false);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int i = displayMetrics.widthPixels / 7;
        viewPager.setPadding(i, 0, i, 40);
        viewPager.setOffscreenPageLimit(3);
        viewPager.getChildAt(0).setOverScrollMode(2);
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer (40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                page.setScaleY(((1.0f - Math.abs(position)) * 0.15f) + 0.85f);
            }
        });
        viewPager.setPageTransformer(compositePageTransformer);

    }

    private void setOnClickListener() {

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                selectPosition = position;

                if(wallpaperModelArrayList.get(position).getFull_img()!=null){
                    Glide.with(getApplicationContext()).asBitmap().load(wallpaperModelArrayList.get(position).getFull_img()).transition(BitmapTransitionOptions.withCrossFade()).into(new CustomTarget<Bitmap>() {
                        public void onLoadCleared(Drawable drawable) {
                        }

                        public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                            iv_backgroundFull.setImageBitmap(Constant.blurImage(activity, bitmap));
                        }
                    });
                    ObjectAnimator.ofFloat(iv_backgroundFull, View.ALPHA, new float[]{0.6f, 1.0f}).setDuration(1000).start();
                }
            }
        });

        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
                SharedPreferencesManager.getInstance(activity).savePreferences("liveWallpaper",wallpaperModelArrayList.get(selectPosition).getFull_img());
                intent.putExtra("liveWallpaper",wallpaperModelArrayList.get(selectPosition).getFull_img());
                intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new ComponentName (activity, liveWallpaper.class));
                activity.startActivity(intent);

                WallpaperManager wallpaperManager = WallpaperManager.getInstance(activity);
                try {
                    wallpaperManager.clear();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}