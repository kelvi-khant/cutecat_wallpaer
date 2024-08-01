package com.example.myapplication.Adepter;

import static com.google.android.material.internal.ContextUtils.getActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.myapplication.FavDB;
import com.example.myapplication.Fragment.ForYouFragment;
import com.example.myapplication.FullWallpaperActivity;
import com.example.myapplication.MainActivity;
import com.example.myapplication.Model.WallpaperModel;
import com.example.myapplication.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class FullScreenAdapter extends RecyclerView.Adapter <FullScreenAdapter.viewholder> {
    private Context context;
    private ArrayList<WallpaperModel> wallpaperList;
    public static String imagelist;
    private FavDB favDB;
    public FullScreenAdapter(Context context, ArrayList<WallpaperModel> wallpaperList) {
        this.context = context;
        this.wallpaperList = wallpaperList;
    }
    @NonNull
    @Override
    public FullScreenAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        favDB = new FavDB (context);
        View view = LayoutInflater.from (context).inflate (R.layout.item_fullscreen, parent, false);
        return new FullScreenAdapter.viewholder (view);
    }
    @Override
    public void onBindViewHolder(@NonNull FullScreenAdapter.viewholder holder, @SuppressLint("RecyclerView") int position) {
        WallpaperModel post = wallpaperList.get (position);
        String url = post.getWallpaperUrl ();
        Glide.with (context).load (url).into (holder.image);

        if (favDB.isFavorite(post.getWallpaperUrl ())) {
            holder.like_border.setImageResource(R.drawable.like);
        } else {
            holder.like_border.setImageResource(R.drawable.like_border);
        }
        holder.relativeLayout.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                MainActivity.wallpaperList = wallpaperList;
                Intent intent = new Intent (context, FullWallpaperActivity.class);
                intent.putExtra ("image", position);
                imagelist = post.getWallpaperUrl ();
                context.startActivity (intent);
            }
        });
        holder.visible.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                holder.visible_off.setVisibility (View.VISIBLE);
                holder.extralayout.setVisibility (View.VISIBLE);
                holder.visible.setVisibility (View.INVISIBLE);
                holder.Llike.setVisibility (View.INVISIBLE);
                holder.Llock.setVisibility (View.INVISIBLE);
                holder.Lhome.setVisibility (View.INVISIBLE);
                holder.Lboth.setVisibility (View.INVISIBLE);
                holder.Lshare.setVisibility (View.INVISIBLE);
                holder.Ldwnload.setVisibility (View.INVISIBLE);
                holder.lofull.setVisibility (View.INVISIBLE);
            }
        });
        holder.visible_off.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                holder.visible_off.setVisibility (View.GONE);
                holder.extralayout.setVisibility (View.GONE);
                holder.visible.setVisibility (View.VISIBLE);
                holder.Llike.setVisibility (View.VISIBLE);
                holder.Llock.setVisibility (View.VISIBLE);
                holder.Lhome.setVisibility (View.VISIBLE);
                holder.Lboth.setVisibility (View.VISIBLE);
                holder.Lshare.setVisibility (View.VISIBLE);
                holder.Ldwnload.setVisibility (View.VISIBLE);
                holder.lofull.setVisibility (View.VISIBLE);
            }
        });
        holder.home.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                    String imageUrl = wallpaperList.get(position).getWallpaperUrl();
                    holder.setAsWallpaper(imageUrl);
                }
        });
        holder.lock.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                holder.LockScreen ();
            }
        });
        holder.both.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                holder.LockScreen ();
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
                    Toast.makeText (context, "gif", Toast.LENGTH_SHORT).show ();
                    if (gifDrawable != null) {
                        Intent intent = new Intent (Intent.ACTION_SEND);
                        intent.setType ("image/gif");
                        intent.putExtra (Intent.EXTRA_STREAM, imageUri);
                        context.startActivity (Intent.createChooser (intent, "Share Image"));
                    }else {
                        Toast.makeText(context, "Error sharing image", Toast.LENGTH_SHORT).show();
                    }
                } else if (drawable instanceof BitmapDrawable) {
                    imageUri = saveBitmapAndGetUri(context, ((BitmapDrawable) drawable).getBitmap());
                    if (imageUri != null) {
                        Intent intent = new Intent (Intent.ACTION_SEND);
                        intent.setType ("image/*");
                        intent.putExtra (Intent.EXTRA_STREAM, imageUri);
                        context.startActivity (Intent.createChooser (intent, "Share Image"));
                    }else {
                        Toast.makeText(context, "Error sharing image", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        holder.download.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Drawable drawable = holder.image.getDrawable ();
                if (drawable instanceof GifDrawable) {
                    holder.downloadGif (position);
                } else if (drawable instanceof BitmapDrawable) {
                    holder.downloadImage (position);
                } else {
                    Toast.makeText (context, "Unsupported image format", Toast.LENGTH_SHORT).show ();
                }
            }
        });
        holder.close.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                MainActivity.fullscreen.setVisibility (View.GONE);
                ForYouFragment.main.setVisibility (View.VISIBLE);
                ColorStateList colorStateList = ColorStateList.valueOf(Color.BLACK); // Change to your desired color
                MainActivity.bottomNavigationView.setItemTextColor(colorStateList);
                MainActivity.bottomNavigationView.setItemIconTintList (colorStateList);
                MainActivity.bottomNavigationView.setItemTextAppearanceActive(R.style.TextStyle);
                MainActivity.bottomNavigationView.setItemTextAppearanceInactive(R.style.TextStyle);
            }
        });
        holder.full.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                MainActivity.fullscreen.setVisibility (View.GONE);
                ForYouFragment.main.setVisibility (View.VISIBLE);
                ColorStateList colorStateList = ColorStateList.valueOf(Color.BLACK); // Change to your desired color
                MainActivity.bottomNavigationView.setItemTextColor(colorStateList);
                MainActivity.bottomNavigationView.setItemIconTintList (colorStateList);

                MainActivity.bottomNavigationView.setItemTextAppearanceActive(R.style.TextStyle);
                MainActivity.bottomNavigationView.setItemTextAppearanceInactive(R.style.TextStyle);

