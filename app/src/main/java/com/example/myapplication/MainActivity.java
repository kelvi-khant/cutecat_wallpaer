package com.example.myapplication;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import com.example.myapplication.Adepter.FullScreenAdapter;
import com.example.myapplication.Fragment.ForYouFragment;
import com.example.myapplication.Fragment.HomeFragment;
import com.example.myapplication.Model.WallpaperModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public static BottomNavigationView bottomNavigationView;
    Fragment selectFragment = null;
    public static ViewPager2 fullscreen;
    public static ArrayList<WallpaperModel> wallpaperList=new ArrayList<> ();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        loadLocal ();


//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
////            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
////        }
       getWindow ().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN );
        getWindow ().setStatusBarColor(Color.TRANSPARENT);

        setContentView (R.layout.activity_main);

        String savedLanguage = LanguageUtils.getSavedLanguage(this);
        LanguageUtils.setSelectedLanguage(savedLanguage, this);

        bottomNavigationView = findViewById (R.id.buttom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containar, new HomeFragment ()).commit();
        fullscreen = findViewById (R.id.fulllayoutviewpager);

        FullScreenAdapter fullScreenAdapter = new FullScreenAdapter (MainActivity.this, wallpaperList);
        fullscreen.setAdapter(fullScreenAdapter);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    int item = menuItem.getItemId();
                    if (item == R.id.HOME) {
                        selectFragment = new HomeFragment ();
                        fullscreen.setVisibility (View.GONE);
                        ColorStateList colorStateList = ColorStateList.valueOf(Color.BLACK); // Change to your desired color
                        bottomNavigationView.setItemTextColor(colorStateList);
                        bottomNavigationView.setItemIconTintList (colorStateList);

                    } else if (item == R.id.ForYou) {
                        selectFragment = new ForYouFragment ();
                        fullscreen.setVisibility (View.GONE);
                        ColorStateList colorStateList = ColorStateList.valueOf(Color.BLACK); // Change to your desired color
                        bottomNavigationView.setItemTextColor(colorStateList);
                        bottomNavigationView.setItemIconTintList(createColorStateList(Color.BLACK, Color.BLACK));
                    }
                    if (selectFragment != null) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containar, selectFragment).commit();
                    }
                    return true;
                }
            };
    public static void bottumsheet() {
        if (fullscreen != null ) {
            bottomNavigationView.setBackground(new ColorDrawable(Color.TRANSPARENT));
            ColorStateList colorStateList = ColorStateList.valueOf(Color.WHITE); // Change to your desired color
            bottomNavigationView.setItemTextColor(colorStateList);
            bottomNavigationView.setItemIconTintList(createColorStateList(Color.BLACK, Color.WHITE));
            bottomNavigationView.setItemTextAppearanceActive(R.style.CustomTextStyle);
            bottomNavigationView.setItemTextAppearanceInactive(R.style.CustomTextStyle);
        } else {
            Toast.makeText (fullscreen.getContext (), "nochange", Toast.LENGTH_SHORT).show ();
        }
    }

    private static ColorStateList createColorStateList(int selectedColor, int unselectedColor) {
        int[][] states = new int[][]{
                new int[]{android.R.attr.state_checked},
                new int[]{-android.R.attr.state_checked}
        };
        int[] colors = new int[]{
                selectedColor,
                unselectedColor
        };
        return new ColorStateList (states, colors);
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder alert = new AlertDialog.Builder (this);
        View view = getLayoutInflater ().inflate (R.layout.dialogbox_rate_us,null);
        alert.setView (view);
        final AlertDialog dialog =alert.create ();
        dialog.setCancelable (false);
        view.findViewById (R.id.RatingBar).setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse ("https://play.google.com/store/apps/details?id=" + MainActivity.this);
                Intent intent = new Intent (Intent.ACTION_VIEW,uri);
                try {
                    startActivity (intent);
                }
                catch (Exception e){
                    e.printStackTrace ();
                    Toast.makeText (MainActivity.this, "Unable to open", Toast.LENGTH_SHORT).show ();
                }
            }
        });
        view.findViewById (R.id.ExitApp).setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                dialog.dismiss ();
                finishAffinity ();
            }
        });
        view.findViewById (R.id.close).setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                dialog.dismiss ();
            }
        });
        dialog.show ();
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

    public void setstatusbar(){
        Window window = getWindow ();

    }
}
