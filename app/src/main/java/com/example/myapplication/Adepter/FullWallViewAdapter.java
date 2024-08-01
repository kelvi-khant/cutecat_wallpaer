package com.example.myapplication.Adepter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Model.WallpaperLiveModel;
import com.example.myapplication.R;

import java.util.ArrayList;

public class FullWallViewAdapter extends RecyclerView.Adapter<FullWallViewAdapter.ViewHolder> {
    Activity activity;
    ArrayList<WallpaperLiveModel> wallpaperModelList = new ArrayList<>();
    public FullWallViewAdapter(Activity activity, ArrayList<WallpaperLiveModel> wallpaperModelList) {
        this.activity = activity;
        this.wallpaperModelList = wallpaperModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.adapter_fullview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Glide.with(activity).load(wallpaperModelList.get(position).getFull_img()).into(holder.iv_adaptervp_full4kWall);

    }

    @Override
    public int getItemCount() {
        return wallpaperModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_adaptervp_full4kWall;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_adaptervp_full4kWall = itemView.findViewById(R.id.iv_adaptervp_full4kWall);

        }
    }

}

