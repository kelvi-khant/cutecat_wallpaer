package com.example.myapplication.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.myapplication.Activities.LiveActivity;
import com.example.myapplication.Adepter.FullScreenAdapter;
import com.example.myapplication.Adepter.MultipleLayoutAdapter;
import com.example.myapplication.LanguageActivity;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.Model.WallpaperModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
public class ForYouFragment extends Fragment {
    public static RelativeLayout main;
    public static RecyclerView catWallpaperRV;
    private static ProgressBar lodingPB;
    private static ProgressBar layoutPB;
    private ImageView changelist , listlayout, refresh1, refresh2 ;
    public static ImageView fullscreenlayout;
    static ArrayList<WallpaperModel> list = new ArrayList<> ();
    public static int currentLayout = MultipleLayoutAdapter.LAYOUT_TYPE_THREE;
    static MultipleLayoutAdapter adapterclass;
    private static FullScreenAdapter fullScreenAdapter;
    private static int currentPage = 1;
    private static boolean isLoading = false;
    Boolean isScrolling = false;
    private static boolean LastPage = false;
    private static final int ITEMS_PER_PAGE = 15; // Items per page
    static String selectedOption = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        loadLocal ();
        View view = inflater.inflate (R.layout.fragment_for_you, container, false);

        catWallpaperRV = view.findViewById (R.id.CatWallpaper);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext ());
        catWallpaperRV.setLayoutManager(layoutManager);
        catWallpaperRV.setHasFixedSize(true);
        adapterclass = new MultipleLayoutAdapter (getContext (),list,currentLayout);
        fullScreenAdapter = new FullScreenAdapter (getContext (),list);
        layoutPB = view.findViewById (R.id.layoutPB);
        lodingPB = view.findViewById (R.id.progressbar);
        main = view.findViewById (R.id.mainlayout);
        refresh1 = view.findViewById (R.id.refresh1);
        refresh2 = view.findViewById (R.id.refresh2);

        refresh1.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                onRefreshButtonClick();
            }
        });
        refresh2.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                onBackButton();
            }
        });
        fullscreenlayout = view.findViewById (R.id.fullScreen);
        fullscreenlayout.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {

                MainActivity.fullscreen.setAdapter(fullScreenAdapter);
                MainActivity.fullscreen.setVisibility (View.VISIBLE);
                main.setVisibility (View.GONE);
                loadMoreData ();
                MainActivity.bottumsheet ();
//                Activity activity = getActivity();
//                // Check if the Activity is not null before accessing its methods or properties
//                if (activity != null) {
//                    // Set the window flags for fullscreen mode
//                    activity.getWindow().setFlags(
//                            WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                            WindowManager.LayoutParams.FLAG_FULLSCREEN
//                    );
//                }
            }
        });

        listlayout = view.findViewById (R.id.listlayout);
        listlayout.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                toggleLayoutManager ();
            }
        });

        changelist = view.findViewById (R.id.selectlist);
        changelist.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu (getContext (),changelist);
                popupMenu.getMenuInflater ().inflate (R.menu.options,popupMenu.getMenu ());
                popupMenu.setOnMenuItemClickListener (new PopupMenu.OnMenuItemClickListener () {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        if (menuItem.getItemId () == R.id.Recent){
                            selectedOption = "Option 1";
                            currentPage =1;
                            list.clear ();
                            lodingPB.setVisibility (View.VISIBLE);
                            getWallpapers (selectedOption);
                        }
                        else if (menuItem.getItemId () == R.id.Featured) {
                            selectedOption = "Option 2";
                            currentPage =1;
                            list.clear ();
                            lodingPB.setVisibility (View.VISIBLE);
                            getWallpapers (selectedOption);
                        }
                        else if  (menuItem.getItemId () == R.id.Popular) {
                            selectedOption = "Option 3";
                            currentPage =1;
                            list.clear ();
                            lodingPB.setVisibility (View.VISIBLE);
                            getWallpapers (selectedOption);
                        }
                        else if (menuItem.getItemId () == R.id.Random) {
                            selectedOption = "Option 4";
                            currentPage =1;
                            list.clear ();
                            lodingPB.setVisibility (View.VISIBLE);
                            getWallpapers (selectedOption);
                        }
                        else if  (menuItem.getItemId () == R.id.Live_Wallpaper) {
                            selectedOption = "Option 5";
//                            currentPage =1;
//                            list.clear ();
//                            lodingPB.setVisibility (View.VISIBLE);
                            Intent intent = new Intent (getContext (), LiveActivity.class);
                            startActivity (intent);
                        }
                        return false;
                    }
                });
                popupMenu.show ();
            }
        });
        selectedOption = "Option 3";
        getWallpapers (selectedOption);
        setupScrollListener();
        return view;
    }
