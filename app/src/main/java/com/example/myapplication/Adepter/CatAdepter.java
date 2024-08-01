package com.example.myapplication.Adepter;

import static com.example.myapplication.R.string.removed_from_favorites;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.FavDB;
import com.example.myapplication.FullWallpaperActivity;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.Model.WallpaperModel;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class CatAdepter extends RecyclerView.Adapter <CatAdepter.SwiperHolder> {
    private Context context;
    private ArrayList<WallpaperModel> wallpaperList;
    public static String imagelist;
    private FavDB favDB;

    public CatAdepter(Context context, ArrayList<WallpaperModel> wallpaperList) {
        this.context = context;
        this.wallpaperList = wallpaperList;
    }
    @NonNull
    @Override
    public CatAdepter.SwiperHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        favDB = new FavDB (context);
        View view = LayoutInflater.from(context).inflate(R.layout.item_columb,parent,false);
        return new CatAdepter.SwiperHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull CatAdepter.SwiperHolder holder,int position) {
        WallpaperModel post =wallpaperList.get(position);
        String url=post.getWallpaperUrl ();
        Glide.with(context).load(url).into(holder.image);
        if (favDB.isFavorite(post.getWallpaperUrl ())) {
            holder.like_border.setImageResource(R.drawable.like);
        } else {
            holder.like_border.setImageResource(R.drawable.like_border);
        }
        holder.relativeLayout.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                MainActivity.wallpaperList=wallpaperList;
                Intent intent = new Intent (context , FullWallpaperActivity.class);
                intent.putExtra ("image",position);
                imagelist= post.getWallpaperUrl();
                context.startActivity (intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return wallpaperList.size();
    }

    public class SwiperHolder extends RecyclerView.ViewHolder {
        public ShapeableImageView image;
        RelativeLayout relativeLayout;
        public ImageView like_border;
        public SwiperHolder(@NonNull View itemView) {
            super (itemView);

            image = itemView.findViewById (R.id.imagec);
            relativeLayout = itemView.findViewById (R.id.mainwallpaper);
            like_border = itemView.findViewById (R.id.like);

            like_border.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    WallpaperModel wallpaper = wallpaperList.get(position);
                    if (favDB.isFavorite(wallpaper.getWallpaperUrl ())) {
                        favDB.removeFavorite(wallpaper.getWallpaperUrl ());
                        like_border.setImageResource(R.drawable.like_border);
                        Toast.makeText(context, removed_from_favorites, Toast.LENGTH_SHORT).show();
                    } else {
                        favDB.addFavorite(wallpaper.getWallpaperUrl ());
                        like_border.setImageResource(R.drawable.like);
                        Toast.makeText(context, R.string.added_to_favorites, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}