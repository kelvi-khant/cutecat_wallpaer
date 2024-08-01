package com.example.myapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.myapplication.Adepter.WallpaperAdapter;
import com.example.myapplication.Constant;
import com.example.myapplication.Model.WallpaperLiveModel;
import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class LiveActivity extends AppCompatActivity {


    Activity activity = LiveActivity.this;
    String TAG = LiveActivity.class.getSimpleName();

    ArrayList<WallpaperLiveModel> randomLiveWallArrayList = new ArrayList<> ();

    RecyclerView rv_Main;

    WallpaperLiveModel model;
    WallpaperAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_live);

        initVal();
        setLocalData();
        setOnClickListener();
        getLiveWalls();

    }

    private void initVal() {

        rv_Main = findViewById(R.id.rv_Main);

    }

    private void setLocalData() {

        rv_Main.setLayoutManager(new GridLayoutManager (activity, 3));

        adapter = new WallpaperAdapter(activity, randomLiveWallArrayList);
        rv_Main.setAdapter(adapter);

    }

    private void setOnClickListener() {

    }

    private void getLiveWalls(){
        randomLiveWallArrayList.clear();
        AndroidNetworking.get(Constant.BaseApi)
                .addHeaders("Content-Type", "application/json")
                .setTag("DrD")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener () {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "onResponse:------->>"+response );
                        try{
                            JSONArray jsonArray = response.getJSONArray("posts");
                            int length = jsonArray.length();
                            if(length != 0) {
                                for (int i = 0; i < length; i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String full_img = jsonObject.getString("image_url");
                                    String thumb_img = jsonObject.getString("image_url");

                                    model = new WallpaperLiveModel (full_img, thumb_img);
                                    randomLiveWallArrayList.add(model);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }
                        catch (Exception e){
                            Log.e(TAG, "onResponse:------->>"+e );
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "onResponse:------->>"+anError );
                    }
                });
    }

}