//                @SuppressLint("RestrictedApi") Activity activity = getActivity(context);
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
    }
    @Override
    public int getItemCount() {
        return wallpaperList.size ();
    }
    public class viewholder extends RecyclerView.ViewHolder {
        public ImageView image;
        RelativeLayout relativeLayout;
        ProgressBar loding;
        CardView progresscard;
        TextView txtlikes;
        public TextView dateTextView;
        LinearLayout extralayout, Llike, Lhome, Llock, Lboth, Lshare, Ldwnload , lofull;
        public ImageView full,share, both, lock, home, like_border, like_fav, visible, visible_off, download , close;

        public viewholder(@NonNull View itemView) {
            super (itemView);

            extralayout = itemView.findViewById (R.id.extralayoutfull);
            lofull = itemView.findViewById (R.id.lofull);
            Llike = itemView.findViewById (R.id.lolike);
            Lhome = itemView.findViewById (R.id.lohome);
            Llock = itemView.findViewById (R.id.lolock);
            Lboth = itemView.findViewById (R.id.loboth);
            Lshare = itemView.findViewById (R.id.loshare);
            Ldwnload = itemView.findViewById (R.id.lodownload);
            txtlikes = itemView.findViewById (R.id.txtlikes);
            full = itemView.findViewById (R.id.full);
            image = itemView.findViewById (R.id.imagefull);
            relativeLayout = itemView.findViewById (R.id.mainwallpaper);
            visible = itemView.findViewById (R.id.visible);
            visible_off = itemView.findViewById (R.id.visible_off);
            like_border = itemView.findViewById (R.id.like);
            like_fav = itemView.findViewById (R.id.likes);
            home = itemView.findViewById (R.id.home);
            lock = itemView.findViewById (R.id.lock);
            both = itemView.findViewById (R.id.both);
            share = itemView.findViewById (R.id.share);
            download = itemView.findViewById (R.id.download);
            loding = itemView.findViewById (R.id.loding);
            progresscard = itemView.findViewById (R.id.progresscard);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            close = itemView.findViewById (R.id.imgclose);
            like_border.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    WallpaperModel wallpaper = wallpaperList.get(position);

                    // Check if the wallpaper is already in favorites
                    if (favDB.isFavorite(wallpaper.getWallpaperUrl ())) {
                        favDB.removeFavorite(wallpaper.getWallpaperUrl ());
                        like_border.setImageResource(R.drawable.like_border);
                        Toast.makeText(context,R.string.removed_from_favorites, Toast.LENGTH_SHORT).show();
                    } else {
                        favDB.addFavorite(wallpaper.getWallpaperUrl ());
                        like_border.setImageResource(R.drawable.like);
                        Toast.makeText(context, R.string.added_to_favorites, Toast.LENGTH_SHORT).show();
                    }
                }
            });
            Calendar calendar = Calendar.getInstance();
            String currentdate = DateFormat.getDateInstance (DateFormat.FULL).format (calendar.getTime ());
            dateTextView.setText (currentdate);
        }
        private void LockScreen() {
            progresscard.setVisibility (View.VISIBLE);
            Bitmap bitmap = ((BitmapDrawable) image.getDrawable ()).getBitmap ();
            WallpaperManager wallpaperManager = WallpaperManager.getInstance (context);

            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    wallpaperManager.setBitmap (bitmap, null, true, WallpaperManager.FLAG_LOCK);
                    Handler handler = new Handler ();
                    handler.postDelayed (new Runnable () {
                        @Override
                        public void run() {
                            progresscard.setVisibility (View.GONE);
                            Toast.makeText (context,R.string.set_wallpaper_on_lock, Toast.LENGTH_SHORT).show ();
                        }
                    }, 2000);
                } else {
                    Toast.makeText (context,R.string.not_set, Toast.LENGTH_SHORT).show ();
                }
            } catch (IOException e) {
                Toast.makeText (context, "Error" + e.getMessage (), Toast.LENGTH_SHORT).show ();
                e.printStackTrace ();
            }
        }
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
                    .into(new CustomTarget<Bitmap>() {
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
        private void downloadImage(int position) {
            progresscard.setVisibility (View.VISIBLE);
            String imageUrl = wallpaperList.get (position).getWallpaperUrl ();
            Glide.with (context)
                    .asBitmap ()
                    .load (imageUrl)
                    .into (new CustomTarget<Bitmap> () {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            try {
                                File dir = new File (Environment.getExternalStoragePublicDirectory (Environment.DIRECTORY_PICTURES), "MyWallpapers");
                                if (!dir.exists ()) {
                                    dir.mkdirs ();
                                }
                                String filename = String.format ("%d.jpg", System.currentTimeMillis ());
                                File file = new File (dir, filename);
                                FileOutputStream fileOutputStream = new FileOutputStream (file);
                                resource.compress (Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                                fileOutputStream.flush ();
                                fileOutputStream.close ();
                                // Broadcasting MediaScanner to notify image addition
                                Intent intent = new Intent (Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                                intent.setData (Uri.fromFile (file));
                                context.sendBroadcast (intent);
                                Handler handler = new Handler ();
                                handler.postDelayed (new Runnable () {
                                    @Override
                                    public void run() {
                                        progresscard.setVisibility (View.INVISIBLE);
                                        Toast.makeText (context,R.string.image_saved_to_albums, Toast.LENGTH_SHORT).show ();
                                    }
                                }, 2000);
                            } catch (IOException e) {
                                e.printStackTrace ();
                                Toast.makeText (context,R.string.failed_to_download_image, Toast.LENGTH_SHORT).show ();
                            }
                        }
                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                            // Handle when resource is cleared
                        }
                    });
        }
        private void downloadGif(int position) {
            progresscard.setVisibility (View.VISIBLE);
            String gifUrl = wallpaperList.get (position).getWallpaperUrl ();
            new DownloadTask ().execute (gifUrl);
        }
        private class DownloadTask extends AsyncTask<String, Void, Void> {
            @Override
            protected Void doInBackground(String... urls) {
                try {
                    URL url = new URL (urls[0]);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection ();
                    connection.connect ();

                    InputStream inputStream = connection.getInputStream ();
                    File dir = new File (Environment.getExternalStoragePublicDirectory (Environment.DIRECTORY_PICTURES), "MyGifs");
                    if (!dir.exists ()) {
                        dir.mkdirs (); // Create the directory if it doesn't exist
                    }
                    String filename = String.format ("%d.gif", System.currentTimeMillis ());
                    File file = new File (dir, filename);
                    FileOutputStream fileOutputStream = new FileOutputStream (file);

                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = inputStream.read (buffer)) != -1) {
                        fileOutputStream.write (buffer, 0, len);
                    }
                    fileOutputStream.flush ();
                    fileOutputStream.close ();
                    inputStream.close ();
                    connection.disconnect ();

                    // Broadcasting MediaScanner to notify gif addition
                    Intent intent = new Intent (Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData (Uri.fromFile (file));
                    context.sendBroadcast (intent);

                    // Delayed Toast to indicate download completion
                    Handler handler = new Handler (Looper.getMainLooper ());
                    handler.postDelayed (new Runnable () {
                        @Override
                        public void run() {
                            progresscard.setVisibility (View.GONE);
//                            Toast.makeText (context, "gif save", Toast.LENGTH_SHORT).show ();
                        }
                    }, 2000);
                } catch (IOException e) {
                    e.printStackTrace ();
                    Handler handler = new Handler (Looper.getMainLooper ());
                    handler.post (new Runnable () {
                        @Override
                        public void run() {
                            progresscard.setVisibility (View.GONE);
                            Toast.makeText (context, "failed_to_download_gif", Toast.LENGTH_SHORT).show ();
                        }
                    });
                }
                return null;
            }
        }
    }
    private Uri saveGifAndGetUri(Context context, GifDrawable gifDrawable) {
        try {
            File gifFile = new File(context.getExternalCacheDir(), "shared_gif.gif");
            FileOutputStream out = new FileOutputStream(gifFile);
            gifDrawable.getBuffer().rewind();
            out.write(gifDrawable.getBuffer().array());
            out.close();
            return Uri.fromFile(gifFile);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private Uri saveBitmapAndGetUri(Context context, Bitmap bitmap) {
        try {
            File imageFile = new File(context.getExternalCacheDir(), "shared_image.jpg");
            FileOutputStream out = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
            return Uri.fromFile(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