//    public static void Fullscreen() {
//
//        MainActivity.fullscreen.setAdapter(fullScreenAdapter);
//        MainActivity.fullscreen.setVisibility (View.VISIBLE);
//        main.setVisibility (View.GONE);
//        loadMoreData ();
//        MainActivity.bottumsheet ();
////        Window window =new MainActivity().getWindow();
////        MainActivity.setstatusbar(window);
////        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//    }
    private void setupScrollListener() {
        catWallpaperRV.addOnScrollListener (new RecyclerView.OnScrollListener () {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged (recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling = true;
                }
            }
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled (recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager ();
                int visibleItemCount = layoutManager.getChildCount ();
                int totalItemCount = layoutManager.getItemCount ();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition ();

                if (isScrolling && (visibleItemCount + firstVisibleItemPosition == totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount >= ITEMS_PER_PAGE)){ // Adjust as needed
                    isScrolling = false;
                    loadMoreData ();
                    adapterclass.notifyDataSetChanged ();
                }
            }
        });
    }
    public static void loadMoreData() {
        if (LastPage == true) {
            layoutPB.setVisibility (View.GONE);
        } else {
            layoutPB.setVisibility(View.VISIBLE);
            currentPage++;
            getWallpapers (selectedOption);
        }
    }
    public static void getWallpapers(String option){
        Log.e ("TAG", "getWallpapers:======"+option );
        String apiUrl = "";
        if ("Option 1".equals(option)) {
            apiUrl = "http://ayttechnology.com/catiowp_panel/api/v1/api.php?get_new_wallpapers&page=" + currentPage + "&count=" + ITEMS_PER_PAGE + "&filter=wallpaper&order=recent&category=0"; // Replace with the actual URL for Option 1
        } else if ("Option 2".equals(option)) {
            apiUrl = "http://ayttechnology.com/catiowp_panel/api/v1/api.php?get_new_wallpapers&page=" + currentPage + "&count=" + ITEMS_PER_PAGE + "&filter=both&order=featured&category=0"; // Replace with the actual URL for Option 2
        } else if ("Option 3".equals(option)) {
            apiUrl = "http://ayttechnology.com/catiowp_panel/api/v1/api.php?get_new_wallpapers&page=" + currentPage + "&count=" + ITEMS_PER_PAGE + "&filter=wallpaper&order=random&category=0"; // Replace with the actual URL for Option 3
        } else if ("Option 4".equals(option)) {
            apiUrl = "http://ayttechnology.com/catiowp_panel/api/v1/api.php?get_new_wallpapers&page=" + currentPage + "&count=" + ITEMS_PER_PAGE + "&filter=wallpaper&order=popular&category=0"; // Replace with the actual URL for Option 4
        } else if ("Option 5".equals(option)) {
//            apiUrl = "http://ayttechnology.com/catiowp_panel/api/v1/api.php?get_new_wallpapers&page=" + currentPage + "&count=" + ITEMS_PER_PAGE + "&filter=live&order=live&category=0";

        }
        if (currentPage == 1){
            lodingPB.setVisibility (View.VISIBLE);
            MultipleLayoutAdapter adapter = new MultipleLayoutAdapter(catWallpaperRV.getContext(), list, currentLayout);
            catWallpaperRV.setAdapter(adapter);
            adapter.notifyDataSetChanged ();
        }
        if (!apiUrl.isEmpty()) {
            new FetchDataTask().execute(apiUrl);
        }
    }
    private static class FetchDataTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute ();
            isLoading = true;
        }
        @Override
        protected String doInBackground(String... params) {
            String urlString = params[0];
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String response = null;

            try {
                URL url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");

                reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                response = stringBuilder.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return response;
        }
        @Override
        protected void onPostExecute(String response) {
                super.onPostExecute (response);

                isLoading = true;
                if (response != null) {
                    try {
                        lodingPB.setVisibility (View.GONE);
                        layoutPB.setVisibility (View.GONE);
                        JSONObject jsonObject = new JSONObject (response);
                        JSONArray jsonArray = jsonObject.getJSONArray ("posts");
                        List<WallpaperModel> newWallpapers = new ArrayList<> ();

                        for (int i = 0; i < jsonArray.length (); i++) {
                            JSONObject wallpaperObject = jsonArray.getJSONObject (i);
                            int id = wallpaperObject.getInt ("category_id");
                            String wallpaperUrl = wallpaperObject.getString ("image_url");
                            WallpaperModel wallpaper = new WallpaperModel (id, wallpaperUrl);
                            newWallpapers.add (wallpaper);
                        }
                        list.addAll (newWallpapers);
                        adapterclass.notifyDataSetChanged ();

                    } catch (JSONException e) {
                        e.printStackTrace ();
                    }
                }
            }
        }
    @Override
    public void onResume() {
        super.onResume();
        MultipleLayoutAdapter adapter = new MultipleLayoutAdapter(getContext(), list, currentLayout);
        catWallpaperRV.setAdapter(adapter);
        adapter.notifyDataSetChanged ();
        if (currentLayout == MultipleLayoutAdapter.LAYOUT_TYPE_ONE){
            catWallpaperRV.setLayoutManager(new GridLayoutManager(getContext(), 2));
            listlayout.setImageResource (R.drawable.baseline_calendar_view);
            lodingPB.setVisibility (View.GONE);
        } else if (currentLayout == MultipleLayoutAdapter.LAYOUT_TYPE_TWO) {
            listlayout.setImageResource (R.drawable.bookmark);
            lodingPB.setVisibility (View.GONE);
        } else {
            listlayout.setImageResource (R.drawable.calendar_view);
        }
    }
    public void onRefreshButtonClick() {
        refresh1.setVisibility (View.GONE);
        refresh2.setVisibility (View.VISIBLE);
        Collections.shuffle(list);
        setAdapter ();
    }
    private void onBackButton() {
        lodingPB.setVisibility (View.VISIBLE);
        refresh2.setVisibility (View.GONE);
        refresh1.setVisibility (View.VISIBLE);
        list.clear(); // Clear the list before re-fetching data
        getWallpapers (selectedOption);
        setAdapter ();
    }
    public void setAdapter() {
        MultipleLayoutAdapter adapter = new MultipleLayoutAdapter(getContext(), list, currentLayout);
        catWallpaperRV.setAdapter(adapter);
        adapter.notifyDataSetChanged ();
    }
    private void toggleLayoutManager() {
        switch (currentLayout) {
            case MultipleLayoutAdapter.LAYOUT_TYPE_TWO:
                currentLayout = MultipleLayoutAdapter.LAYOUT_TYPE_ONE;
                listlayout.setImageResource (R.drawable.baseline_calendar_view);
                catWallpaperRV.setLayoutManager (new GridLayoutManager (getContext (), 2));
                break;
            case MultipleLayoutAdapter.LAYOUT_TYPE_THREE:
                currentLayout = MultipleLayoutAdapter.LAYOUT_TYPE_TWO;
                listlayout.setImageResource (R.drawable.bookmark);
                catWallpaperRV.setLayoutManager (new LinearLayoutManager (getContext ()));
                break;
            case MultipleLayoutAdapter.LAYOUT_TYPE_ONE:
                currentLayout = MultipleLayoutAdapter.LAYOUT_TYPE_THREE;
                listlayout.setImageResource (R.drawable.calendar_view);
                catWallpaperRV.setLayoutManager (new LinearLayoutManager (getContext ()));
                break;
        }
        setAdapter ();
    }
    public void setLanguage(String languagecode){
        Locale locale = new Locale (languagecode);
        Locale.setDefault (locale);
        Configuration config = new Configuration ();
        config.locale = locale;
        getContext ().getResources ().updateConfiguration (config, getContext ().getResources ().getDisplayMetrics ());
        SharedPreferences.Editor editor = getContext ().getSharedPreferences("Settings",Context.MODE_PRIVATE).edit ();
        editor.putString ("MY_LANG",languagecode);
        editor.apply ();
    }
    public void loadLocal(){
        SharedPreferences sharedPreferences = getContext ().getSharedPreferences ("Settings", Context.MODE_PRIVATE);
        String lang = sharedPreferences.getString ("MY_LANG","");
        setLanguage (lang);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Clear the fullscreen flags when the fragment is destroyed
        Activity activity = getActivity();
        if (activity != null) {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }
}