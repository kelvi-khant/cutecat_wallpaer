package com.example.myapplication.Adepter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Activities.Live2Activity;
import com.example.myapplication.Constant;
import com.example.myapplication.Model.WallpaperLiveModel;
import com.example.myapplication.R;

import java.util.ArrayList;

public class WallpaperAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Activity activity;
    ArrayList<WallpaperLiveModel> wallpaperModelArrayList;

    public WallpaperAdapter(Activity activity, ArrayList<WallpaperLiveModel> wallpaperModelArrayList) {
        this.activity = activity;
        this.wallpaperModelArrayList = wallpaperModelArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.adapter_wallpaper_view2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder inHolder, @SuppressLint("RecyclerView") int position) {
        ViewHolder holder = (ViewHolder) inHolder;
        Glide.with(activity).load(wallpaperModelArrayList.get(position).getThumb_img()).into(holder.iv_adapter_wall2);

        holder.iv_adapter_wall2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.LiveWallArrayList = wallpaperModelArrayList;
                Intent intent = new Intent(activity, Live2Activity.class);
                intent.putExtra(Constant.POS1, position);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return wallpaperModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_adapter_wall2;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_adapter_wall2 = itemView.findViewById(R.id.iv_adapter_wall2);

        }
    }
}
