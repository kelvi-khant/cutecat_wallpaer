package com.example.myapplication.Adepter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.FavDB;
import com.example.myapplication.Model.WallpaperModel;
import com.example.myapplication.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class FavoriteWallpapersAdapter extends RecyclerView.Adapter<FavoriteWallpapersAdapter.WallpaperViewHolder> {
    Context context;
    private ArrayList<WallpaperModel> favModelArrayList;
    private FavDB favDB;
    View.OnClickListener onClickListener;
    public FavoriteWallpapersAdapter(Context context, ArrayList<WallpaperModel> favModelArrayList) {
        this.context = context;
        this.favModelArrayList = favModelArrayList;
    }
    @NonNull
    @Override
    public FavoriteWallpapersAdapter.WallpaperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        favDB = new FavDB (context);
        View view = LayoutInflater.from (parent.getContext ()).inflate (R.layout.item_fav, parent, false);
        return new WallpaperViewHolder (view);
    }
    @Override
    public void onBindViewHolder(@NonNull FavoriteWallpapersAdapter.WallpaperViewHolder holder, int position) {
       final WallpaperModel favModel = favModelArrayList.get (position);
        String url = favModel.getWallpaperUrl ();
        Glide.with (context).load (url).into (holder.imageview);

        holder.btnfav.setImageResource (R.drawable.like);
        holder.btnfav.setTag (position);
        holder.imageview.setOnClickListener (onClickListener);
    }
    @Override
    public int getItemCount() {
        return favModelArrayList.size ();
    }

    public class WallpaperViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView imageview;
        ImageView btnfav;
        public WallpaperViewHolder(@NonNull View itemView) {
            super (itemView);

            imageview = itemView.findViewById (R.id.wallpapers);
            btnfav = itemView.findViewById (R.id.fav);
            btnfav.setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
            favDB.removeFromDatabase (favModelArrayList.get (position).getWallpaperUrl ());
            favModelArrayList.remove (favModelArrayList.get (position));
            setItem(favModelArrayList);
            notifyDataSetChanged ();
                }
            });
        }
    }
    public void setItem (ArrayList<WallpaperModel> favModelArrayList){
        this.favModelArrayList = favModelArrayList;
    }
}
