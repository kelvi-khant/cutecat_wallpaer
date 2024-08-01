package com.example.myapplication.Adepter;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.myapplication.FavDB;
import com.example.myapplication.FullWallpaperActivity;
import com.example.myapplication.MainActivity;
import com.example.myapplication.Model.WallpaperModel;
import com.example.myapplication.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.IOException;
import java.util.ArrayList;
public class MultipleLayoutAdapter extends RecyclerView.Adapter<MultipleLayoutAdapter.ViewHolder> {

    public static final int LAYOUT_TYPE_ONE = 1 ;
    public static final int LAYOUT_TYPE_TWO = 2;
    public static final int LAYOUT_TYPE_THREE = 3;
    Context context;
    public static String imagelist;
     ArrayList<WallpaperModel> wallpaperList;
     private int LayoutType;
    private FavDB favDB;


    public MultipleLayoutAdapter(Context context, ArrayList<WallpaperModel> wallpaperList, int LayoutType) {
        this.context = context;
        this.wallpaperList = wallpaperList;
        this.LayoutType = LayoutType;
    }

    public int getItemViewType(int position) {
        return LayoutType;
    }

    @NonNull
    @Override
    public MultipleLayoutAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        favDB = new FavDB (context);
        View view;
        switch (viewType) {
            case LAYOUT_TYPE_ONE:
                view = LayoutInflater.from(context).inflate(R.layout.item_row, parent, false);
                break;
            case LAYOUT_TYPE_TWO:
                view = LayoutInflater.from(context).inflate(R.layout.item_columb, parent, false);
                break;
            case LAYOUT_TYPE_THREE:
                view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
                break;
            default:
                throw new IllegalArgumentException("Invalid viewType");
        }
        return new MultipleLayoutAdapter.ViewHolder (view);
    }

    @Override
    public void onBindViewHolder(@NonNull MultipleLayoutAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        WallpaperModel post =wallpaperList.get(position);
        String url=post.getWallpaperUrl ();

        switch (LayoutType) {
            case LAYOUT_TYPE_ONE:
                Glide.with(context).load(url).into(holder.wallpaper);
                break;
            case LAYOUT_TYPE_TWO:
                Glide.with(context).load(url).into(holder.image2);
                break;
            case LAYOUT_TYPE_THREE:
                Glide.with(context).load(url).into(holder.image);
//                Glide.with(context).asBitmap().load(url).into(holder.image);

                holder.both.setOnClickListener (new View.OnClickListener () {
                    @Override
                    public void onClick(View view) {
                        holder.LockScreen ();
                        String imageUrl = wallpaperList.get(position).getWallpaperUrl();
                        holder.setAsWallpaper(imageUrl);
                    }
                });
                holder.lock.setOnClickListener (new View.OnClickListener () {
                    @Override
                    public void onClick(View view) {
                        holder.LockScreen();
                    }
                });
                holder.home.setOnClickListener (new View.OnClickListener () {
                    @Override
                    public void onClick(View view) {
                        String imageUrl = wallpaperList.get(position).getWallpaperUrl();
                        holder.setAsWallpaper(imageUrl);

                    }
                });
                holder.share.setOnClickListener (new View.OnClickListener () {
                    @Override
                    public void onClick(View view) {

                        Drawable drawable = holder.image.getDrawable();
                        Uri imageUri = null;

                        if (drawable instanceof GifDrawable) {
                            GifDrawable gifDrawable = (GifDrawable) drawable;
                            Toast.makeText (context, "Gif", Toast.LENGTH_SHORT).show ();

                            if (gifDrawable != null) {
                                Intent intent = new Intent (Intent.ACTION_SEND);
                                intent.setType ("image/Gif");
                                intent.putExtra (Intent.EXTRA_STREAM, imageUri);
                                context.startActivity (Intent.createChooser (intent, "Share Image"));
                            }else {
                                Toast.makeText(context, "Error sharing image", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else if (drawable instanceof BitmapDrawable) {
                            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                            Bitmap bitmap = bitmapDrawable.getBitmap();

                            if (bitmap.getConfig() == Bitmap.Config.ARGB_8888) {
                                // It's PNG (assuming the bitmap has alpha channel)
                                String imagePath = MediaStore.Images.Media.insertImage(
                                        context.getContentResolver(),
                                        bitmap,
                                        "Image",
                                        "Image/png"
                                );
                                imageUri = Uri.parse(imagePath);
                            } else {
                                // It's some other format, handle accordingly
                                Toast.makeText(context, "Unsupported image format", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Intent intent = new Intent(Intent.ACTION_SEND );
                            intent.setType("image/*");
                            intent.putExtra(Intent.EXTRA_STREAM, imageUri);
                            context.startActivity(Intent.createChooser(intent, "Share Image"));
                        }

                    }
                });
                break;
            default:
                throw new IllegalArgumentException("Invalid layoutType");
        }

        if (favDB.isFavorite(post.getWallpaperUrl ())) {
            holder.like_border.setImageResource(R.drawable.like);
        } else {
            holder.like_border.setImageResource (R.drawable.like_border);
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
        return wallpaperList.size ();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView image,wallpaper, image2;
        ImageView like_border , share , like;
        public LinearLayout both , home , lock ;
        RelativeLayout relativeLayout;
        CardView progresscard;
        public ViewHolder(@NonNull View itemView) {
            super (itemView);

            progresscard = itemView.findViewById (R.id.progresscard);
            wallpaper = itemView.findViewById(R.id.wallpaper);
            like_border = itemView.findViewById (R.id.like);
            like = itemView.findViewById (R.id.likes);
            image = itemView.findViewById(R.id.image);
            image2 = itemView.findViewById(R.id.imagec);
            both = itemView.findViewById (R.id.bothset);
            home = itemView.findViewById (R.id.homehset);
            lock = itemView.findViewById (R.id.lockhset);
            share = itemView.findViewById (R.id.sharel);
            relativeLayout=itemView.findViewById (R.id.mainwallpaper);

            like_border.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    WallpaperModel wallpaper = wallpaperList.get(position);

                    // Check if the wallpaper is already in favorites
                    if (favDB.isFavorite(wallpaper.getWallpaperUrl ())) {
                        favDB.removeFavorite(wallpaper.getWallpaperUrl ());
                        like_border.setImageResource(R.drawable.like_border);
                        Toast.makeText(context, R.string.removed_from_favorites, Toast.LENGTH_SHORT).show();
                    } else {
                        favDB.addFavorite(wallpaper.getWallpaperUrl ());
                        like_border.setImageResource(R.drawable.like);
                        Toast.makeText(context, R.string.added_to_favorites, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        private void LockScreen() {
            progresscard.setVisibility (View.VISIBLE);
            Bitmap bitmap = ((BitmapDrawable) image.getDrawable ()).getBitmap ();
            Glide.with (context)
                    .asBitmap ()
                    .load (bitmap) // Replace "your_gif_resource" with your GIF resource
                    .into (new SimpleTarget<Bitmap> () {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            WallpaperManager wallpaperManager = WallpaperManager.getInstance (context);
                            try {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    wallpaperManager.setBitmap (resource, null, true, WallpaperManager.FLAG_LOCK);
                                    Handler handler = new Handler ();
                                    handler.postDelayed (new Runnable () {
                                        @Override
                                        public void run() {
                                            progresscard.setVisibility (View.GONE);
                                            Toast.makeText (context,R.string.set_wallpaper_on_lock, Toast.LENGTH_SHORT).show ();
                                        }
                                    }, 2000);
                                } else {
                                    Toast.makeText (context, R.string.not_set, Toast.LENGTH_SHORT).show ();
                                }
                            } catch (IOException e) {
                                Toast.makeText (context, "error " + e.getMessage (), Toast.LENGTH_SHORT).show ();
                                e.printStackTrace ();
                            }
                        }
                    });
        }


//        private void LockScreen() {
//            progresscard.setVisibility (View.VISIBLE);
//            Bitmap bitmap = ((BitmapDrawable) image.getDrawable ()).getBitmap ();
//            WallpaperManager wallpaperManager = WallpaperManager.getInstance (context);
//
//            try {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    wallpaperManager.setBitmap (bitmap, null, true, WallpaperManager.FLAG_LOCK);
//                    Handler handler = new Handler ();
//                    handler.postDelayed (new Runnable () {
//                        @Override
//                        public void run() {
//                            progresscard.setVisibility (View.GONE);
//                            Toast.makeText (context,R.string.set_wallpaper_on_lock, Toast.LENGTH_SHORT).show ();
//                        }
//                    }, 2000);
//                } else {
//                    Toast.makeText (context,R.string.not_set, Toast.LENGTH_SHORT).show ();
//                }
//
//            } catch (IOException e) {
//                Toast.makeText (context, "Error" + e.getMessage (), Toast.LENGTH_SHORT).show ();
//                e.printStackTrace ();
//            }
//        }

        private void setAsWallpaper(String imageUrl) {
            if (imageUrl.endsWith(".gif")) {
                setJpgOrPngAsWallpaper(imageUrl);
            } else {
                setJpgOrPngAsWallpaper(imageUrl);
            }
        }
        private void setJpgOrPngAsWallpaper(String imageUrl) {
            progresscard.setVisibility (View.VISIBLE);
            Glide.with(context)
                    .asBitmap()
                    .load(imageUrl)
                    .into(new CustomTarget<Bitmap> () {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
                            try {
                                wallpaperManager.setBitmap(resource);
                                Handler handler = new Handler ();
                                handler.postDelayed (new Runnable () {
                                    @Override
                                    public void run() {
                                        progresscard.setVisibility (View.GONE);
                                        Toast.makeText (context,R.string.set_homewallpaper, Toast.LENGTH_SHORT).show ();
                                    }
                                }, 2000);
                            } catch (IOException e) {
                                Toast.makeText (context, "Error" + e.getMessage (), Toast.LENGTH_SHORT).show ();
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                            // Handle when resource is cleared
                        }
                    });
        }
        private void HomeScreen() {
            progresscard.setVisibility (View.VISIBLE);
            Bitmap bitmap = ((BitmapDrawable) image.getDrawable ()).getBitmap ();
            WallpaperManager wallpaperManager = WallpaperManager.getInstance (context.getApplicationContext ());
            try {
                wallpaperManager.setBitmap (bitmap);
                Handler handler = new Handler ();
                handler.postDelayed (new Runnable () {
                    @Override
                    public void run() {
                        progresscard.setVisibility (View.GONE);
                        Toast.makeText (context,R.string.set_homewallpaper, Toast.LENGTH_SHORT).show ();
                    }
                }, 2000);

            } catch (IOException e) {
                Toast.makeText (context, "Error" + e.getMessage (), Toast.LENGTH_SHORT).show ();
                e.printStackTrace ();
            }
        }
    }
}