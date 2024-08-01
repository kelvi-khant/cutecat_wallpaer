package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;
import com.example.myapplication.Model.WallpaperModel;
import java.util.ArrayList;

public class FavDB extends SQLiteOpenHelper {
        private Context context;
        private static final String DATABASE_NAME = "favorites.db";
        private static final int DATABASE_VERSION = 1;
        private static final String TABLE_NAME = "favorites";
        private static final String COLUMN_ID = "id";
        private static final String COLUMN_WALLPAPER_URL = "wallpaper_url";
        private static final String COLUMN_FAVORITE_STATUS = "favorite_status"; //1

    public FavDB(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
             String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                    + COLUMN_ID + " STRING PRIMARY KEY, "
                    + COLUMN_WALLPAPER_URL + " TEXT, "
                    + COLUMN_FAVORITE_STATUS + " INTEGER DEFAULT 0);";//2
            db.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    public void addFavorite(String imageUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_WALLPAPER_URL,imageUrl );
        db.insert(TABLE_NAME, null, values);
        db.close();
    }
        public void removeFromDatabase(String url) {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL ("DELETE FROM " + TABLE_NAME + " WHERE " +COLUMN_WALLPAPER_URL+ "='" +url+ "'");
            Toast.makeText(context, "Image remove to favorites", Toast.LENGTH_SHORT).show();
        }

    public void removeFavorite(String imageUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_WALLPAPER_URL + "=?", new String[]{imageUrl});
        db.close();
    }

    public boolean isFavorite(String imageUrl) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_WALLPAPER_URL + "=?", new String[]{imageUrl});
        boolean isFavorite = cursor.getCount() > 0;
        cursor.close();
        return isFavorite;
    }

    @SuppressLint("Range")
    public ArrayList<WallpaperModel> getAllFavorites() {
        ArrayList<WallpaperModel> favoriteList = new ArrayList<> ();
        SQLiteDatabase db = this.getReadableDatabase ();
        Cursor cursor = db.rawQuery ("SELECT * FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst ()) {
            do {
                WallpaperModel wallpaper = new WallpaperModel ();
                wallpaper.setWallpaperUrl (cursor.getString (cursor.getColumnIndex (COLUMN_WALLPAPER_URL)));
                // Set other properties if needed

                favoriteList.add (wallpaper);
            } while (cursor.moveToNext ());
        }
        cursor.close ();
        db.close ();
        return favoriteList;
    }
}
