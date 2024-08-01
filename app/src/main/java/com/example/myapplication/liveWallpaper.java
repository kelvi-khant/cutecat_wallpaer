package com.example.myapplication;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.AsyncTask;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class liveWallpaper extends WallpaperService {
    protected static int playheadTime = 0;

    @Override
    public Engine onCreateEngine() {
        String liveWallpaper = SharedPreferencesManager.getInstance(getApplicationContext()).loadSavedPreferences("liveWallpaper","");
//        String gifUrl = "https://i.pinimg.com/originals/99/b4/61/99b4619b25011ef32d404d198d5102d7.gif";
        return new GIFWallpaperEngine(liveWallpaper);
    }

    private class GIFWallpaperEngine extends Engine {

        private final int frameDuration = 20;

        private SurfaceHolder holder;
        private Movie gifMovie;
        private boolean visible;
        private Handler handler;
        private String gifUrl;

        public GIFWallpaperEngine(String gifUrl) {
            this.gifUrl = gifUrl;
            handler = new Handler();
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            this.holder = surfaceHolder;
            new DownloadGIFTask().execute(gifUrl);
        }

        private Runnable drawGIF = new Runnable() {
            public void run() {
                draw();
            }
        };

        private void draw() {
            if (visible && gifMovie != null) {
                Canvas canvas = holder.lockCanvas();
                if (canvas != null) {
                    try {
                        // Clear the canvas before drawing
                        canvas.drawColor(0, android.graphics.PorterDuff.Mode.CLEAR);

                        float scale = Math.max(
                                (float) canvas.getWidth() / gifMovie.width(),
                                (float) canvas.getHeight() / gifMovie.height()
                        );

                        int scaledWidth = (int) (gifMovie.width() * scale);
                        int scaledHeight = (int) (gifMovie.height() * scale);

                        int xOffset = (canvas.getWidth() - scaledWidth) / 2;
                        int yOffset = (canvas.getHeight() - scaledHeight) / 2;

                        canvas.save();
                        canvas.scale(scale, scale);
                        gifMovie.draw(canvas, xOffset / scale, yOffset / scale);
                        canvas.restore();

                        holder.unlockCanvasAndPost(canvas);
                        gifMovie.setTime((int) (System.currentTimeMillis() % gifMovie.duration()));

                        handler.removeCallbacks(drawGIF);
                        handler.postDelayed(drawGIF, frameDuration);
                    } catch (Exception e) {
                        Log.e("GIF", "Failed to draw GIF", e);
                    }
                }
            }
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            this.visible = visible;
            if (visible) {
                handler.post(drawGIF);
            } else {
                handler.removeCallbacks(drawGIF);
            }
        }

        private class DownloadGIFTask extends AsyncTask<String, Void, Movie> {

            @Override
            protected Movie doInBackground(String... params) {
                try {
                    URL url = new URL(params[0]);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    Movie gifMovie = Movie.decodeStream(inputStream);
                    inputStream.close();
                    urlConnection.disconnect();
                    return gifMovie;
                } catch (IOException e) {
                    Log.e("GIF", "Failed to download GIF from URL", e);
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Movie result) {
                if (result != null) {
                    gifMovie  = result;
                    handler.post(drawGIF);
                }
            }
        }
    }
}