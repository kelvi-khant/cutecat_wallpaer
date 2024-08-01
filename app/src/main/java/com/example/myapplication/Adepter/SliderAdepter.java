package com.example.myapplication.Adepter;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.myapplication.R;
import com.example.myapplication.Model.WallpaperModel;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class SliderAdepter extends RecyclerView.Adapter<SliderAdepter.SwiperHolder> {

    private final Context context;
    private final ArrayList<WallpaperModel> wallpaperList;
    private FavDB favDB;

    public SliderAdepter(ArrayList<WallpaperModel> wallpaperList, Context context) {
        this.wallpaperList = wallpaperList;
        this.context = context;
    }
    @NonNull
    @Override
    public SwiperHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        favDB = new FavDB (context);
        View view = LayoutInflater.from (parent.getContext ()).inflate (R.layout.item_fullimage, parent, false);
        return new SwiperHolder (view);
    }
    @Override
    public void onBindViewHolder(@NonNull SwiperHolder holder, @SuppressLint("RecyclerView") int position) {
        final WallpaperModel post = wallpaperList.get (position);
        String url = post.getWallpaperUrl ();
        Glide.with (context).load (url).into (holder.slider_image);

        if (favDB.isFavorite (post.getWallpaperUrl ())) {
            holder.like_border.setImageResource (R.drawable.like);
        } else {
            holder.like_border.setImageResource (R.drawable.like_border);
        }

        holder.share.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Drawable drawable = holder.slider_image.getDrawable ();
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
                    // If it's a PNG or Bitmap drawable
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                    Bitmap bitmap = bitmapDrawable.getBitmap ();

                    String imagePath = MediaStore.Images.Media.insertImage (
                            context.getContentResolver (),
                            bitmap,
                            "Image",
                            "Image/png"
                    );
                    imageUri = Uri.parse (imagePath);

                    Handler handler = new Handler ();
                    handler.postDelayed (new Runnable () {
                        @Override
                        public void run() {
                            holder.lodingPB.setVisibility (View.INVISIBLE);
                        }
                    }, 2000);

                    Intent intent = new Intent (Intent.ACTION_SEND);
                    intent.setType ("image/*");
                    intent.putExtra (Intent.EXTRA_STREAM, imageUri);
                    context.startActivity (Intent.createChooser (intent, "Share Image"));
                }


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
        holder.lock.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                holder.LockScreen ();
            }
        });
        holder.home.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                String imageUrl = wallpaperList.get(position).getWallpaperUrl();
                holder.setAsWallpaper(imageUrl);
            }
        });
        holder.visible.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                holder.visible_off.setVisibility (View.VISIBLE);
                holder.extralayout.setVisibility (View.VISIBLE);
                holder.visible.setVisibility (View.GONE);
                holder.Llike.setVisibility (View.GONE);
                holder.Llock.setVisibility (View.GONE);
                holder.Lhome.setVisibility (View.GONE);
                holder.Lboth.setVisibility (View.GONE);
                holder.Lshare.setVisibility (View.GONE);
                holder.Ldwnload.setVisibility (View.GONE);
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
            }
        });
        holder.Ldwnload.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Drawable drawable = holder.slider_image.getDrawable ();
                if (drawable instanceof GifDrawable) {
                    holder.downloadGif (position);
                } else if (drawable instanceof BitmapDrawable) {
                    holder.downloadImage (position);
                } else {
                    // Handle other types of drawables if needed
                    Toast.makeText (context, "Unsupported image format", Toast.LENGTH_SHORT).show ();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return wallpaperList.size ();
    }

    class SwiperHolder extends RecyclerView.ViewHolder {
        CardView progresscard;
        RelativeLayout layout;
        LinearLayout extralayout, Llike, Lhome, Llock, Lboth, Lshare, Ldwnload;
        PhotoView slider_image;
        public ImageView share, both, lock, home, like_border, visible, visible_off;
        TextView txtlikes;
        public TextView dateTextView;
        private ProgressBar lodingPB;
        private Handler handler;

        public SwiperHolder(@NonNull View itemView) {
            super (itemView);
            handler = new Handler ();
            lodingPB = itemView.findViewById (R.id.loding);

            Ldwnload = itemView.findViewById (R.id.lodownload);
            layout = itemView.findViewById (R.id.Clayout);
            slider_image = itemView.findViewById (R.id.slider_image);
            share = itemView.findViewById (R.id.share);
            both = itemView.findViewById (R.id.both);
            lock = itemView.findViewById (R.id.lock);
            home = itemView.findViewById (R.id.home);
            like_border = itemView.findViewById (R.id.like);
            txtlikes = itemView.findViewById (R.id.txtlikes);
            visible = itemView.findViewById (R.id.visible);
            visible_off = itemView.findViewById (R.id.visible_off);
            extralayout = itemView.findViewById (R.id.extralayout);
            Llike = itemView.findViewById (R.id.lolike);
            Lhome = itemView.findViewById (R.id.lohome);
            Llock = itemView.findViewById (R.id.lolock);
            Lboth = itemView.findViewById (R.id.loboth);
            Lshare = itemView.findViewById (R.id.loshare);
            dateTextView = itemView.findViewById (R.id.dateTextView);
            progresscard = itemView.findViewById (R.id.progresscard);

            like_border.setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition ();
                    WallpaperModel wallpaper = wallpaperList.get (position);

                    // Check if the wallpaper is already in favorites
                    if (favDB.isFavorite (wallpaper.getWallpaperUrl ())) {
                        favDB.removeFavorite (wallpaper.getWallpaperUrl ());
                        like_border.setImageResource (R.drawable.like_border);
                        Toast.makeText (context, R.string.removed_from_favorites, Toast.LENGTH_SHORT).show ();
                    } else {
                        favDB.addFavorite (wallpaper.getWallpaperUrl ());
                        like_border.setImageResource (R.drawable.like);
                        Toast.makeText (context, R.string.added_to_favorites, Toast.LENGTH_SHORT).show ();
                    }
                }
            });

            Calendar calendar = Calendar.getInstance ();
            String currentdate = DateFormat.getDateInstance (DateFormat.FULL).format (calendar.getTime ());

            dateTextView.setText (currentdate);

        }

        private void LockScreen() {
            progresscard.setVisibility (View.VISIBLE);
            Bitmap bitmap = ((BitmapDrawable) slider_image.getDrawable ()).getBitmap ();
            WallpaperManager wallpaperManager = WallpaperManager.getInstance (context);

            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    wallpaperManager.setBitmap (bitmap, null, true, WallpaperManager.FLAG_LOCK);
                    Handler handler = new Handler ();
                    handler.postDelayed (new Runnable () {
                        @Override
                        public void run() {
                            progresscard.setVisibility (View.INVISIBLE);
                            Toast.makeText (context, R.string.set_wallpaper_on_lock, Toast.LENGTH_SHORT).show ();
                        }
                    }, 2000);

                } else {
                    Toast.makeText (context, R.string.not_set, Toast.LENGTH_SHORT).show ();
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
        private void HomeScreen() {
            progresscard.setVisibility (View.VISIBLE);
            Bitmap bitmap = ((BitmapDrawable) slider_image.getDrawable ()).getBitmap ();
            WallpaperManager wallpaperManager = WallpaperManager.getInstance (context.getApplicationContext ());
            try {
                wallpaperManager.setBitmap (bitmap);
                Handler handler = new Handler ();
                handler.postDelayed (new Runnable () {
                    @Override
                    public void run() {
                        progresscard.setVisibility (View.GONE);
                        Toast.makeText (context, R.string.set_homewallpaper, Toast.LENGTH_SHORT).show ();
                    }
                }, 2000);

            } catch (IOException e) {
                Toast.makeText (context, "Error" + e.getMessage (), Toast.LENGTH_SHORT).show ();
                e.printStackTrace ();
            }
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
                                // Creating a directory and file for image download
                                File dir = new File (Environment.getExternalStoragePublicDirectory (Environment.DIRECTORY_PICTURES), "MyWallpapers");
                                if (!dir.exists ()) {
                                    dir.mkdirs (); // Create the directory if it doesn't exist
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
                                        progresscard.setVisibility (View.GONE);
                                        Toast.makeText (context, R.string.image_saved_to_albums, Toast.LENGTH_SHORT).show ();
                                    }
                                }, 2000);
                            } catch (IOException e) {
                                e.printStackTrace ();
                                Toast.makeText (context, R.string.failed_to_download_image, Toast.LENGTH_SHORT).show ();
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
                            Toast.makeText (context, "gif save", Toast.LENGTH_SHORT).show ();
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
}

