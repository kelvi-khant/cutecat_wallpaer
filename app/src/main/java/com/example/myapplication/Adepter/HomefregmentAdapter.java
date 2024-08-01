package com.example.myapplication.Adepter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Homefregment_activitys.AestheticCatActivity;
import com.example.myapplication.Homefregment_activitys.BlackcatActivity;
import com.example.myapplication.Homefregment_activitys.CatActivity;
import com.example.myapplication.Homefregment_activitys.FunnyCatActivity;
import com.example.myapplication.Homefregment_activitys.kittenActivity;
import com.example.myapplication.MainActivity;
import com.example.myapplication.Model.WallpaperModel;
import com.example.myapplication.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class HomefregmentAdapter extends RecyclerView.Adapter <HomefregmentAdapter.SwiperHolder> {

    private Context context;
    private ArrayList<WallpaperModel> list;
    public static String imagelist;
    public HomefregmentAdapter(Context context, ArrayList<WallpaperModel> list) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public HomefregmentAdapter.SwiperHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_homefregment,parent,false);
        return new HomefregmentAdapter.SwiperHolder(view);
    }
    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull HomefregmentAdapter.SwiperHolder holder, int position) {
        WallpaperModel post =list.get(position);
        String url=post.getWallpaperUrl ();
        Glide.with(context).load(url).into(holder.homefregmentimage);

        holder.mainwallpaper.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                MainActivity.wallpaperList=list;

                if(position == 0){
                    Intent intent = new Intent (context , CatActivity.class);
                    intent.putExtra ("image",position);
                    imagelist= post.getWallpaperUrl ();
                    context.startActivity (intent);
                } else if(position == 1) {
                    Intent intent = new Intent (context , BlackcatActivity.class);
                    intent.putExtra ("image",position);
                    imagelist= post.getWallpaperUrl ();
                    context.startActivity (intent);
                } else if (position == 2) {
                    Intent intent = new Intent (context , kittenActivity.class);
                    intent.putExtra ("image",position);
                    imagelist= post.getWallpaperUrl ();
                    context.startActivity (intent);
                } else if (position == 3) {
                    Intent intent = new Intent (context , AestheticCatActivity.class);
                    intent.putExtra ("image",position);
                    imagelist= post.getWallpaperUrl ();
                    context.startActivity (intent);
                } else if (position == 4) {
                    Intent intent = new Intent (context , FunnyCatActivity.class);
                    intent.putExtra ("image",position);
                    imagelist= post.getWallpaperUrl ();
                    context.startActivity (intent);
                } else {
                    Toast.makeText (context,R.string.somthing_wrong, Toast.LENGTH_SHORT).show ();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SwiperHolder extends RecyclerView.ViewHolder {
        public ShapeableImageView homefregmentimage;
        RelativeLayout mainwallpaper;

        public SwiperHolder(@NonNull View itemView) {
            super (itemView);

            homefregmentimage = itemView.findViewById (R.id.homefregmentimage);
            mainwallpaper = itemView.findViewById (R.id.mainwallpaper);
        }
    }
}
