package com.example.myapplication.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Adepter.HomefregmentAdapter;
import com.example.myapplication.Adepter.MultipleLayoutAdapter;
import com.example.myapplication.LanguageActivity;
import com.example.myapplication.Model.WallpaperModel;
import com.example.myapplication.Homefregment_activitys.FavoritActivity;
import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class HomeFragment extends Fragment {
    ImageView more;
    private RecyclerView homeimages;
    private ProgressBar lodingPB;
    ArrayList<WallpaperModel> list =new ArrayList<> ();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        loadLocal ();
        View view = inflater.inflate (R.layout.fragment_home, container, false);
        more = view.findViewById (R.id.selectmore);
        lodingPB =view.findViewById (R.id.progressbar);
        lodingPB.setVisibility (View.VISIBLE);
        homeimages = view.findViewById (R.id.homeRV);

        homeimages.setLayoutManager(new LinearLayoutManager (getContext ()));
        getimages();

        more.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                optionmenu();
            }
        });

        return view;
    }
    private void getimages(){
        String Api = "http://ayttechnology.com/catiowp_panel/api/v1/api.php?get_new_wallpapers&count=5&category=2";
        RequestQueue requestQueue = Volley.newRequestQueue (getContext ());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest (Request.Method.GET, Api, null, new Response.Listener<JSONObject> () {
            @Override
            public void onResponse(JSONObject json) {
                Log.e ("TAG", "onResponce:mmm" + json);
                try {
                    lodingPB.setVisibility (View.GONE);
                    JSONArray categoriesArray = json.getJSONArray ("posts");

                    for (int i = 0; i < categoriesArray.length (); i++) {
                        JSONObject wallpaperObject = categoriesArray.getJSONObject (i);
                        int id = wallpaperObject.getInt ("category_id");
                        String categoryimage= wallpaperObject.getString ("image_url");

                        Log.e ("TAG", "onResponce:xyz" + categoryimage);

                        list.add (new WallpaperModel (id,categoryimage));
                    }
                    HomefregmentAdapter adapter = new HomefregmentAdapter (getContext (), list);
                    homeimages.setAdapter (adapter);
                    adapter.notifyDataSetChanged ();

                } catch (JSONException e) {
                    e.printStackTrace ();
                }
            }
        },new Response.ErrorListener () {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Error:===" + error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<> ();
                headers.put ("categoryImage", "");
                return headers;
            }
        };
        requestQueue.add (jsonObjectRequest);
    }
    private void optionmenu(){
        PopupMenu popupMenu = new PopupMenu (getContext (),more);
        popupMenu.getMenuInflater ().inflate (R.menu.moreoption,popupMenu.getMenu ());
        popupMenu.setOnMenuItemClickListener (new PopupMenu.OnMenuItemClickListener () {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId () == R.id.Rate){
                    Uri uri = Uri.parse ("https://play.google.com/store/apps/details?id=" +getContext ());
                    Intent intent = new Intent (Intent.ACTION_VIEW,uri);
                    try {
                        startActivity (intent);
                    }
                    catch (Exception e){
                        e.printStackTrace ();
                    }
                }
                else if (menuItem.getItemId () == R.id.Favorite) {
                    Intent intent = new Intent (getContext (), FavoritActivity.class);
                    startActivity (intent);
                }
                else if (menuItem.getItemId () == R.id.Share) {
                    Intent intent = new Intent ();
                    intent.setAction (Intent.ACTION_SEND);
                    intent.setType ("text/plain");
                    intent.putExtra (Intent.EXTRA_TEXT,"Download this Wallpaper Application \n PlayStore Link : https://play.google.com/store/apps/details?id=" + getParentFragment ());
                    startActivity (Intent.createChooser (intent,"Choose one"));
                }
                else if (menuItem.getItemId () == R.id.FeedBack) {
                    Intent intent = new Intent (Intent.ACTION_SENDTO);
                    String UriText = "mailto:" + Uri.encode ("kelvikhant@gmail.com") + "?subject=" + Uri.encode ("Feedback")
                            + "$body=" + Uri.encode ("");
                    Uri uri =Uri.parse (UriText);
                    intent.setData (uri);
                    startActivity (Intent.createChooser (intent,"send mail"));
                }
                else if (menuItem.getItemId () == R.id.Language) {
                    Intent intent = new Intent (getContext (), LanguageActivity.class);
                    startActivity (intent);
                    getActivity ().finish ();
                }
                return false;
            }
        });
        popupMenu.show ();
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
}