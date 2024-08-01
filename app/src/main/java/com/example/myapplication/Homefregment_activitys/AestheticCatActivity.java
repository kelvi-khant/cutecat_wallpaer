package com.example.myapplication.Homefregment_activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Adepter.CatAdepter;
import com.example.myapplication.Model.WallpaperModel;
import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AestheticCatActivity extends AppCompatActivity {
    ImageView back;
    private RecyclerView AestheticWallpaperRV;
    private ProgressBar lodingPB;
    ArrayList<WallpaperModel> list =new ArrayList<> ();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
////            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
////        }
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView (R.layout.activity_aesthetic_cat);

        back = findViewById (R.id.back);
        AestheticWallpaperRV =findViewById (R.id.AestheticWallpaperRV);
        lodingPB =findViewById (R.id.progressbar);

        AestheticWallpaperRV.setLayoutManager (new LinearLayoutManager (this));
        getWallpapers();

        back.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                finish ();
            }
        });
    }

    private void getWallpapers() {
        String Api = "http://ayttechnology.com/catiowp_panel/api/v1/api.php?get_new_wallpapers&page=1&count=43&filter=both&order=recent&category=2";
        RequestQueue requestQueue = Volley.newRequestQueue (this);
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
                        String wallpaperUrl = wallpaperObject.getString ("image_url");
                        Log.e ("TAG", "onResponce:xyz" + wallpaperObject);
                        list.add (new WallpaperModel (id, wallpaperUrl));
                    }
                    // Create and set the adapter for your RecyclerView.
                    CatAdepter adapter = new CatAdepter (AestheticCatActivity.this, list);
                    AestheticWallpaperRV.setAdapter (adapter);
                } catch (JSONException e) {
                    e.printStackTrace ();
                }
            }
        },new Response.ErrorListener () {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<> ();
                headers.put ("id", "");
                headers.put ("wallpaperUrl", "");
                return headers;
            }
        };
        requestQueue.add (jsonObjectRequest);
    }

    @Override
    protected void onResume() {
        CatAdepter adapter = new CatAdepter (AestheticCatActivity.this, list);
        AestheticWallpaperRV.setAdapter (adapter);
        adapter.notifyDataSetChanged ();
        super.onResume ();
    }
}